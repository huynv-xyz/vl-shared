-- ========================
-- WAREHOUSES
-- ========================
CREATE TABLE warehouses (
    id INT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255) NOT NULL,
    address TEXT,

    status VARCHAR(20) DEFAULT 'ACTIVE',

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- ========================
-- COMPANIES
-- ========================
CREATE TABLE companies (
    id INT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255) NOT NULL,
    address TEXT,

    tax_code VARCHAR(50),

    status VARCHAR(20) DEFAULT 'ACTIVE',

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- ========================
-- DELIVERIES
-- ========================
CREATE TABLE deliveries (
    id INT AUTO_INCREMENT PRIMARY KEY,

    order_id INT NOT NULL,

    delivery_no VARCHAR(50) NOT NULL,
    delivery_date DATE,

    warehouse_id INT,
    company_id INT,

    delivery_address TEXT,

    status VARCHAR(20) DEFAULT 'NEW',
    note TEXT,

    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- ========================
-- DELIVERY ITEMS
-- ========================
CREATE TABLE delivery_items (
    id INT AUTO_INCREMENT PRIMARY KEY,

    delivery_id INT NOT NULL,
    product_id INT NOT NULL,

    quantity DECIMAL(18,2) NOT NULL,

    note TEXT
);

-- ========================
-- FOREIGN KEY (optional)
-- ========================
ALTER TABLE deliveries
ADD CONSTRAINT fk_delivery_order
FOREIGN KEY (order_id) REFERENCES orders(id);

ALTER TABLE deliveries
ADD CONSTRAINT fk_delivery_warehouse
FOREIGN KEY (warehouse_id) REFERENCES warehouses(id);

ALTER TABLE deliveries
ADD CONSTRAINT fk_delivery_company
FOREIGN KEY (company_id) REFERENCES companies(id);

ALTER TABLE delivery_items
ADD CONSTRAINT fk_delivery_item_delivery
FOREIGN KEY (delivery_id) REFERENCES deliveries(id);

ALTER TABLE delivery_items
ADD CONSTRAINT fk_delivery_item_product
FOREIGN KEY (product_id) REFERENCES products(id);

-- ========================
-- SAMPLE DATA
-- ========================
INSERT INTO warehouses (name, address, created_at, updated_at)
VALUES
('Kho Sông Mây', 'Đồng Nai', NOW(), NOW()),
('Kho Hà Nội', 'Hà Nội', NOW(), NOW());

INSERT INTO companies (name, address, tax_code, created_at, updated_at)
VALUES
('CÔNG TY QUỐC TẾ CUỘC SỐNG VIỆT', 'Đồng Nai', '123456789', NOW(), NOW()),
('CÔNG TY ABC', 'HCM', '987654321', NOW(), NOW());