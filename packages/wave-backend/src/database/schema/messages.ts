import { sql } from "drizzle-orm";
import { blob, integer, sqliteTable, text } from "drizzle-orm/sqlite-core";

export const messagesTable = sqliteTable("messages", {
    id: integer("id").primaryKey(),
    conversation: integer("conversation"),
    sender: text("sender"),
    timestamp: integer("timestamp").default(sql`CURRENT_TIMESTAMP`),
    metadata: blob("meta"),
    type: text("type", { enum: ["text"] }),
    message: text("message", { mode: "json" }),
});
