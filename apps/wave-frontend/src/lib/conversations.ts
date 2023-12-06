import { api } from "$lib/api";
import { readable, readonly, writable } from "svelte/store";
import type { Conversation, Message } from "wave-types";

export const conversations = readable<Conversation[]>([{ id: 123456, name: "Test" }]);

export function connectConversation(id: number) {
    let conversation: Conversation | undefined;
    conversations.subscribe((c) => (conversation = c.find((c) => (c.id = id))))();
    const chat = api.chat[id].subscribe();

    const messages = writable<Message[]>([]);

    chat.subscribe(({ data }) => {
        messages.update((prev) => {
            prev.push(data);
            return prev;
        });
    });

    function sendMessage(message: string) {
        chat.send({
            id: 0,
            sender: "svelte",
            time_sent: 0,
            modified: false,
            type: "text",
            data: message,
        });
    }

    return {
        conversation: conversation,
        messages: readonly(messages),
        send: sendMessage,
    };
}
