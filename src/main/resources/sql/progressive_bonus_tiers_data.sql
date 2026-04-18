SET FOREIGN_KEY_CHECKS = 0;
START TRANSACTION;

-- =====================================================
-- 0) RESET DATA CU
-- =====================================================
TRUNCATE TABLE progressive_bonus_tiers;

DELETE FROM config_parameters
WHERE group_code IN ('ANNUAL_BONUS', 'KPI_APPENDIX_2026');

-- =====================================================
-- 1) INSERT DIEU KIEN THUONG NAM
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
('ANNUAL_BONUS', 'RULE_01', '0.00', 'DECIMAL', '2026-01-01', NULL, 1,
 'Thu nợ < 80% | HT doanh số: bất kỳ | Tỷ lệ hưởng: 0%', NOW(), NOW()),

('ANNUAL_BONUS', 'RULE_02', '0.49', 'DECIMAL', '2026-01-01', NULL, 1,
 'Thu nợ 80% - 89.9% | HT doanh số 80% - 89.9% | Tỷ lệ hưởng: 49%', NOW(), NOW()),

('ANNUAL_BONUS', 'RULE_03', '0.80', 'DECIMAL', '2026-01-01', NULL, 1,
 'Thu nợ >= 90% | HT doanh số 90% - 99.9% | Tỷ lệ hưởng: 80%', NOW(), NOW()),

('ANNUAL_BONUS', 'RULE_04', '1.00', 'DECIMAL', '2026-01-01', NULL, 1,
 'Thu nợ >= 90% | HT doanh số 100% - 109.9% | Tỷ lệ hưởng: 100%', NOW(), NOW());

-- =====================================================
-- 2) INSERT CAC NGUONG KPI / HO TRO TRA CUU
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
('KPI_APPENDIX_2026', 'KPI_FORMULA', 'Doanh số thuần = Doanh số MISA (GTQD) - Doanh số nợ 2025 quy đổi', 'STRING', '2026-01-01', NULL, 1,
 'Nguyên tắc xác định KPI', NOW(), NOW()),

('KPI_APPENDIX_2026', 'KPI_RATIO_FORMULA', 'Tỷ lệ hoàn thành KPI = Doanh số thuần / Chỉ tiêu đăng ký năm', 'STRING', '2026-01-01', NULL, 1,
 'Công thức tỷ lệ hoàn thành KPI', NOW(), NOW()),

('KPI_APPENDIX_2026', 'OVR_MIN_THRESHOLD', '0.03', 'DECIMAL', '2026-01-01', NULL, 1,
 'Ngưỡng tối thiểu để bắt đầu xét thưởng vượt = 3%', NOW(), NOW());

-- =====================================================
-- 3) INSERT BAC THUONG VUOT
-- =====================================================
INSERT INTO progressive_bonus_tiers (
    code,
    from_rate,
    to_rate,
    bonus_rate,
    effective_from,
    effective_to,
    status,
    sort_order,
    description,
    created_at,
    updated_at
) VALUES
('TIER_01', 0.03, 0.05, 1.0000, '2026-01-01', NULL, 1, 1, 'Mức 1: từ 3% đến dưới 5%', NOW(), NOW()),
('TIER_02', 0.05, 0.07, 1.0000, '2026-01-01', NULL, 1, 2, 'Mức 2: từ 5% đến dưới 7%', NOW(), NOW()),
('TIER_03', 0.07, 0.10, 1.0000, '2026-01-01', NULL, 1, 3, 'Mức 3: từ 7% đến dưới 10%', NOW(), NOW()),
('TIER_04', 0.10, 0.13, 1.0000, '2026-01-01', NULL, 1, 4, 'Mức 4: từ 10% đến dưới 13%', NOW(), NOW()),
('TIER_05', 0.13, 0.15, 1.0000, '2026-01-01', NULL, 1, 5, 'Mức 5: từ 13% đến dưới 15%', NOW(), NOW()),
('TIER_06', 0.15, 0.18, 1.0000, '2026-01-01', NULL, 1, 6, 'Mức 6: từ 15% đến dưới 18%', NOW(), NOW()),
('TIER_07', 0.18, 0.20, 1.0000, '2026-01-01', NULL, 1, 7, 'Mức 7: từ 18% đến dưới 20%', NOW(), NOW()),
('TIER_08', 0.20, NULL, 1.0000, '2026-01-01', NULL, 1, 8, 'Mức 8: từ 20% trở lên', NOW(), NOW());

-- =====================================================
-- 4) CHECK KET QUA
-- =====================================================
SELECT 'config_parameters' AS table_name, COUNT(*) AS total_rows
FROM config_parameters
WHERE group_code IN ('ANNUAL_BONUS', 'KPI_APPENDIX_2026')

UNION ALL

SELECT 'progressive_bonus_tiers', COUNT(*) AS total_rows
FROM progressive_bonus_tiers;

COMMIT;
SET FOREIGN_KEY_CHECKS = 1;