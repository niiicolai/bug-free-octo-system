# bug-free-octo-system

# Create db
```sql
CREATE DATABASE test;
CREATE TABLE test.users (
	id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE test.wishlists (
	id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    user_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE test.wishes (
	id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    wishlist_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wishlist_id) REFERENCES wishlists(id)
);

CREATE TABLE test.wish_reservers (
    wish_id INT PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wish_id) REFERENCES wishes(id)
);

CREATE TABLE test.wishlist_shares (
	uuid VARCHAR(36) DEFAULT (UUID()) ,
    wishlist_id INT PRIMARY KEY,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wishlist_id) REFERENCES wishlists(id)
);
```