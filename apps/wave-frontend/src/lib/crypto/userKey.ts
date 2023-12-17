import type { EncryptionHandler, KeyWrapHandler } from "./crypto";

export class KeyWrapperAES implements KeyWrapHandler {
    #secretKey: Promise<CryptoKey>;

    constructor(passowrd: string, userId: string) {
        this.#secretKey = new Promise(async (res) => {
            // Import the password as a CryptoKey
            const passkey = window.crypto.subtle.importKey(
                "raw",
                new TextEncoder().encode(passowrd),
                "PBKDF2",
                false,
                ["deriveKey"]
            );

            // Derive an AES key from it
            const secretKey = window.crypto.subtle.deriveKey(
                {
                    name: "PBKDF2",
                    salt: new TextEncoder().encode(userId),
                    iterations: 110000,
                    hash: "SHA-256",
                },
                await passkey,
                { name: "AES-KW", length: 256 },
                false,
                ["wrapKey", "unwrapKey"]
            );

            res(secretKey);
        });
    }

    async wrap(key: CryptoKey | EncryptionHandler<any>) {
        const buffer = await window.crypto.subtle.wrapKey(
            "raw",
            key instanceof CryptoKey ? key : await key._getPrivateKey(),
            await this.#secretKey,
            "AES-KW"
        );

        return new Uint8Array(buffer);
    }

    async unwrap(encryptedKey: Uint8Array) {
        return await window.crypto.subtle.unwrapKey(
            "raw",
            encryptedKey,
            await this.#secretKey,
            "AES-KW",
            "AES-GCM",
            false,
            ["encrypt", "decrypt"]
        );
    }
}
