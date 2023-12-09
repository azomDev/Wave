import { blob, integer, sqliteTable, text } from "drizzle-orm/sqlite-core";

export const conversationsTable = sqliteTable("conversations", {
    id: integer("id").primaryKey(),
    title: text("name"),
    creationDate: integer("creation_date"),
});

export const conversationsUsersTable = sqliteTable("conversations", {
    id: integer("id").primaryKey(),
    conversation: integer("conversation"),
    user: text("user"),
    joinedDate: integer("joined_date"),
    key: blob("key"),
});
