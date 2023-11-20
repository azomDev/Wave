import { edenTreaty } from "@elysiajs/eden";
import { writable } from "svelte/store";
import type { Conversation, Message } from "ui";
import { isConversation } from "ui/types/types";
import type { App } from "wave-backend";

const api = edenTreaty<App>("http://localhost:3000");

export const chat = api.chat["123456"].subscribe();

export const _messages = writable<Message[]>([]);

export const _conversations = writable<Conversation[]>([]);

chat.subscribe((message) => {
    if (isConversation(message.data)) {
        addConversation(message.data);
    } else {
        addMessage(message.data as string);
    }
});

function addConversation(newConversation: Conversation) {
    _conversations.update((prevConversations) => [...prevConversations, newConversation]);
}

function addMessage(text: string) {
    _messages.update((prevMessages) => [...prevMessages, { id: new Date().getTime(), text }]);
}

export function sendMessage(text: string) {
    chat.send({ username: "svelte", message: text });
    addMessage(text);
}
