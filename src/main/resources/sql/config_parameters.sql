START TRANSACTION;

-- =====================================================
-- 0) Truncate dữ liệu cũ
-- =====================================================
TRUNCATE TABLE bonus_split_rules;
TRUNCATE TABLE region_income_configs;
TRUNCATE TABLE config_parameters;

-- =====================================================
-- 1) Import config_parameters
-- =====================================================
INSERT INTO config_parameters
(
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
)
VALUES
-- ===== Đơn giá / rate chung =====
('PAYROLL_GENERAL', 'RATE_BON_LA_LIT',        '20000',    'DECIMAL', '2026-01-01', NULL, 1, '20.000 đ/lít', NOW(), NOW()),
('PAYROLL_GENERAL', 'RATE_BON_GOC_TON',       '200000',   'DECIMAL', '2026-01-01', NULL, 1, '200.000 đ/tấn', NOW(), NOW()),
('PAYROLL_GENERAL', 'RATE_CL_BOT_KG',         '3000',     'DECIMAL', '2026-01-01', NULL, 1, '3.000 đ/kg (đã chuẩn hóa CL bột sang kg cho cả 2025 và 2026)', NOW(), NOW()),
('PAYROLL_GENERAL', 'RATE_CLCN_TON',          '300000',   'DECIMAL', '2026-01-01', NULL, 1, '300.000 đ/tấn', NOW(), NOW()),

-- ===== Tỷ lệ quỹ thu nhập =====
('PAYROLL_RATIO',   'FIXED_RATIO_ALL',        '0.8',      'DECIMAL', '2026-01-01', NULL, 1, '80% quỹ thu nhập dùng trả lương + phụ cấp', NOW(), NOW()),
('PAYROLL_RATIO',   'BONUS_RATIO_ALL',        '0.2',      'DECIMAL', '2026-01-01', NULL, 1, '20% quỹ thu nhập là thưởng năm', NOW(), NOW()),

-- ===== Điều kiện thưởng / ngưỡng gating =====
('BONUS_CONDITION', 'THU_NO_TOI_THIEU',       '0.8',      'DECIMAL', '2026-01-01', NULL, 1, 'Thu nợ dưới 80% = 0 thưởng', NOW(), NOW()),
('BONUS_GATE_DEBT', 'THU_NO_80_89',           '0.7',      'DECIMAL', '2026-01-01', NULL, 1, 'Thu nợ 80-89,9% => hưởng 70% mức thưởng', NOW(), NOW()),
('BONUS_GATE_DEBT', 'THU_NO_90_PLUS',         '1',        'DECIMAL', '2026-01-01', NULL, 1, 'Thu nợ từ 90% => hưởng 100% mức thưởng', NOW(), NOW()),
('BONUS_GATE_COMPLETION', 'DS_HT_85_PLUS',    '0.7',      'DECIMAL', '2026-01-01', NULL, 1, 'HT doanh số từ 85% => hưởng 70% mức thưởng', NOW(), NOW()),
('BONUS_GATE_COMPLETION', 'DS_HT_90_99',      '0.8',      'DECIMAL', '2026-01-01', NULL, 1, 'HT doanh số 90-99,9% => hưởng 80% mức thưởng', NOW(), NOW()),
('BONUS_GATE_COMPLETION', 'DS_HT_100',        '1',        'DECIMAL', '2026-01-01', NULL, 1, 'HT doanh số 100-109,9% => hưởng 100% mức thưởng', NOW(), NOW()),

-- ===== Thưởng vượt =====
('EXTRA_BONUS',     'BONUS_OVR',              '200000',   'DECIMAL', '2026-01-01', NULL, 1, 'Đơn vị quy đổi thưởng vượt', NOW(), NOW()),

-- ===== Bảo vệ / uplift =====
('INCOME_PROTECTION', 'MIN_UPLIFT_SALE',      '0.03',     'DECIMAL', '2026-01-01', NULL, 1, 'Sale tối thiểu +3% so với thu nhập 2025', NOW(), NOW()),
('INCOME_PROTECTION', 'MIN_UPLIFT_TECH',      '0.05',     'DECIMAL', '2026-01-01', NULL, 1, 'Kỹ thuật tối thiểu +5%', NOW(), NOW()),
('INCOME_PROTECTION', 'MIN_UPLIFT_MGR',       '0.05',     'DECIMAL', '2026-01-01', NULL, 1, 'Quản lý tối thiểu +5%', NOW(), NOW()),
('STRATEGIC_SUPPORT', 'STRATEGIC_SUPPORT_NEW','3000000',  'DECIMAL', '2026-01-01', NULL, 1, 'Hỗ trợ tháng vùng mới/zero target', NOW(), NOW()),
('STRATEGIC_SUPPORT', 'STRATEGIC_SUPPORT_WEAK','2000000', 'DECIMAL', '2026-01-01', NULL, 1, 'Hỗ trợ tháng vùng yếu', NOW(), NOW()),
('INCOME_PROTECTION', 'PROTECT_2025_RATIO',   '0.95',     'DECIMAL', '2026-01-01', NULL, 1, 'Bảo vệ tối thiểu 95% thu nhập 2025 bằng phần hỗ trợ chiến lược nếu cần', NOW(), NOW());

-- =====================================================
-- 2) Import region_income_configs
--    Yêu cầu bảng regions đã có sẵn code: R01..R05
-- =====================================================
INSERT INTO region_income_configs
(
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
    x.bon_goc_ton,
    x.bon_la_l_lit,
    x.bon_la_b_kg,
    x.clcn_ton,
    x.hs_bon_goc,
    x.hs_bon_la_l,
    x.hs_bon_la_b,
    x.hs_clcn,
    '2026-01-01',
    NULL,
    1,
    x.note,
    NOW(),
    NOW()
FROM regions r
JOIN (
    SELECT 'R03' AS region_code, 300000 AS bon_goc_ton, 20000 AS bon_la_l_lit, 4000 AS bon_la_b_kg, 500000 AS clcn_ton,
           1.00 AS hs_bon_goc, 0.07 AS hs_bon_la_l, 0.01 AS hs_bon_la_b, 1.67 AS hs_clcn, 'Tây Nguyên' AS note
    UNION ALL
    SELECT 'R01', 300000, 20000, 4000, 500000, 1.00, 0.07, 0.01, 1.67, 'Miền Đông'
    UNION ALL
    SELECT 'R04', 300000, 20000, 4000, 500000, 1.00, 0.07, 0.01, 1.67, 'Miền Tây'
    UNION ALL
    SELECT 'R02', 300000, 20000, 4000, 500000, 1.00, 0.07, 0.01, 1.67, 'Miền Trung'
    UNION ALL
    SELECT 'R05', 300000, 20000, 4000, 500000, 1.00, 0.07, 0.01, 1.67, 'Miền Bắc'
) x
    ON r.code = x.region_code;

-- =====================================================
-- 3) Import bonus_split_rules
-- =====================================================
INSERT INTO bonus_split_rules
(
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
)
VALUES
(
    'RULE_HAS_ASM',
    1,
    0.65,
    0.10,
    0.15,
    '2026-01-01',
    NULL,
    1,
    'Vùng có ASM: SALE 65%, ASM 10%, RM 15%',
    NOW(),
    NOW()
),
(
    'RULE_NO_ASM',
    0,
    0.80,
    0.00,
    0.20,
    '2026-01-01',
    NULL,
    1,
    'Vùng không ASM: SALE 80%, RM 20%',
    NOW(),
    NOW()
);

COMMIT;