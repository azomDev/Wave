import { edenTreaty } from "@elysiajs/eden";
import { writable } from "svelte/store";
import type { Conversation, Message } from "ui";
import { is_conversation } from "ui/types/types";
import type { App } from "wave-backend";

const api = edenTreaty<App>("http://localhost:3000");

export const chat = api.chat["123456"].subscribe();

export const _messages = writable<Message[]>([]);

export const _conversations = writable<Conversation[]>([]);

chat.subscribe((message) => {
    // Will change later
    if (is_conversation(message.data)) {
        add_conversation(message.data);
    } else {
        add_message(message.data as string);
    }
});

function add_conversation(newConversation: Conversation) {
    _conversations.update((prevConversations) => [...prevConversations, newConversation]);
}

function add_message(text: string) {
    _messages.update((prevMessages) => [...prevMessages, { id: new Date().getTime(), text }]);
}

export function send_message_server(text: string) {
    chat.send({ username: "svelte", message: text });
    add_message(text);
}
