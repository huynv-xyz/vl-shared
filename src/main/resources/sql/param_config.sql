SET FOREIGN_KEY_CHECKS = 0;
START TRANSACTION;

-- =====================================================
-- 0) RESET DATA CU
-- =====================================================
TRUNCATE TABLE bonus_split_rules;
TRUNCATE TABLE config_parameters;

-- regions dang duoc employee_scopes / manager_mappings / sales_* tham chieu
-- chi truncate neu he thong dang moi hoan toan
TRUNCATE TABLE regions;

-- =====================================================
-- 1) TAO BANG LUU CAU HINH THEO VUNG (NEU CHUA CO)
-- =====================================================
CREATE TABLE IF NOT EXISTS region_income_configs (
    id INT NOT NULL AUTO_INCREMENT,
    region_id INT NOT NULL,
    bon_goc_ton DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    bon_la_l_lit DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    bon_la_b_kg DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    clcn_ton DECIMAL(18,2) NOT NULL DEFAULT 0.00,
    hs_bon_goc DECIMAL(18,4) NOT NULL DEFAULT 0.0000,
    hs_bon_la_l DECIMAL(18,4) NOT NULL DEFAULT 0.0000,
    hs_bon_la_b DECIMAL(18,4) NOT NULL DEFAULT 0.0000,
    hs_clcn DECIMAL(18,4) NOT NULL DEFAULT 0.0000,
    effective_from DATE NOT NULL,
    effective_to DATE DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    note VARCHAR(255) DEFAULT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_region_effective_from (region_id, effective_from),
    KEY idx_region_id (region_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

TRUNCATE TABLE region_income_configs;

-- =====================================================
-- 2) IMPORT REGIONS
-- =====================================================
INSERT INTO regions (
    code, name, status, created_at, updated_at
) VALUES
('R01', 'Miền Đông', 1, NOW(), NOW()),
('R02', 'Miền Trung', 1, NOW(), NOW()),
('R03', 'Tây Nguyên', 1, NOW(), NOW()),
('R04', 'Miền Tây', 1, NOW(), NOW()),
('R05', 'Miền Bắc', 1, NOW(), NOW());

-- =====================================================
-- 3) IMPORT CONFIG_PARAMETERS
-- =====================================================
INSERT INTO config_parameters (
    group_code,
    code,
    value,
    value_type,
    effective_from,
    effective_to,
    status,
    description,
    created_at,
    updated_at
) VALUES
-- don gia / rate chung
('PAYROLL_CORE', 'RATE_BON_LA_LIT', '20000', 'DECIMAL', '2026-01-01', NULL, 1, '20.000 đ/lít', NOW(), NOW()),
('PAYROLL_CORE', 'RATE_BON_GOC_TON', '200000', 'DECIMAL', '2026-01-01', NULL, 1, '200.000 đ/tấn', NOW(), NOW()),
('PAYROLL_CORE', 'RATE_CL_BOT_KG', '3000', 'DECIMAL', '2026-01-01', NULL, 1, '3.000 đ/kg (đã chuẩn hóa CL bột sang kg)', NOW(), NOW()),
('PAYROLL_CORE', 'RATE_CLCN_TON', '300000', 'DECIMAL', '2026-01-01', NULL, 1, '300.000 đ/tấn', NOW(), NOW()),
('PAYROLL_CORE', 'BONUS_OVR', '200000', 'DECIMAL', '2026-01-01', NULL, 1, 'Đơn vị quy đổi thưởng vượt', NOW(), NOW()),

-- ty le quy thu nhap
('PAYROLL_CORE', 'FIXED_RATIO_ALL', '0.80', 'DECIMAL', '2026-01-01', NULL, 1, '80% quỹ thu nhập dùng trả lương + phụ cấp', NOW(), NOW()),
('PAYROLL_CORE', 'BONUS_RATIO_ALL', '0.20', 'DECIMAL', '2026-01-01', NULL, 1, '20% quỹ thu nhập là thưởng năm', NOW(), NOW()),

-- thu no
('PAYROLL_DEBT', 'THU_NO_TOI_THIEU', '0.80', 'DECIMAL', '2026-01-01', NULL, 1, 'Thu nợ dưới 80% = 0 thưởng', NOW(), NOW()),
('PAYROLL_DEBT', 'THU_NO_80_89', '0.70', 'DECIMAL', '2026-01-01', NULL, 1, 'Thu nợ 80%-89,9%', NOW(), NOW()),
('PAYROLL_DEBT', 'THU_NO_90_UP', '1.00', 'DECIMAL', '2026-01-01', NULL, 1, 'Thu nợ từ 90%', NOW(), NOW()),

-- he so hoan thanh doanh so
('PAYROLL_PERFORMANCE', 'DS_HT_85_UP', '0.70', 'DECIMAL', '2026-01-01', NULL, 1, 'Hoàn thành doanh số từ 85% đến dưới 90%', NOW(), NOW()),
('PAYROLL_PERFORMANCE', 'DS_HT_90_99', '0.80', 'DECIMAL', '2026-01-01', NULL, 1, 'Hoàn thành doanh số từ 90% đến dưới 100%', NOW(), NOW()),
('PAYROLL_PERFORMANCE', 'DS_HT_100', '1.00', 'DECIMAL', '2026-01-01', NULL, 1, 'Hoàn thành doanh số từ 100%', NOW(), NOW()),

