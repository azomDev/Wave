export interface Conversation {
    name: string;
    id: number;
}

export type MessageType = "text";

export interface Message {
    id: number;
    sender: string;

    time_sent: number;
    sent: boolean;
    modified: boolean;

    type: MessageType;
    message: string;
}

export const is_conversation = (data: any): data is Conversation => {
    return typeof data === "object" && typeof data.name === "string" && typeof data.id === "number";
};

export const is_message = (data: any): data is Message => {
    return typeof data === "object" && typeof data.id === "number" && typeof data.text === "string";
};
