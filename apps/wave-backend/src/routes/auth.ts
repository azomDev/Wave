import { Elysia } from "elysia";

export const authRoutes = () =>
    new Elysia().group("/auth", (app) =>
        app.post("/login", () => {
            console.log("login");
        })
    );
