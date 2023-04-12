use actix_web::{web, HttpResponse, Responder}; // Import the actix_web crate and its types and traits
use crate::db::models::Message; // Import the Message struct from the db/models module

pub fn config(cfg: &mut web::ServiceConfig) { // Define a function called "config" that configures the HTTP service
    cfg.service( // Define a new HTTP service with two routes: one for GET requests to retrieve messages, and one for POST requests to create messages
        web::resource("/")
            .route(web::get().to(get_messages)) // Use the get_messages() function to handle GET requests to the root URL
            .route(web::post().to(create_message)), // Use the create_message() function to handle POST requests to the root URL
    );
}

pub async fn get_messages() -> impl Responder { // Define an async function called "get_messages" that retrieves a list of messages
    // TODO Implement the logic to retrieve messages from the database
    let messages = vec![Message::new(1, "Sample message".into(), "2023-04-11T12:34:56Z".into())]; // Create a sample message
    HttpResponse::Ok().json(messages) // Return an HTTP 200 OK response with the list of messages as JSON
}

pub async fn create_message(new_message: web::Json<Message>) -> impl Responder { // Define an async function called "create_message" that creates a new message
    // TODO Implement the logic to store the new message in the database
    let message = new_message.into_inner(); // Get the new message from the request body
    HttpResponse::Created().json(message) // Return an HTTP 201 Created response with the new message as JSON
}
