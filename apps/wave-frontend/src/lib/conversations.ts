import { api } from "$lib/api";
import { readable, readonly, writable } from "svelte/store";
import type { Conversation, Message } from "wave-types";
import { EncryptionAES } from "./crypto/aes";

export const conversations = readable<Conversation[]>([{ id: 123456, name: "Test" }]);

export function connectConversation(id: number) {
    let conversation: Conversation | undefined;
    conversations.subscribe((c) => (conversation = c.find((c) => (c.id = id))))();
    const chat = api.chat[id].subscribe();

    const messages = writable<Message[]>([]);

    const encryption = new EncryptionAES("password");

    chat.subscribe(async ({ data }) => {
        const message = await encryption.decrypt(data.message);
        messages.update((prev) => {
            prev.push({ ...data, sent: true, message });
            return prev;
        });
    });

    async function sendMessage(message: string) {
        chat.send({
            type: "text",
            message: await encryption.encrypt(message),
            modified: false,
        });
    }

    return {
        conversation: conversation,
        messages: readonly(messages),
        send: sendMessage,
    };
}
