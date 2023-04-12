use rusqlite::{Connection, Result};
use crate::models::message::Message;
use chrono::Utc;

pub fn create_database() -> Result<Connection> {
    let conn = Connection::open("messaging_app.db")?;

    conn.execute(
        "CREATE TABLE IF NOT EXISTS messages (
            id INTEGER PRIMARY KEY,
            content TEXT NOT NULL,
            timestamp TEXT NOT NULL
        )",
        [],
    )?;

    Ok(conn)
}

pub fn create_message(conn: &Connection, content: &str) -> Result<()> {
    let timestamp = Utc::now();

    conn.execute(
        "INSERT INTO messages (content, timestamp) VALUES (?1, ?2)",
        &[content, &timestamp.to_rfc3339()],
    )?;

    Ok(())
}

pub fn get_all_messages(conn: &Connection) -> Result<Vec<Message>> {
    let mut stmt = conn.prepare("SELECT id, content, timestamp FROM messages")?;
    let rows = stmt.query_map([], |row| {
        Ok(Message {
            id: row.get(0)?,
            content: row.get(1)?,
            timestamp: row.get(2).map(|ts: String| ts.parse().unwrap())?,
        })
    })?;

    let mut messages = Vec::new();
    for message_result in rows {
        messages.push(message_result?);
    }

    Ok(messages)
}
