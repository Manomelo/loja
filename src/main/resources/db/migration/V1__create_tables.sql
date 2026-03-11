-- Categories
CREATE TABLE categories (
                            id          BIGSERIAL PRIMARY KEY,
                            name        VARCHAR(100) NOT NULL UNIQUE,
                            description VARCHAR(255)
);

-- Users
CREATE TABLE users (
                       id       BIGSERIAL PRIMARY KEY,
                       name     VARCHAR(150) NOT NULL,
                       email    VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role     VARCHAR(20)  NOT NULL DEFAULT 'CLIENTE',
                       active   BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Products
CREATE TABLE products (
                          id          BIGSERIAL PRIMARY KEY,
                          name        VARCHAR(150) NOT NULL,
                          description VARCHAR(500),
                          price       NUMERIC(10, 2) NOT NULL,
                          stock       INTEGER        NOT NULL,
                          image_url   VARCHAR(255),
                          active      BOOLEAN        NOT NULL DEFAULT TRUE,
                          category_id BIGINT         NOT NULL,
                          CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

-- Carts
CREATE TABLE carts (
                       id      BIGSERIAL PRIMARY KEY,
                       user_id BIGINT NOT NULL UNIQUE,
                       CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Cart Items
CREATE TABLE cart_items (
                            id         BIGSERIAL PRIMARY KEY,
                            cart_id    BIGINT  NOT NULL,
                            product_id BIGINT  NOT NULL,
                            quantity   INTEGER NOT NULL,
                            CONSTRAINT fk_cart_item_cart    FOREIGN KEY (cart_id)    REFERENCES carts (id),
                            CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES products (id)
);

-- Orders
CREATE TABLE orders (
                        id         BIGSERIAL PRIMARY KEY,
                        user_id    BIGINT         NOT NULL,
                        status     VARCHAR(20)    NOT NULL DEFAULT 'PENDENTE',
                        total      NUMERIC(10, 2) NOT NULL,
                        created_at TIMESTAMP      NOT NULL DEFAULT NOW(),
                        CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Order Items
CREATE TABLE order_items (
                             id                BIGSERIAL PRIMARY KEY,
                             order_id          BIGINT         NOT NULL,
                             product_id        BIGINT         NOT NULL,
                             quantity          INTEGER        NOT NULL,
                             price_at_purchase NUMERIC(10, 2) NOT NULL,
                             CONSTRAINT fk_order_item_order   FOREIGN KEY (order_id)   REFERENCES orders (id),
                             CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES products (id)
);