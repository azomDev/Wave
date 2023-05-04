use std::env; // Import the std::env module for accessing environment variables

pub struct AppConfig { // Define a struct called "AppConfig" to hold the application's configuration settings
    pub server_host: String, // The IP address or hostname of the server to listen on
    pub server_port: u16, // The port number to listen on
    pub database_url: String, // The URL of the database to connect to
    pub redis_url: String, // The URL of the Redis server to connect to
}

impl AppConfig { // Implement the AppConfig struct's methods
    pub fn from_env() -> Self { // Define a static method called "from_env" that creates a new AppConfig instance from environment variables
        let server_host = env::var("SERVER_HOST").unwrap_or_else(|_| "127.0.0.1".to_string()); // Get the value of the SERVER_HOST environment variable, or use the default value "127.0.0.1" if it is not set
        let server_port: u16 = env::var("SERVER_PORT") // Get the value of the SERVER_PORT environment variable, or use the default value "8080" if it is not set
            .unwrap_or_else(|_| "8080".to_string())
            .parse()
            .expect("Invalid SERVER_PORT value");
        let database_url = env::var("DATABASE_URL").expect("DATABASE_URL environment variable is not set"); // Get the value of the DATABASE_URL environment variable, or panic if it is not set
        let redis_url = env::var("REDIS_URL").expect("REDIS_URL environment variable is not set"); // Get the value of the REDIS_URL environment variable, or panic if it is not set

        Self { // Create a new AppConfig instance with the retrieved configuration settings
            server_host,
            server_port,
            database_url,
            redis_url,
        }
    }
}
