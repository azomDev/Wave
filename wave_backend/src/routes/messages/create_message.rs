use actix_web::{web, HttpResponse, Responder}; // Import the actix_web crate and its types and traits
use chrono::Utc; // Import the chrono crate and its Utc type
use crate::db::models::Message; // Import the Message struct from the db/models module
use crate::db::DB_INSTANCE; // Import the global DB_INSTANCE variable from the db module

pub async fn create_message(new_message: web::Json<NewMessage>) -> impl Responder { // Define an async function called "create_message" that handles a POST request to create a new message
    let timestamp = Utc::now().to_rfc3339(); // Get the current timestamp in RFC 3339 format using the chrono library
    let content = new_message.content.clone(); // Get the content of the new message from the request body

    let connection = DB_INSTANCE.connection.lock().unwrap(); // Acquire a lock on the Mutex-protected Connection to the SQLite database

    let query = "
        INSERT INTO messages (content, timestamp)
        VALUES (?1, ?2)
        RETURNING id, content, timestamp;
    "; // Define an SQL query to insert a new message into the messages table and return its id, content, and timestamp

    match connection.query_row(query, &[&content, &timestamp], |row| { // Execute the SQL query using the SQLite Connection
        let id: i32 = row.get(0)?; // Get the id of the new message from the query result
        let content: String = row.get(1)?; // Get the content of the new message from the query result
        let timestamp: String = row.get(2)?; // Get the timestamp of the new message from the query result
        Ok(Message::new(id, content, timestamp)) // Create a new Message instance from the id, content, and timestamp, and return it as a Result
    }) {
        Ok(message) => HttpResponse::Created().json(message), // If the query succeeds, return an HTTP 201 Created response with the new message as JSON
        Err(_) => HttpResponse::InternalServerError().finish(), // If the query fails, return an HTTP 500 Internal Server Error response
    }
}

#[derive(Deserialize)]
pub struct NewMessage { // Define a struct called "NewMessage" that holds data for a new message
    pub content: String, // A content field of type String that holds the content of the new message
}
