-- Lozinke su hesovane pomocu BCrypt algoritma https://www.dailycred.com/article/bcrypt-calculator
-- Lozinka za oba user-a je 123



INSERT INTO USERS (username, password, first_name, last_name, email, address, enabled, last_password_reset_date, number_of_following, number_of_posts) VALUES ('user', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Marko', 'Markovic', 'user@example.com', 'Trg Kralja 2', true, '2017-10-01 21:58:58.508-07', 0, 0);
INSERT INTO USERS (username, password, first_name, last_name, email, address, enabled, last_password_reset_date, number_of_following, number_of_posts) VALUES ('admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'admin@example.com', 'Titogradska 12', true, '2017-10-01 18:57:58.508-07', 0, 0);

INSERT INTO LOCATIONS (longitude, latitude) VALUES (120, 120);
INSERT INTO LOCATIONS (longitude, latitude) VALUES (90, 89);

INSERT INTO POSTS (description, folder_path, likes, num_of_comments, post_date, creator_id, location_id,is_Deleted,is_Restricted) VALUES ('A beautiful sunset over the mountains', 'uploads/user/post_0', 0, 4, '2024-11-12 11:00:00', 1, 1, false,false);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-12 11:00:00', 'This sunset is stunning!', 1, 1);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-12 11:05:00', 'Amazing view! Where is this?', 1, 1);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-12 11:05:00', 'Such a cute little potato!', 1, 1);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-12 11:05:00', 'Rabbit just pissed on me', 1, 1);




INSERT INTO FOLLOWING (follower_id,following_id) VALUES  (2,1);


INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_ZEC');

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 1); -- admin-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ADMIN
