import { Elysia, t } from "elysia";

const app = new Elysia()
    .get("/", () => {
        return "Hello Elysia";
    })
    .group("/chat", (app) =>
        app.ws("/:chatId", {
            params: t.Object({ chatId: t.String({ minLength: 6, maxLength: 6 }) }),
            body: t.Object({
                id: t.Number(),
                sender: t.String(),

                time_sent: t.Number(),
                modified: t.Boolean(),

                type: t.Enum({ text: "text" }),
                data: t.Any(),
            }),
            response: t.Object({
                id: t.Number(),
                sender: t.String(),

                time_sent: t.Number(),
                modified: t.Boolean(),

                type: t.Enum({ text: "text" }),
                data: t.Any(),
            }),
            open(ws) {
                const msg = `${ws.remoteAddress} has entered the chat`;
                // ws.send(temp[0]); // see comment on temp definition

                ws.subscribe(ws.data.params.chatId);
            },
            message(ws, message) {
                console.log(
                    "Received",
                    message.data,
                    "from",
                    message.sender,
                    "in convo",
                    ws.data.params.chatId
                );
                ws.send(message);
                // the server re-broadcasts incoming messages to everyone
                ws.publish(ws.data.params.chatId, message);
            },
            close(ws) {
                const msg = `${ws.remoteAddress} has left the chat`;
                // ws.publish(ws.data.params.chatId, { data: msg });
                ws.unsubscribe(ws.data.params.chatId);
            },
        })
    )
    .listen(3000);

console.log(`🦊 Elysia is running at ${app.server?.hostname}:${app.server?.port}`);

export type App = typeof app;
