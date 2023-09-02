ALTER TABLE td_book ADD CONSTRAINT uc_book_title UNIQUE (title);

ALTER TABLE td_book ADD COLUMN file_key VARCHAR(250);