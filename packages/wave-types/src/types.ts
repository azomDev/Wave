export interface Conversation {
    name: string;
    id: number;
}

export type MessageType = "text";

export interface Message {
    id: number | null;
    sender: string;

    time_sent: number;
    sent: boolean;
    modified: boolean;

    type: MessageType;
    message: string;
}

export const isConversation = (data: unknown): data is Conversation => {
    return (
        !!data &&
        typeof data === "object" &&
        "name" in data &&
        typeof data.name === "string" &&
        "id" in data &&
        typeof data.id === "number"
    );
};

export const isMessage = (data: unknown): data is Message => {
    return (
        !!data &&
        typeof data === "object" &&
        "id" in data &&
        typeof data.id === "number" &&
        "text" in data &&
        typeof data.text === "string"
    );
};
