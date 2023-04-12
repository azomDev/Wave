use actix_web::web; // Import the actix_web crate and its web module

mod messages; // Import the messages module and its contents
pub use messages::{create_message, get_messages}; // Re-export the create_message and get_messages functions from the messages module

pub fn config(cfg: &mut web::ServiceConfig) { // Define a function called "config" that configures the HTTP service
    cfg.service(web::scope("/messages").configure(messages::config)); // Create a scope for handling requests related to messages and configure it with the messages::config function
}
