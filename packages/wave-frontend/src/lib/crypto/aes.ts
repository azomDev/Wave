import type { EncryptedData, EncryptionHandler } from "./crypto";

interface EncryptedDataAES extends EncryptedData {
    data: Uint8Array;
    iv: Uint8Array;
}

export class EncryptionAES implements EncryptionHandler<EncryptedDataAES> {
    #masterKey: Promise<CryptoKey>;

    constructor(key?: CryptoKey) {
        if (key) {
            this.#masterKey = Promise.resolve(key);
        } else {
            this.#masterKey = window.crypto.subtle.generateKey(
                {
                    name: "AES-GCM",
                    length: 256,
                },
                true,
                ["encrypt", "decrypt", "wrapKey", "unwrapKey"],
            );
        }
    }

    async encrypt(data: Uint8Array) {
        const iv = window.crypto.getRandomValues(new Uint8Array(12));
        const buffer = await window.crypto.subtle.encrypt({ name: "AES-GCM", iv }, await this.#masterKey, data);

        return { data: new Uint8Array(buffer), iv };
    }

    async decrypt(encryptedData: EncryptedDataAES) {
        const decryptedBuffer = await window.crypto.subtle.decrypt(
            { name: "AES-GCM", iv: encryptedData.iv },
            await this.#masterKey,
            encryptedData.data,
        );

        return new Uint8Array(decryptedBuffer);
    }

    async encryptString(data: string) {
        return this.encrypt(new TextEncoder().encode(data));
    }

    async decryptString(encryptedData: EncryptedDataAES) {
        return new TextDecoder().decode(await this.decrypt(encryptedData));
    }

    async _getPrivateKey() {
        return await this.#masterKey;
    }
}
