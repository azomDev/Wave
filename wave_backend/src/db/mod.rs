use rusqlite::{Connection, Result}; // Import the rusqlite crate and its Result type
use std::sync::Mutex; // Import the standard library's Mutex type

pub mod models; // Import a module called "models"

pub struct Database {
    connection: Mutex<Connection>, // Define a struct that wraps a Mutex-protected SQLite Connection
}

impl Database {
    pub fn new(database_url: &str) -> Result<Self> { // Define a new() function that creates a new Database instance
        let connection = Connection::open(database_url)?; // Open a SQLite Connection using the given URL
        Ok(Self { // Return a Result that contains the newly created Database instance
            connection: Mutex::new(connection), // Wrap the Connection in a Mutex and store it in the Database struct
        })
    }

    pub fn initialize(&self) -> Result<()> { // Define an initialize() function that sets up the database schema
        let create_messages_table = "CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY, content TEXT NOT NULL, timestamp DATETIME NOT NULL);"; // Define a SQL statement to create a "messages" table if it does not already exist

        let connection = self.connection.lock().unwrap(); // Acquire a lock on the Mutex-protected Connection
        connection.execute(create_messages_table, [])?; // Execute the SQL statement using the Connection
        Ok(()) // Return a Result that indicates success
    }
}

lazy_static! { // Define a lazily initialized static variable (i.e., it is initialized on first use)
    pub static ref DB_INSTANCE: Database = { // Define a static variable called "DB_INSTANCE" that holds a Database instance
        let database_url = "wave.db"; // Define the URL of the SQLite database
        let db = Database::new(database_url).expect("Failed to create database instance"); // Create a new Database instance using the URL, or panic with an error message if creation fails
        db.initialize().expect("Failed to initialize database"); // Call the initialize() function on the new Database instance, or panic with an error message if initialization fails
        db // Return the initialized Database instance
    };
}
