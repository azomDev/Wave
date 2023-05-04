use redis::{Client, Commands, Connection, RedisResult}; // Import the redis crate and its types and traits

pub struct RedisHelper { // Define a struct called "RedisHelper" that holds a Redis Connection
    connection: Connection,
}

impl RedisHelper {
    pub fn new(redis_url: &str) -> RedisResult<Self> { // Define a new() function that creates a new RedisHelper instance
        let client = Client::open(redis_url)?; // Create a new Redis client using the given URL
        let connection = client.get_connection()?; // Connect to the Redis server using the client
        Ok(Self { connection }) // Return a Result that contains the newly created RedisHelper instance
    }

    pub fn set(&mut self, key: &str, value: &str) -> RedisResult<()> { // Define a set() function to set a Redis key-value pair
        self.connection.set(key, value)?; // Use the Redis Connection to set the key-value pair
        Ok(()) // Return a Result that indicates success
    }

    pub fn get(&mut self, key: &str) -> RedisResult<Option<String>> { // Define a get() function to get the value of a Redis key
        self.connection.get(key) // Use the Redis Connection to get the value of the key
    }

    // Add more Redis helper functions as needed.
}

lazy_static! { // Define a lazily initialized static variable (i.e., it is initialized on first use)
    pub static ref REDIS_HELPER_INSTANCE: RedisHelper = { // Define a static variable called "REDIS_HELPER_INSTANCE" that holds a RedisHelper instance
        let redis_url = "redis://127.0.0.1/"; // Define the URL of the Redis server
        let redis_helper = RedisHelper::new(redis_url).expect("Failed to create Redis helper instance"); // Create a new RedisHelper instance using the URL, or panic with an error message if creation fails
        redis_helper // Return the initialized RedisHelper instance
    };
}
