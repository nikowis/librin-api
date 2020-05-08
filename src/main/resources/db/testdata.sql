INSERT INTO "user" (id, created_at, role, email, password)
VALUES (1, now(), 'USER', 'testuser@email.com', 'pswd');
INSERT INTO "user" (id, created_at, role, email, password)
VALUES (2, now(), 'USER', 'testuser2@email.com', 'pswd');
INSERT INTO "user" (id, created_at, role, email, password)
VALUES (3, now(), 'USER', 'testuser3@email.com', 'pswd');

INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'TERM_AND_CONDITIONS', now());
INSERT  into  policy (version, file_name, type, created_at) values (1, 'empty.pdf', 'PRIVACY_POLICY', now());
