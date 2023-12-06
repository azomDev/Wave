import { Elysia } from "elysia";

import { authRoutes } from "./routes/auth";
import { chatRoutes } from "./routes/chat";

const app = new Elysia()
    .get("/", () => {
        return "Hello Elysia";
    })
    .use(chatRoutes)
    .use(authRoutes)
    .listen(3000);

console.log(`🦊 Elysia is running at ${app.server?.hostname}:${app.server?.port}`);

export type App = typeof app;
