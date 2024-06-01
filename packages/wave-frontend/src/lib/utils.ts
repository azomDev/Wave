import type { ClassValue } from "clsx";
import clsx from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...classLists: ClassValue[]) {
    return twMerge(clsx(classLists));
}

type R<T extends Record<string, unknown>> = {
    [K in keyof T]: T[K] extends Uint8Array ? string : T[K] extends Record<string, unknown> ? R<T[K]> : T[K];
};
export function atosDeep<T extends Record<string, unknown>>(obj: T): R<T> {
    return Object.fromEntries(
        Object.entries(obj).map<[keyof T, unknown]>(([k, v]) => {
            if (!v || typeof v !== "object") return [k, v];
            if (v && v instanceof Uint8Array) return [k, bufferToBase64(v)];
            return [k, atosDeep(v as Record<string, unknown>)];
        }),
    ) as R<T>;
}

export function base64ToBuffer(string: string) {
    const binString = atob(string);
    return Uint8Array.from(binString, (m) => m.codePointAt(0) ?? 0);
}

export function bufferToBase64(array: Uint8Array) {
    const binString = Array.from(array, (byte) => String.fromCodePoint(byte)).join("");
    return btoa(binString);
}
