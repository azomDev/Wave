import { type Elysia, t } from "elysia";
import { bcriptHashType, encryptedDataType, uint8ArrayType } from "../validationTypes";

export const authRoutes = (app: Elysia) =>
    app.group("/auth", (app) =>
        app
            .post(
                "/signup",
                ({ body }) => {
                    console.log("🚀 ~ .post ~ body:", body);
                    return { test: true };
                },
                {
                    body: t.Object({
                        username: t.String(),
                        displayName: t.String(),
                        // hash: bcriptHashType,
                        masterKey: uint8ArrayType,
                        privateKey: encryptedDataType,
                        publicKey: uint8ArrayType,
                    }),
                },
            )
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
                },
            ),
    );
