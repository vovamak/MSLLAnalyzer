CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         product_code VARCHAR(50) NOT NULL UNIQUE,
                         description VARCHAR(500),
                         category VARCHAR(50)
);
CREATE TABLE part_number (
                             id BIGSERIAL PRIMARY KEY,
                             product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
                             part_number VARCHAR(100) NOT NULL,
                             measure_unit VARCHAR(20),
                             priority INTEGER DEFAULT 1
);
CREATE TABLE product_lists (
                               product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
                               list_name VARCHAR(50) NOT NULL,
                               PRIMARY KEY (product_id, list_name)
);

CREATE INDEX idx_product_part_number ON part_number(part_number);
CREATE INDEX idx_product_lists ON product_lists(list_name);
CREATE INDEX idx_product_code ON product(product_code);