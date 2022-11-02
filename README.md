# bug-free-octo-system

# Create db
```sql
CREATE DATABASE test;
CREATE TABLE test.users (
	id INT AUTO_INCREMENT PRIMARY KEY,
    email TEXT NOT NULL UNIQUE
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
    reserved_by VARCHAR(255) NOT NULL,
    wishlist_id INT,
    FOREIGN KEY (wishlist_id) REFERENCES wishlists(id)
);

CREATE TABLE test.wishlist_shares (
	id INT AUTO_INCREMENT,
	uuid VARCHAR(36) DEFAULT (UUID()) ,
    wishlist_id INT UNIQUE,
    PRIMARY KEY (id),
    FOREIGN KEY (wishlist_id) REFERENCES wishlists(id)
);
```