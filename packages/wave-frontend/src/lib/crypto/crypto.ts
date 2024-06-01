export interface EncryptedData {
    data: Uint8Array;
}

export interface EncryptionHandler<D extends EncryptedData = EncryptedData> {
    encrypt(data: Uint8Array): Promise<D>;
    decrypt(encryptedData: D): Promise<Uint8Array>;

    encryptString(data: string): Promise<D>;
    decryptString(encryptedData: D): Promise<string>;

    _getPrivateKey(): Promise<CryptoKey>;
}

export interface KeyWrapHandler {
    wrap(key: CryptoKey | EncryptionHandler): Promise<Uint8Array>;
    unwrap(encryptedKey: Uint8Array): Promise<CryptoKey>;
}
