import { Elysia, t } from "elysia";
import { bcriptHashType, uint8ArrayType } from "../validationTypes";

export const authRoutes = (app: Elysia) =>
    app.group("/auth", (app) =>
        app
            .post("/signup", ({ body }) => {}, {
                body: t.Object({
                    username: t.String(),
                    displayName: t.String(),
                    hash: bcriptHashType,
                    privateKey: uint8ArrayType,
                    publicKey: uint8ArrayType,
                    masterKey: uint8ArrayType,
                }),
            })
            .post(
                "/login",
                () => {
                    console.log("login");
                },
                {
                    body: t.Object({
                        username: t.String(),
                        hash: bcriptHashType,
                    }),
                }
            )
    );
