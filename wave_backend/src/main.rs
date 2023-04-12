use actix_web::{middleware::Logger, App, HttpServer}; // Import the actix_web crate and its types and traits
use env_logger::Env; // Import the env_logger crate and its Env struct for configuring logging
use std::io; // Import the std::io module for handling I/O operations

mod config; // Import the config module and its contents
mod db; // Import the db module and its contents
mod redis_helper; // Import the redis_helper module and its contents
mod routes; // Import the routes module and its contents

use crate::config::AppConfig; // Use the AppConfig struct from the config module
use crate::db::DB_INSTANCE; // Use the DB_INSTANCE object from the db module
use crate::redis_helper::REDIS_HELPER_INSTANCE; // Use the REDIS_HELPER_INSTANCE object from the redis_helper module

#[macro_use]
extern crate lazy_static; // Import the lazy_static crate and its macros

#[actix_web::main]
async fn main() -> io::Result<()> {
    // Define the main function that sets up and starts the HTTP server
    env_logger::Builder::from_env(Env::default().default_filter_or("info")).init(); // Configure the logger with the default filter "info"

    let config = AppConfig::from_env(); // Get the application configuration settings from environment variables

    let database_url = config.database_url.clone(); // Clone the database URL string
    let redis_url = config.redis_url.clone(); // Clone the Redis URL string

    DB_INSTANCE.initialize(&database_url); // Initialize the database connection
    *REDIS_HELPER_INSTANCE = redis_helper::RedisHelper::new(&redis_url).unwrap(); // Initialize the Redis connection

    HttpServer::new(|| {
        // Create a new HttpServer instance
        App::new()
            .wrap(Logger::default()) // Add the logger middleware to the App instance
            .configure(routes::config) // Configure the App instance with the routes defined in the routes module
    })
    .bind((config.server_host, config.server_port))? // Bind the HttpServer to the specified host and port
    .run() // Start the HttpServer
    .await // Wait for the HttpServer to stop
}
