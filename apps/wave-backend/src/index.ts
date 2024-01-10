import { Elysia, t } from "elysia";

const app = new Elysia()
    .get("/", () => "Hello Elysia")
    .ws("/client", {
        body: t.Object({
            username: t.String(),
            message: t.String(),
        }),
        open(ws) {
            const msg = `${ws.data.body.username} has entered the chat`;
            ws.subscribe("the-group-chat");
            ws.publish("the-group-chat", { message: msg });
        },
        message(ws, message) {
            console.log("Received", message.username, "from", message.username);
            ws.send("The message has been sent to the group.");
            // the server re-broadcasts incoming messages to everyone
            ws.publish("the-group-chat", message);
        },
        close(ws) {
            const msg = `${ws.data.body.username} has left the chat`;
            ws.publish("the-group-chat", { message: msg });
            ws.unsubscribe("the-group-chat");
        },
    })
    .listen(3000);

console.log(`🦊 Elysia is running at ${app.server?.hostname}:${app.server?.port}`);

export type App = typeof app;
