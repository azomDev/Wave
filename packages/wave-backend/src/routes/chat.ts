import { type Elysia, t } from "elysia";

export const chatRoutes = (app: Elysia) =>
    app.group("/chat", (app) =>
        app.ws("/:chatId", {
            params: t.Object({ chatId: t.String({ minLength: 6, maxLength: 6 }) }),
            body: t.Object({
                modified: t.Boolean({ default: false }),

                type: t.Enum({ text: "text" }),
                message: t.Any(),
            }),
            response: t.Object({
                id: t.Number(),
                sender: t.String(),

                modified: t.Boolean({ default: false }),
                time_sent: t.Number(),

                type: t.Enum({ text: "text" }),
                message: t.Any(),
            }),
            open(ws) {
                const msg = `${ws.remoteAddress} has entered the chat`;
                // ws.send(temp[0]); // see comment on temp definition

                ws.subscribe(ws.data.params.chatId);
            },
            message(ws, message) {
                ws.send({
                    ...message,
                    id: 0,
                    sender: ws.remoteAddress,
                    time_sent: Date.now(),
                });

                // the server re-broadcasts incoming messages to everyone
                ws.publish(ws.data.params.chatId, {
                    ...message,
                    id: 0,
                    sender: ws.remoteAddress,
                    time_sent: Date.now(),
                });
            },
            close(ws) {
                const msg = `${ws.remoteAddress} has left the chat`;
                // ws.publish(ws.data.params.chatId, { data: msg });
                ws.unsubscribe(ws.data.params.chatId);
            },
        }),
    );
