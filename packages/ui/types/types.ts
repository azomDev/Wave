export type Conversation = {
    name: string;
    id: number;
};

export type Message = {
    // sender: string;
    // sent_time_stamp: string;
    id: number;
    text: string;
};

// Type checker for Conversation
export const isConversation = (data: any): data is Conversation => {
    return typeof data === "object" && typeof data.name === "string" && typeof data.id === "number";
};

// Type checker for Message
export const isMessage = (data: any): data is Message => {
    return typeof data === "object" && typeof data.id === "number" && typeof data.text === "string";
};
