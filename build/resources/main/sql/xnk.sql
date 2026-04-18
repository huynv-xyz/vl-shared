SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS shipment_costs;
DROP TABLE IF EXISTS shipment_items;
DROP TABLE IF EXISTS shipments;
DROP TABLE IF EXISTS contract_items;
DROP TABLE IF EXISTS contracts;

SET FOREIGN_KEY_CHECKS = 1;

-- ========================
-- contracts
-- ========================
CREATE TABLE contracts (
  id int NOT NULL AUTO_INCREMENT COMMENT 'ID hợp đồng',
  code varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Mã hợp đồng',

  supplier_id int NOT NULL COMMENT 'ID nhà cung cấp',
  currency_id int NOT NULL COMMENT 'ID tiền tệ',

  signed_date datetime DEFAULT NULL COMMENT 'Ngày ký',
  deposit_rate decimal(5,2) DEFAULT 0 COMMENT 'Tỷ lệ đặt cọc (%)',

  status varchar(50) DEFAULT 'DRAFT' COMMENT 'Trạng thái',

  created_at datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
  created_by int DEFAULT NULL COMMENT 'Người tạo',
  updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
  updated_by int DEFAULT NULL COMMENT 'Người cập nhật',

  PRIMARY KEY (id),
  KEY idx_supplier (supplier_id),
  KEY idx_currency (currency_id),

  CONSTRAINT fk_contract_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
  CONSTRAINT fk_contract_currency FOREIGN KEY (currency_id) REFERENCES currencies(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Hợp đồng nhập hàng';

-- ========================
-- contract_items
-- ========================
CREATE TABLE contract_items (
  id int NOT NULL AUTO_INCREMENT COMMENT 'ID dòng hợp đồng',

  contract_id int NOT NULL COMMENT 'FK → contracts.id (hợp đồng)',
  product_id int NOT NULL COMMENT 'FK → products.id (sản phẩm)',

  quantity decimal(18,2) NOT NULL COMMENT 'Số lượng theo hợp đồng (SL ký)',

  unit_price decimal(18,2) NOT NULL COMMENT 'Đơn giá trước chiết khấu (giá gốc / 1 đơn vị)',
  discount_amount decimal(18,2) DEFAULT 0 COMMENT 'Số tiền giảm giá trên 1 đơn vị',

  created_at datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
  created_by int DEFAULT NULL COMMENT 'Người tạo',
  updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
  updated_by int DEFAULT NULL COMMENT 'Người cập nhật',

  PRIMARY KEY (id),

  UNIQUE KEY uk_contract_product (contract_id, product_id) COMMENT 'Mỗi sản phẩm chỉ xuất hiện 1 lần trong 1 hợp đồng',

  KEY idx_contract (contract_id) COMMENT 'Index theo contract để query nhanh',
  KEY idx_product (product_id) COMMENT 'Index theo product để join/report',

  CONSTRAINT fk_ci_contract FOREIGN KEY (contract_id) REFERENCES contracts(id),
  CONSTRAINT fk_ci_product FOREIGN KEY (product_id) REFERENCES products(id)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci
COMMENT='Danh sách hàng hóa theo hợp đồng (giá thỏa thuận ban đầu, chưa bao gồm chi phí lô hàng)';

-- ========================
-- shipments
-- ========================
CREATE TABLE shipments (
  id int NOT NULL AUTO_INCREMENT COMMENT 'ID lô hàng',

  contract_id int NOT NULL COMMENT 'ID hợp đồng',
  code varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Mã lô hàng',

  etd datetime DEFAULT NULL COMMENT 'Ngày đi',
  eta datetime DEFAULT NULL COMMENT 'Ngày đến',
  exchange_rate decimal(18,6) DEFAULT 1 COMMENT 'Tỷ giá tại thời điểm lô',

  status varchar(50) DEFAULT 'PLANNED' COMMENT 'Trạng thái',

  created_at datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
  created_by int DEFAULT NULL COMMENT 'Người tạo',
  updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
  updated_by int DEFAULT NULL COMMENT 'Người cập nhật',

  PRIMARY KEY (id),

  KEY idx_contract (contract_id),

  CONSTRAINT fk_shipment_contract FOREIGN KEY (contract_id) REFERENCES contracts(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Lô hàng theo hợp đồng';

-- ========================
-- shipment_items
-- ========================
CREATE TABLE shipment_items (
  id int NOT NULL AUTO_INCREMENT COMMENT 'ID dòng lô hàng',

  shipment_id int NOT NULL COMMENT 'ID lô hàng',
  product_id int NOT NULL COMMENT 'ID sản phẩm',

  quantity decimal(18,2) NOT NULL COMMENT 'Số lượng thực tế',
  unit_price decimal(18,2) NOT NULL COMMENT 'Giá thực tế',

  created_at datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
  created_by int DEFAULT NULL COMMENT 'Người tạo',
  updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật',
  updated_by int DEFAULT NULL COMMENT 'Người cập nhật',

  PRIMARY KEY (id),

  UNIQUE KEY uk_shipment_product (shipment_id, product_id),

  KEY idx_shipment (shipment_id),
  KEY idx_product (product_id),

  CONSTRAINT fk_si_shipment FOREIGN KEY (shipment_id) REFERENCES shipments(id),
  CONSTRAINT fk_si_product FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Danh sách hàng trong lô';

-- ========================
-- shipment_costs
-- ========================
CREATE TABLE shipment_costs (
  id int NOT NULL AUTO_INCREMENT COMMENT 'ID chi phí',

  shipment_id int NOT NULL COMMENT 'ID lô hàng',

  type varchar(50) NOT NULL COMMENT 'Loại chi phí (SHIPPING, PACKAGING, TAX, OTHER)',
  amount decimal(18,2) NOT NULL COMMENT 'Số tiền',
  note varchar(255) DEFAULT NULL COMMENT 'Ghi chú',

  created_at datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
  created_by int DEFAULT NULL COMMENT 'Người tạo',

  PRIMARY KEY (id),

  KEY idx_shipment (shipment_id),

  CONSTRAINT fk_cost_shipment FOREIGN KEY (shipment_id) REFERENCES shipments(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chi phí theo lô hàng';

-- ========================
-- payments
-- ========================
CREATE TABLE payments (
  id int NOT NULL AUTO_INCREMENT COMMENT 'ID thanh toán',

  contract_id int NOT NULL COMMENT 'ID hợp đồng',
  shipment_id int DEFAULT NULL COMMENT 'ID lô hàng (nullable)',

  type varchar(50) NOT NULL COMMENT 'DEPOSIT | PAYMENT | FEE',
  amount decimal(18,2) NOT NULL COMMENT 'Số tiền',

  paid_at datetime DEFAULT NULL COMMENT 'Ngày thanh toán',
  note varchar(255) DEFAULT NULL COMMENT 'Ghi chú',

  created_at datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo',
  created_by int DEFAULT NULL COMMENT 'Người tạo',

  PRIMARY KEY (id),

  KEY idx_contract (contract_id),
  KEY idx_shipment (shipment_id),

  CONSTRAINT fk_payment_contract FOREIGN KEY (contract_id) REFERENCES contracts(id),
  CONSTRAINT fk_payment_shipment FOREIGN KEY (shipment_id) REFERENCES shipments(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Thanh toán hợp đồng/lô hàng';