INSERT INTO category(category_name) VALUES ('Fashion');
INSERT INTO category(category_name) VALUES ('Electronics');
INSERT INTO category(category_name) VALUES ('Books');
INSERT INTO category(category_name) VALUES ('Groceries');
INSERT INTO category(category_name) VALUES ('Medicines');

INSERT INTO role(role) VALUES ('CONSUMER');
INSERT INTO role(role) VALUES ('SELLER');

INSERT INTO user_details(username, password, user_enabled) VALUES ('jack', 'pass_word', true);
INSERT INTO user_details(username, password, user_enabled) VALUES ('bob', 'pass_word', true);
INSERT INTO user_details(username, password, user_enabled) VALUES ('apple', 'pass_word', true);
INSERT INTO user_details(username, password, user_enabled) VALUES ('glaxo', 'pass_word', true);

INSERT INTO user_details_roles(user_details_user_id, roles_role_id) VALUES (1, 1);
INSERT INTO user_details_roles(user_details_user_id, roles_role_id) VALUES (2, 1);
INSERT INTO user_details_roles(user_details_user_id, roles_role_id) VALUES (3, 2);
INSERT INTO user_details_roles(user_details_user_id, roles_role_id) VALUES (4, 2);

INSERT INTO cart(total_amount, user_user_id) VALUES (20, 1);
INSERT INTO cart(total_amount, user_user_id) VALUES (0, 2);

INSERT INTO product(price, product_name, category_id, seller_id) VALUES (29190, 'Apple iPad 10.2 8th Gen WiFi iOS Tablet', 2, 3);
INSERT INTO product(price, product_name, category_id, seller_id) VALUES (10, 'Crocin pain relief tablet', 5, 4);

INSERT INTO cart_product(quantity, cart_id, product_id) VALUES (2, 1, 2);

