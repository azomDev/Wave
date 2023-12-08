export interface EncryptedData {
    data: string;
}

export interface EncryptionHandler<D extends EncryptedData> {
    encrypt(password: string): Promise<D>;
    decrypt(encryptedData: D): Promise<string>;
}
