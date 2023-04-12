use chrono::{DateTime, Utc};
use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct Message {
    pub id: i32,
    pub content: String,
    pub timestamp: DateTime<Utc>,
}