-- uplift toi thieu
('PAYROLL_PROTECTION', 'MIN_UPLIFT_SALE', '0.03', 'DECIMAL', '2026-01-01', NULL, 1, 'Sale tối thiểu +3% so với TN 2025', NOW(), NOW()),
('PAYROLL_PROTECTION', 'MIN_UPLIFT_TECH', '0.05', 'DECIMAL', '2026-01-01', NULL, 1, 'Kỹ thuật tối thiểu +5%', NOW(), NOW()),
('PAYROLL_PROTECTION', 'MIN_UPLIFT_MGR', '0.05', 'DECIMAL', '2026-01-01', NULL, 1, 'Quản lý tối thiểu +5%', NOW(), NOW()),

-- ho tro chien luoc
('PAYROLL_SUPPORT', 'STRATEGIC_SUPPORT_NEW', '3000000', 'DECIMAL', '2026-01-01', NULL, 1, 'Hỗ trợ tháng vùng mới / zero target', NOW(), NOW()),
('PAYROLL_SUPPORT', 'STRATEGIC_SUPPORT_WEAK', '2000000', 'DECIMAL', '2026-01-01', NULL, 1, 'Hỗ trợ tháng vùng yếu', NOW(), NOW()),
('PAYROLL_SUPPORT', 'PROTECT_2025_RATIO', '0.95', 'DECIMAL', '2026-01-01', NULL, 1, 'Bảo vệ tối thiểu 95% thu nhập 2025', NOW(), NOW()),

-- bonus split luu them o config de de lookup nhanh neu can
('BONUS_SPLIT', 'WITH_ASM_SALE', '0.65', 'DECIMAL', '2026-01-01', NULL, 1, 'Vùng có ASM: Sale nhận 65% quỹ thưởng vượt vùng', NOW(), NOW()),
('BONUS_SPLIT', 'WITH_ASM_ASM', '0.10', 'DECIMAL', '2026-01-01', NULL, 1, 'Vùng có ASM: ASM nhận 10% quỹ thưởng vượt vùng', NOW(), NOW()),
('BONUS_SPLIT', 'WITH_ASM_RM', '0.15', 'DECIMAL', '2026-01-01', NULL, 1, 'Vùng có ASM: RM nhận 15% quỹ thưởng vượt vùng', NOW(), NOW()),
('BONUS_SPLIT', 'NO_ASM_SALE', '0.80', 'DECIMAL', '2026-01-01', NULL, 1, 'Vùng không ASM: Sale nhận 80% quỹ thưởng vượt vùng', NOW(), NOW()),
('BONUS_SPLIT', 'NO_ASM_RM', '0.20', 'DECIMAL', '2026-01-01', NULL, 1, 'Vùng không ASM: RM nhận 20% quỹ thưởng vượt vùng', NOW(), NOW());

-- =====================================================
-- 4) IMPORT BONUS_SPLIT_RULES
-- =====================================================
INSERT INTO bonus_split_rules (
    code,
    has_asm,
    sales_rate,
    asm_rate,
    rm_rate,
    effective_from,
    effective_to,
    status,
    description,
    created_at,
    updated_at
) VALUES
('RULE_HAS_ASM', 1, 0.65, 0.10, 0.15, '2026-01-01', NULL, 1, 'Vùng có ASM', NOW(), NOW()),
('RULE_NO_ASM', 0, 0.80, 0.00, 0.20, '2026-01-01', NULL, 1, 'Vùng không ASM', NOW(), NOW());

-- =====================================================
-- 5) IMPORT CAU HINH THU NHAP THEO VUNG
-- =====================================================
INSERT INTO region_income_configs (
    region_id,
    bon_goc_ton,
    bon_la_l_lit,
    bon_la_b_kg,
    clcn_ton,
    hs_bon_goc,
    hs_bon_la_l,
    hs_bon_la_b,
    hs_clcn,
    effective_from,
    effective_to,
    status,
    note,
    created_at,
    updated_at
)
SELECT
    r.id,
    300000.00,
    20000.00,
    4000.00,
    500000.00,
    1.0000,
    0.0700,
    0.0100,
    1.6700,
    '2026-01-01',
    NULL,
    1,
    CONCAT('Imported for region ', r.code),
    NOW(),
    NOW()
FROM regions r
WHERE r.code IN ('R01', 'R02', 'R03', 'R04', 'R05');

-- =====================================================
-- 6) CHECK KET QUA
-- =====================================================
SELECT 'regions' AS table_name, COUNT(*) AS total_rows FROM regions
UNION ALL
SELECT 'config_parameters', COUNT(*) FROM config_parameters
UNION ALL
SELECT 'bonus_split_rules', COUNT(*) FROM bonus_split_rules
UNION ALL
SELECT 'region_income_configs', COUNT(*) FROM region_income_configs;

COMMIT;
SET FOREIGN_KEY_CHECKS = 1;