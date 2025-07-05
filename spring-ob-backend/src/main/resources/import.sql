-- Lozinke su hesovane pomocu BCrypt algoritma https://www.dailycred.com/article/bcrypt-calculator
-- Lozinka za oba user-a je 123



INSERT INTO USERS (username, password, first_name, last_name, email, address, enabled, last_password_reset_date, number_of_following, number_of_followers, number_of_posts, last_login_date) VALUES ('user', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Marko', 'Markovic', 'user@example.com', 'Despota Djurdja 21 Smederevo', true, '2024-11-12 11:00:00', 3, 1, 0, '2024-11-12 11:00:00');
INSERT INTO USERS (username, password, first_name, last_name, email, address, enabled, last_password_reset_date, number_of_following, number_of_followers, number_of_posts, last_login_date) VALUES ('admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'admin@example.com', 'Titogradska 12', true, '2024-11-12 11:00:00', 0, 1, 0, '2024-11-12 11:00:00');
INSERT INTO USERS (username, password, first_name, last_name, email, address, enabled, last_password_reset_date, number_of_following, number_of_followers, number_of_posts, last_login_date) VALUES ('m', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'm', 'm', 'm@example.com', 'Titogradska 12', true, '2024-11-12 11:00:00', 2, 2, 0, '2024-11-12 11:00:00');
INSERT INTO USERS (username, password, first_name, last_name, email, address, enabled, last_password_reset_date, number_of_following, number_of_followers, number_of_posts, last_login_date) VALUES ('t', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 't', 't', 't@example.com', 'Titogradska 12', true, '2024-11-12 11:00:00', 1, 2, 0, '2024-11-12 11:00:00');


INSERT INTO LOCATIONS (longitude, latitude) VALUES (44.664276, 20.925287);
INSERT INTO LOCATIONS (longitude, latitude) VALUES (44.666276, 20.921787);




INSERT INTO POSTS (description, folder_path, likes, num_of_comments, post_date, creator_id, location_id,is_Deleted,is_Restricted,version) VALUES ('A beautiful sunset over the mountains', 'uploads/user/post_0', 2, 4, '2025-01-3 11:00:00', 1, 1, false,false,0);


INSERT INTO POSTS (description, folder_path, likes, num_of_comments, post_date, creator_id, location_id,is_Deleted,is_Restricted,version) VALUES ('This little bunny is so soft and fluffy.', 'uploads/user/post_1', 1, 3, '2024-11-15 10:00:00', 2, 2, false,false,0);




INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-12 11:00:00', 'This sunset is stunning!', 2, 1);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2010-11-12 11:05:00', 'Amazing view! Where is this?', 2, 1);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-12 11:05:00', 'Such a cute little potato!', 1, 2);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-12 11:05:00', 'Rabbit just pissed on me', 1, 1);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-13 12:05:00', 'That thing is so cute.', 1, 1);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-14 10:05:00', 'I like a good rabbit soup!', 1, 1);

INSERT INTO COMMENTS (created, text, creator_id, post_id) VALUES ('2024-11-16 11:05:00', 'You are disgusting sir!', 1, 1);



INSERT INTO FOLLOWING (follower_id,following_id) VALUES  (1,2);
INSERT INTO FOLLOWING (follower_id,following_id) VALUES  (1,3);
INSERT INTO FOLLOWING (follower_id,following_id) VALUES  (1,4);
INSERT INTO FOLLOWING (follower_id,following_id) VALUES  (3,1);
INSERT INTO FOLLOWING (follower_id,following_id) VALUES  (3,4);
INSERT INTO FOLLOWING (follower_id,following_id) VALUES  (4,3);


INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_ZEC');




INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ADMIN


INSERT INTO POST_USER_LIKES (like_date, post_id, user_id) VALUES ('2025-01-03 11:55:00', 2, 1);
INSERT INTO POST_USER_LIKES (like_date, post_id, user_id) VALUES ('2025-01-03 11:55:00', 1, 1);
INSERT INTO POST_USER_LIKES (like_date, post_id, user_id) VALUES ('2025-01-04 11:55:00', 1, 2);

INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (4, 1);

