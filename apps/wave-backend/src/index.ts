import { Elysia, t } from "elysia";
import type { Conversation } from "ui";

let temp: Conversation[] = [{ name: "Convo 1", id: 123456 }]; // This is to simulate a database with the conversations

const app = new Elysia()
    .get("/", () => "Hello Elysia")
    .ws("/chat/:chatId", {
        params: t.Object({ chatId: t.String({ minLength: 6, maxLength: 6 }) }),
        body: t.Object({
            username: t.String(),
            message: t.String(),
        }),
        open(ws) {
            const msg = `${ws.remoteAddress} has entered the chat`;

            ws.send(temp[0]); // see comment on temp definition

            ws.subscribe("the-group-chat");
            ws.publish("the-group-chat", { message: msg });
        },
        message(ws, message) {
            console.log(
                "Received",
                message.message,
                "from",
                message.username,
                "in convo",
                ws.data.params.chatId
            );
            ws.send("The message has been sent to the group.");
            // the server re-broadcasts incoming messages to everyone
            ws.publish("the-group-chat", message);
        },
        close(ws) {
            const msg = `${ws.remoteAddress} has left the chat`;
            ws.publish("the-group-chat", { message: msg });
            ws.unsubscribe("the-group-chat");
        },
    })
    .listen(3000);

console.log(`🦊 Elysia is running at ${app.server?.hostname}:${app.server?.port}`);

export type App = typeof app;
