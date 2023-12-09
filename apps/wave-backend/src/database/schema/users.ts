import { blob, integer, sqliteTable, text } from "drizzle-orm/sqlite-core";

export const usersTable = sqliteTable("users", {
    id: text("id").primaryKey().notNull(),
    displayName: text("display_name").notNull(),
    inscriptionDate: integer("inscription_date").notNull(),
    kyberPub: blob("kyber_public_key").notNull(),
    kyberPriv: blob("kyber_private_key").notNull(),
    masterKey: blob("master_key").notNull(),
});
