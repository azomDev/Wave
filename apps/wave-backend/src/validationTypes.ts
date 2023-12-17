import { t } from "elysia";

export const bcriptHashType = t.String({
    pattern: /^\$2[ayb]?\$[0-9]{2}\$[./A-Za-z0-9]{53}$/.source,
});

export const uint8ArrayType = t.Array(t.Number({ minimum: 0, maximum: 255 }));
