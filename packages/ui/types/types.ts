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

export const is_conversation = (data: any): data is Conversation => {
    return typeof data === "object" && typeof data.name === "string" && typeof data.id === "number";
};

export const is_message = (data: any): data is Message => {
    return typeof data === "object" && typeof data.id === "number" && typeof data.text === "string";
};
