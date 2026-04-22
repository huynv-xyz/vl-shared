-- =========================================
-- EXPORT (PHIẾU XUẤT KHO)
-- =========================================

CREATE TABLE exports (
  id INT PRIMARY KEY AUTO_INCREMENT,

  export_no VARCHAR(50) NOT NULL,           -- PX-20260419-001
  export_date DATE NOT NULL,

  delivery_id INT NOT NULL,
  order_id INT NOT NULL,

  warehouse_id INT NOT NULL,

  status VARCHAR(20) DEFAULT 'DRAFT',       -- DRAFT / DONE / CANCELLED

  note TEXT,

  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,

  created_by INT,
  updated_by INT,

  UNIQUE KEY uk_export_no (export_no),

  INDEX idx_delivery_id (delivery_id),
  INDEX idx_order_id (order_id),
  INDEX idx_warehouse_id (warehouse_id),
  INDEX idx_status (status),
  INDEX idx_export_date (export_date)
);

-- =========================================
-- EXPORT ITEMS (DÒNG HÀNG XUẤT)
-- =========================================

CREATE TABLE export_items (
  id INT PRIMARY KEY AUTO_INCREMENT,

  export_id INT NOT NULL,

  product_id INT NOT NULL,
  quantity DECIMAL(18,3) NOT NULL,

  note TEXT,

  created_at DATETIME,
  updated_at DATETIME,

  -- chống trùng dòng sản phẩm trong 1 phiếu
  UNIQUE KEY uk_export_product (export_id, product_id),

  INDEX idx_export_id (export_id),
  INDEX idx_product_id (product_id),

  CONSTRAINT fk_export_items_export
    FOREIGN KEY (export_id) REFERENCES exports(id)
);

-- =========================================
-- INVENTORY LEDGER (SỔ KHO)
-- =========================================

CREATE TABLE inventory_ledger (
  id INT PRIMARY KEY AUTO_INCREMENT,

  product_id INT NOT NULL,
  warehouse_id INT NOT NULL,

  quantity_change DECIMAL(18,3) NOT NULL, -- âm khi xuất

  ref_type VARCHAR(20),   -- EXPORT
  ref_id INT,

  created_at DATETIME NOT NULL,

  INDEX idx_product (product_id),
  INDEX idx_warehouse (warehouse_id),
  INDEX idx_ref (ref_type, ref_id)
);

-- =========================================
-- AR LEDGER (SỔ CÔNG NỢ)
-- =========================================

CREATE TABLE ar_ledger (
  id INT PRIMARY KEY AUTO_INCREMENT,

  posting_date DATE NOT NULL,

  customer_id INT NOT NULL,

  doc_type VARCHAR(20),   -- BAN_HANG / THU_TIEN / TRA_HANG
  doc_no VARCHAR(50),

  order_id INT,
  export_id INT,

  product_id INT,
  quantity DECIMAL(18,3),

  debit_amount DECIMAL(18,2),   -- tăng công nợ
  credit_amount DECIMAL(18,2),  -- giảm công nợ

  created_at DATETIME NOT NULL,

  -- chống ghi trùng doanh thu
  UNIQUE KEY uk_business (export_id, product_id),

  INDEX idx_customer (customer_id),
  INDEX idx_posting_date (posting_date)
);

-- =========================================
-- QUERY: TỔNG SL ĐÃ XUẤT
-- =========================================

-- dùng check không vượt order
/*
SELECT
  ei.product_id,
  SUM(ei.quantity) AS exported_qty
FROM export_items ei
JOIN exports e ON e.id = ei.export_id
WHERE e.order_id = :order_id
  AND e.status = 'DONE'
GROUP BY ei.product_id;
*/

-- =========================================
-- RULE NGHIỆP VỤ (NHỚ)
-- =========================================

-- 1. status = DONE mới:
--    - trừ kho (inventory_ledger)
--    - ghi công nợ (ar_ledger)

-- 2. Không cho xuất vượt order:
--    exported + new <= ordered

-- 3. Mỗi export + product chỉ ghi ledger 1 lần
--    (đã khóa bằng UNIQUE KEY)

-- 4. Không xóa cứng → chỉ CANCELLED