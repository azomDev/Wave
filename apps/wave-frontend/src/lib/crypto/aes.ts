import type { EncryptedData, EncryptionHandler } from "./crypto";

function bufferToString(buffer: ArrayLike<number> | ArrayBufferLike): string {
    return btoa(String.fromCharCode(...new Uint8Array(buffer)));
}

function stringToBuffer(str: string): ArrayBuffer {
    return Uint8Array.from(atob(str), (c) => c.charCodeAt(0)).buffer as ArrayBuffer;
}

interface EncryptedDataAES extends EncryptedData {
    data: string;
    iv: string;
}

export class EncryptionAES implements EncryptionHandler<EncryptedDataAES> {
    #masterKey: Promise<CryptoKey>;

    constructor(seed: string) {
        this.#masterKey = new Promise(async (res) => {
            const passkey = window.crypto.subtle.importKey(
                "raw",
                new TextEncoder().encode(seed),
                "PBKDF2",
                false,
                ["deriveBits", "deriveKey"]
            );

            const masterKey = window.crypto.subtle.deriveKey(
                {
                    name: "PBKDF2",
                    // TODO: add salt (from account id?)
                    // salt: window.crypto.getRandomValues(new Uint8Array(16)),
                    salt: new Uint8Array(16),
                    iterations: 110000,
                    hash: "SHA-256",
                },
                await passkey,
                { name: "AES-GCM", length: 256 },
                true,
                ["encrypt", "decrypt"]
            );

            res(masterKey);
        });
    }

    async encrypt(password: string) {
        const iv = window.crypto.getRandomValues(new Uint8Array(12));
        const buffer = await window.crypto.subtle.encrypt(
            { name: "AES-GCM", iv },
            await this.#masterKey,
            new TextEncoder().encode(password)
        );

        return { data: bufferToString(buffer), iv: bufferToString(iv) };
    }

    async decrypt(encryptedData: EncryptedDataAES) {
        const decryptedBuffer = await window.crypto.subtle.decrypt(
            { name: "AES-GCM", iv: stringToBuffer(encryptedData.iv) },
            await this.#masterKey,
            stringToBuffer(encryptedData.data)
        );

        return new TextDecoder().decode(decryptedBuffer);
    }
}
