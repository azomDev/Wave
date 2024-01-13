import { api } from "$lib/api";
import { readable, readonly, writable } from "svelte/store";
import type { Conversation, Message } from "wave-types";

export const conversations = readable<Conversation[]>([{ id: 123456, name: "Test" }, { id: 111111, name: "Test 2" }]);

export function connectConversation(id: number) {
    let conversation: Conversation | undefined;
    conversations.subscribe((c) => (conversation = c.find((c) => (c.id = id))))();
    const chat = api.chat[id].subscribe();

    const messages = writable<Message[]>([]);

    // const encryption = new EncryptionAES("password");

    chat.subscribe(async ({ data }) => {
        // const message = await encryption.decrypt(data.message);
        console.log(data);
        messages.update((prev) => {
            for (const message of prev) { // yes, this is cursed
                if (message.time_sent == data.time_sent) {
                    message.sent = true;
                    continue;
                }
            }
            // prev.push({ ...data, sent: true });
            return prev;
        });
    }); 

    async function sendMessage(message: Message) {
        messages.update((prev) => {
            prev.push(message);
            return prev;
        });
        chat.send(message);
    }

    return {
        conversation: conversation,
        messages: readonly(messages),
        send: sendMessage,
    };
}
