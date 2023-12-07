import { Elysia } from "elysia";

export const authRoutes = (app: Elysia) =>
    app.group("/auth", (app) =>
        app.post("/login", () => {
            console.log("login");
        })
    );
