SET FOREIGN_KEY_CHECKS = 0;
START TRANSACTION;

-- =====================================================
-- 1) XOA SCOPE ASM CU
-- =====================================================
DELETE es
FROM employee_scopes es
JOIN roles r ON r.id = es.role_id
WHERE r.code = 'MGR_PROV';

-- =====================================================
-- 2) INSERT SCOPE ASM MOI
-- =====================================================
INSERT INTO employee_scopes (
    employee_id,
    role_id,
    region_id,
    province_id,
    is_personal_target,
    is_manager_target,
    effective_from,
    effective_to,
    status,
    created_at,
    updated_at
)
SELECT
    e.id AS employee_id,
    ro.id AS role_id,
    rg.id AS region_id,
    p.id AS province_id,
    0 AS is_personal_target,
    1 AS is_manager_target,
    '2026-01-01' AS effective_from,
    NULL AS effective_to,
    1 AS status,
    NOW(),
    NOW()
FROM (
    SELECT 'ASM029' AS emp_id, 'R04' AS ma_kv, NULL AS ma_tinh_phu_trach, 'MGR_PROV' AS ma_cv
    UNION ALL
    SELECT 'ASM032', 'R04', 'DT', 'MGR_PROV'
    UNION ALL
    SELECT 'ASM032', 'R04', 'KG', 'MGR_PROV'
    UNION ALL
    SELECT 'ASM032', 'R04', 'CT', 'MGR_PROV'
    UNION ALL
    SELECT 'ASM032', 'R04', 'ST', 'MGR_PROV'
) s
JOIN employees e
    ON e.code = s.emp_id
JOIN roles ro
    ON ro.code = s.ma_cv
LEFT JOIN regions rg
    ON rg.code = s.ma_kv
LEFT JOIN provinces p
    ON p.code = s.ma_tinh_phu_trach;

-- =====================================================
-- 3) CHECK KET QUA
-- =====================================================
SELECT
    es.id,
    e.code AS employee_code,
    e.name AS employee_name,
    r.code AS role_code,
    r.name AS role_name,
    rg.code AS region_code,
    rg.name AS region_name,
    p.code AS province_code,
    p.name AS province_name,
    es.is_personal_target,
    es.is_manager_target,
    es.effective_from,
    es.effective_to,
    es.status
FROM employee_scopes es
JOIN employees e ON e.id = es.employee_id
JOIN roles r ON r.id = es.role_id
LEFT JOIN regions rg ON rg.id = es.region_id
LEFT JOIN provinces p ON p.id = es.province_id
WHERE r.code = 'MGR_PROV'
ORDER BY e.code, rg.code, p.code;

COMMIT;
SET FOREIGN_KEY_CHECKS = 1;