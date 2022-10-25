# bug-free-octo-system

# Create db
```sql
CREATE DATABASE test;

CREATE TABLE test.users (
	id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE test.wishlists (
	id INT AUTO_INCREMENT PRIMARY KEY,
    title TEXT NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE test.wishes (
	id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    wishlist_id INT,
    FOREIGN KEY (wishlist_id) REFERENCES wishlists(id)
);
```