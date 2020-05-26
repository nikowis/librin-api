INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username, status)
VALUES (1, now(), 'USER', 'testuser@email.com', '$2y$10$eMVYhCacsns3AqEpMR06l./EfTC5kLyMRZlJJpUJMTuGvMsANDUzi', 'Test', 'User', 'testuser', 'ACTIVE');
INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username, status)
VALUES (2, now(), 'USER', 'testuser2@email.com', '$2y$10$eMVYhCacsns3AqEpMR06l./EfTC5kLyMRZlJJpUJMTuGvMsANDUzi', 'Test', 'User2', 'testuser2', 'INACTIVE');
INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username, status)
VALUES (3, now(), 'USER', 'testuser3@email.com', '$2y$10$eMVYhCacsns3AqEpMR06l./EfTC5kLyMRZlJJpUJMTuGvMsANDUzi', 'Test', 'User3', 'testuser3', 'BLOCKED');

INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'TERM_AND_CONDITIONS', now());
INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'PRIVACY_POLICY', now());
