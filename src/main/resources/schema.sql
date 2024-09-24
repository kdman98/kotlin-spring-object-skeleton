CREATE TABLE IF NOT EXISTS users
(
    user_id     VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    user_name   VARCHAR(255) NOT NULL,
    id_type     VARCHAR(50)  NOT NULL,
    id_value    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS quotes (
    quote_id INT AUTO_INCREMENT,
    user_id VARCHAR(255) NOT NULL,
    requested_amount DECIMAL(21, 6) NOT NULL,
    transfer_fee DECIMAL(21, 6) NOT NULL,
    exchange_rate DECIMAL(21, 6) NOT NULL,
    usd_exchange_rate DECIMAL(21, 6) NOT NULL,
    expire_time TIMESTAMP NOT NULL,
    usd_amount DECIMAL(21, 6) NOT NULL,
    receiving_amount DECIMAL(21, 6) NOT NULL,
    currency VARCHAR(20) NOT NULL,
    is_processed BOOLEAN NOT NULL,
    PRIMARY KEY (quote_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


CREATE TABLE IF NOT EXISTS transfer_requests (
    transfer_id             INT AUTO_INCREMENT,
    user_id                 VARCHAR(255) NOT NULL,
    quote_id                INT,
    requested_datetime      TIMESTAMP NOT NULL,
    registered_datetime     TIMESTAMP NOT NULL,
    PRIMARY KEY (transfer_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (quote_id) REFERENCES quotes(quote_id)
);