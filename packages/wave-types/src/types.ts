export interface Conversation {
    name: string;
    id: number;
}

export type MessageType = "text";

export interface Message {
    id: number;
    sender: string;

    time_sent: number;
    modified: boolean;

    type: MessageType;
    data: string;
}

export const is_conversation = (data: any): data is Conversation => {
    return typeof data === "object" && typeof data.name === "string" && typeof data.id === "number";
};

export const is_message = (data: any): data is Message => {
    return typeof data === "object" && typeof data.id === "number" && typeof data.text === "string";
};
