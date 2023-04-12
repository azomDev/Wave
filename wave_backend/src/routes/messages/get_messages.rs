use crate::db::models::Message; // Import the Message struct from the db/models module
use crate::db::DB_INSTANCE; // Import the global DB_INSTANCE variable from the db module
use actix_web::{HttpResponse, Responder}; // Import the actix_web crate and its types and traits

pub async fn get_messages() -> impl Responder { // Define an async function called "get_messages" that handles a GET request to retrieve a list of all messages in the system
    let connection = DB_INSTANCE.connection.lock().unwrap(); // Acquire a lock on the Mutex-protected Connection to the SQLite database

    let query = "SELECT id, content, timestamp FROM messages ORDER BY timestamp DESC;"; // Define an SQL query to select all messages from the messages table and order them by timestamp in descending order

    let messages: Vec<Message> = match connection.prepare(query) { // Prepare the SQL query using the SQLite Connection
        Ok(mut statement) => { // If the query preparation succeeds, execute the query and return a vector of Message instances
            let mut messages = Vec::new();
            while let Ok(Some(row)) = statement.next() { // Iterate over the query result rows and create a Message instance for each row
                let id: i32 = row.get(0).unwrap(); // Get the id of the message from the query result
                let content: String = row.get(1).unwrap(); // Get the content of the message from the query result
                let timestamp: String = row.get(2).unwrap(); // Get the timestamp of the message from the query result
                let message = Message::new(id, content, timestamp); // Create a new Message instance from the id, content, and timestamp
                messages.push(message); // Add the new Message instance to the messages vector
            }
            messages // Return the messages vector as a Result
        }
        Err(_) => return HttpResponse::InternalServerError().finish(), // If the query preparation fails, return an HTTP 500 Internal Server Error response
    };

    HttpResponse::Ok().json(messages) // Return an HTTP 200 OK response with the messages vector as JSON
}
