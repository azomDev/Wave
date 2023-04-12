use crate::db::db_operations::{create_database, create_message, get_all_messages};
use actix_web::{get, post, web, HttpResponse, Responder};
use serde::Deserialize;
use serde_json::json;

#[derive(Debug, Deserialize)]
pub struct MessageContent {
    content: String,
}

#[post("/messages/create")]
pub async fn create_message_handler(message_content: web::Json<MessageContent>) -> impl Responder {

    let conn = match create_database() {
        Ok(conn) => conn,
        Err(_) => {
            return HttpResponse::InternalServerError()
                .json(json!({"error": "Failed to connect to the database"}))
        }
    };

    match create_message(&conn, &message_content.content) {
        Ok(_) => HttpResponse::Created().json(json!({"status": "Message created"})),
        Err(_) => HttpResponse::InternalServerError()
            .json(json!({"error": "Failed to create the message"})),
    }
}

#[get("/messages")]
pub async fn get_all_messages_handler() -> impl Responder {
    let conn = match create_database() {
        Ok(conn) => conn,
        Err(_) => {
            return HttpResponse::InternalServerError()
                .json(json!({"error": "Failed to connect to the database"}))
        }
    };

    match get_all_messages(&conn) {
        Ok(messages) => HttpResponse::Ok().json(messages),
        Err(_) => {
            HttpResponse::InternalServerError().json(json!({"error": "Failed to fetch messages"}))
        }
    }
}
