use serde::{Deserialize, Serialize}; // Import the serde crate and its Deserialize and Serialize traits

#[derive(Debug, Serialize, Deserialize)] // Derive the Debug, Serialize, and Deserialize traits for the Message struct
pub struct Message { // Define a struct called "Message" that holds data for a message
    pub id: i32, // An id field of type i32 that holds the ID of the message
    pub content: String, // A content field of type String that holds the content of the message
    pub timestamp: String, // A timestamp field of type String that holds the timestamp of the message
}

impl Message {
    pub fn new(id: i32, content: String, timestamp: String) -> Self { // Define a new() function to create a new Message instance
        Message { id, content, timestamp } // Return a new Message instance with the given id, content, and timestamp
    }
}
