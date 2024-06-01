import { t } from "elysia";

export const bcriptHashType = t.String({
    pattern: /^\$2[ayb]?\$[0-9]{2}\$[./A-Za-z0-9]{53}$/.source,
});

export const uint8ArrayType = t.String({ pattern: /^[-A-Za-z0-9+/]*={0,3}$/.source });

export const encryptedDataType = t.Object({
    data: uint8ArrayType,
    iv: uint8ArrayType,
});
