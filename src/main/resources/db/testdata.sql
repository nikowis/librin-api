INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username)
VALUES (1, now(), 'USER', 'testuser@email.com', 'pswd', 'Test', 'User', 'testuser');
INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username)
VALUES (2, now(), 'USER', 'testuser2@email.com', 'pswd', 'Test', 'User2', 'testuser2');
INSERT INTO "user" (id, created_at, role, email, password, first_name, last_name, username)
VALUES (3, now(), 'USER', 'testuser3@email.com', 'pswd', 'Test', 'User3', 'testuser3');

INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'TERM_AND_CONDITIONS', now());
INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'PRIVACY_POLICY', now());
