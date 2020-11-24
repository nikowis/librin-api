INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username, status, avg_rating, rating_count)
VALUES (1, now(), 'USER', 'testuser@email.com', '$2y$10$eMVYhCacsns3AqEpMR06l./EfTC5kLyMRZlJJpUJMTuGvMsANDUzi', 'Test', 'User', 'testuser', 'ACTIVE', 0, 0);
INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username, status, avg_rating, rating_count)
VALUES (2, now(), 'USER', 'testuser2@email.com', '$2y$10$eMVYhCacsns3AqEpMR06l./EfTC5kLyMRZlJJpUJMTuGvMsANDUzi', 'Test', 'User2', 'testuser2', 'INACTIVE', 0, 0);
INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username, status, avg_rating, rating_count)
VALUES (3, now(), 'USER', 'testuser3@email.com', '$2y$10$eMVYhCacsns3AqEpMR06l./EfTC5kLyMRZlJJpUJMTuGvMsANDUzi', 'Test', 'User3', 'testuser3', 'BLOCKED', 0, 0);

INSERT INTO offer (id, created_at, status, owner_id, buyer_id, category, condition, price, title, author)
VALUES (1, now(), 'SOLD', 1, 2, 'BIOGRAPHIES', 3, '12.50', 'Harry Potter', 'J. K. Rowling');


INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'TERM_AND_CONDITIONS', now());
INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'PRIVACY_POLICY', now());
