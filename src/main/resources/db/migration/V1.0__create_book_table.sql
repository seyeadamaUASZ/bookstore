
CREATE TABLE td_book
(
    id         BIGINT                  NOT NULL AUTO_INCREMENT,
    title       VARCHAR(100)           NOT NULL,
    isbn        VARCHAR(100)           NOT NULL,
    description VARCHAR(250)           NULL,
    name        VARCHAR(50)            NOT NULL,
    contacts    VARCHAR(100)           NOT NULL,
    resume      VARCHAR(100)           NOT NULL,
    file_name   VARCHAR(100)           NULL,
    filebook    VARCHAR(100)           NULL,
    book_type   VARCHAR(100)           NOT NULL,
    CONSTRAINT pk_book PRIMARY KEY (id)
);

