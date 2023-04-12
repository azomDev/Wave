use crate::handlers::message_handler::{create_message_handler, get_all_messages_handler};
use actix_web::{middleware::Logger, App, HttpServer};

mod db;
mod handlers;
mod models;

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| {
        App::new()
            .wrap(Logger::default())
            .service(create_message_handler)
            .service(get_all_messages_handler)
    })
    .bind(("127.0.0.1", 8080))?
    .run()
    .await
}
