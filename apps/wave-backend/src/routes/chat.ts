import { Elysia, t } from "elysia";

export const chatRoutes = (app: Elysia) =>
    app.group("/chat", (app) =>
        app.ws("/:chatId", {
            params: t.Object({ chatId: t.String({ minLength: 6, maxLength: 6 }) }),
            body: t.Object({
                id: t.String(),
                sender: t.String(),

                modified: t.Boolean({ default: false }),
                time_sent: t.Number(),
                sent: t.Boolean({default: true}),

                message: t.Any(),
            }),
            response: t.Object({
                id: t.String(),
                sender: t.String(),

                modified: t.Boolean({ default: false }),
                time_sent: t.Number(),
                sent: t.Boolean({default: true}),

                message: t.Any(),
            }),
            open(ws) {
                console.log("SERVER: Someone entered the chat");
                const msg = `${ws.remoteAddress} has entered the chat`;
                // ws.send(temp[0]); // see comment on temp definition

                ws.subscribe(ws.data.params.chatId);
            },
            message(ws, message) {
                console.log("SERVER: Someone sent a message");
                ws.send({
                    ...message,
                    sender: ws.remoteAddress,
                });

                // the server re-broadcasts incoming messages to everyone
                ws.publish(ws.data.params.chatId, {
                    ...message,
                    sender: ws.remoteAddress,
                    sent: true,
                });
            },
            close(ws) {
                const msg = `${ws.remoteAddress} has left the chat`;
                // ws.publish(ws.data.params.chatId, { data: msg });
                ws.unsubscribe(ws.data.params.chatId);
            },
        })
    );
