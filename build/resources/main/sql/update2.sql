SET FOREIGN_KEY_CHECKS = 0;
START TRANSACTION;

-- =====================================================
-- 0) RESET DATA CU
-- =====================================================
TRUNCATE TABLE extra_bonus_allocations;
TRUNCATE TABLE extra_bonus_audits;
TRUNCATE TABLE payroll_employee_results;
TRUNCATE TABLE payroll_scope_results;
TRUNCATE TABLE payroll_run_errors;
TRUNCATE TABLE payroll_runs;
TRUNCATE TABLE sales_actuals;
TRUNCATE TABLE sales_targets;
TRUNCATE TABLE manager_mappings;
TRUNCATE TABLE employee_scopes;
TRUNCATE TABLE employees;
TRUNCATE TABLE provinces;
TRUNCATE TABLE regions;
TRUNCATE TABLE roles;

-- =====================================================
-- 1) STAGING TABLE
-- =====================================================
DROP TABLE IF EXISTS employee_import_staging;

CREATE TABLE employee_import_staging (
    emp_id VARCHAR(50) NULL,
    full_name VARCHAR(255) NULL,
    ma_tinh VARCHAR(20) NULL,
    ma_kv VARCHAR(20) NULL,
    chuc_vu VARCHAR(100) NULL,
    ma_cv VARCHAR(50) NULL,
    asm_map_emp VARCHAR(50) NULL,
    rm_map_emp VARCHAR(50) NULL,
    nam_sinh VARCHAR(20) NULL,
    gioi_tinh VARCHAR(20) NULL,
    quoc_tich VARCHAR(100) NULL,
    dia_chi_thuong_tru TEXT NULL,
    so_cccd VARCHAR(50) NULL,
    ngay_cap VARCHAR(50) NULL,
    noi_cap VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2) INSERT RAW CSV DATA TRUC TIEP
-- =====================================================
INSERT INTO employee_import_staging (
    emp_id, full_name, ma_tinh, ma_kv, chuc_vu, ma_cv,
    asm_map_emp, rm_map_emp, nam_sinh, gioi_tinh, quoc_tich,
    dia_chi_thuong_tru, so_cccd, ngay_cap, noi_cap
) VALUES
('NV001','Nguyễn Thành Trung','NT','R02','SR','SALE_SELF',NULL,NULL,'26/06/1981','Nam','Việt Nam','Kp 10 Phước Mỹ, TP Phan Rang- Tháp Chàm, Ninh Thuận','075081002048','5/1/2021','CTCCSQLHCVTTXH'),
('NV002','Lê Minh Hải','BD','R02','SR','SALE_SELF','0',NULL,'15/12/1984','Nam','Việt Nam','Phước Nghĩa, Tuy Phước, Bình Định','052084010870','12/11/2024','Bộ Công An'),
('NV003','Trương Minh Đô','BT','R04','SR','SALE_SELF','0','RM010','06/07/1998','Nam','Việt Nam','Ấp Bắc A Điềm Hy, Châu Thành, Tiền Giang','082098003376','8/9/2021','CTCCSQLHCVTTXH'),
('NV004','Lê Lưu Ngọc Chân','LA','R04','SR','SALE_SELF','0','RM010','1/12/1998','Nam','Việt Nam','Ấp 6 Phú Cường, Cai Lậy, Tiền Giang','082098011644','8/10/2021','CTCCSQLHCVTTXH'),
('NV005','Lê Văn Phú (Kiêm)','TG','R04','SR','SALE_SELF','0','RM010','8/29/1992','Nam','Việt Nam','Ấp Bình Yên, X. Trường Lạc, H. Ô Môn, Tp. Cần Thơ','092092004497','5/27/2019','CTCCSQLHCVTTXH'),
('NV006','Nguyễn Văn Bình','DT','R04','SR','SALE_SELF','ASM032','RM010','28/12/1986','Nam','Việt Nam','168A Kv Phú Lợi, Tân Phú, Cái Răng, Cần Thơ','093086004414','10/5/2021','CTCCSQLHCVTTXH'),
('NV007','Nguyễn Bảo Linh','KG','R04','SR','SALE_SELF','ASM032','RM010','1/1/1995','Nam','Việt Nam','Trung Phú 3, Vĩnh Phú, Thoại Sơn, An Giang','089095017822','12/18/2023','CTCCSQLHCVTTXH'),
('NV008','Vi Quốc Cường','CT','R04','SR','SALE_SELF','ASM032','RM010','4/29/1997','Nam','Việt Nam','Ấp 1, Thạnh Phú, Cờ Đỏ, Cần Thơ','092097008118','11/22/2021','CTCCSQLHCVTTXH'),
('NV009','Nguyễn Quốc Đảm','ST','R04','SR','SALE_SELF','ASM032','RM010','8/27/1999','Nam','Việt Nam','Tổ 1, Ấp Thạnh Trì, Thạnh Trì, Tân Hiệp, Kiên Giang. Sau sát nhập: Ấp Thạnh Trì, Xã Thạnh Đông, Huyện Tân Hiệp, Tỉnh An Giang','091099014528','8/27/1999','CTCCSQLHCVTTXH'),
('RM010','Trần Ngọc Luẩn',NULL,'R01','RM','MGR_REGION',NULL,NULL,'15/11/1983','Nam','Việt Nam','Tân Phước Bắc, Vạn Phước, Vạn Ninh, Khánh Hòa','056083005341','4/10/2023','CTCCSQLHCVTTXH'),
('RM010','Trần Ngọc Luẩn',NULL,'R04','RM','MGR_REGION',NULL,NULL,'15/11/1983','Nam','Việt Nam','Tân Phước Bắc, Vạn Phước, Vạn Ninh, Khánh Hòa','056083005341','4/10/2023','CTCCSQLHCVTTXH'),
('NV011','Hứa Xuân Tiến','BT','R01','SR','SALE_SELF','0','RM010','1/8/1973','Nam','Việt Nam','Tổ 3, Ấp Thọ Bình, Xuân Thọ, Xuân Lộc, Đồng nai','075073003181','6/13/2023','CTCCSQLHCVTTXH'),
('NV012','Đoàn Minh Huy','DN','R01','SR','SALE_SELF','0','RM010','1/5/1993','Nam','Việt Nam','Trần Nguyễn Hãn, Thuận Hòa, Thành phố Huế, Thừa Thiên Huế','046093016088','11/18/2022','CTCCSQLHCVTTXH'),
('NV013','Ngô Văn Dũng (BP)','BP','R01','SR','SALE_SELF','0','RM010','10/20/1987','Nam','Việt Nam','55 Ninh Lộc, Ninh Sơn, Tp Tây Ninh, Tây Ninh','072087000636','6/13/2023','CTCCSQLHCVTTXH'),
('NV014','Ngô Văn Dũng','TN','R01','SR','SALE_SELF','0','RM010','20/10/1987','Nam','Việt Nam','55 Ninh Lộc, Ninh Sơn, Tp Tây Ninh, Tây Ninh','072087000636','6/13/2023','CTCCSQLHCVTTXH'),
('RM015','Huỳnh Thanh Hải',NULL,'R03','RM','MGR_REGION',NULL,NULL,'18/12/1990','Nam','Việt Nam','Hẻm 184/43 Giải phóng, Tổ dân phố 6B, P. Tân Lợi, Tp. Buôn Ma Thuộc','052090022224','8/9/2021','CTCCSQLHCVTTXH'),
('RM015','Huỳnh Thanh Hải','DKL','R03','SR','SALE_SELF',NULL,NULL,'18/12/1990','Nam','Việt Nam','Hẻm 184/43 Giải phóng, Tổ dân phố 6B, P. Tân Lợi, Tp. Buôn Ma Thuộc','052090022224','8/9/2021','CTCCSQLHCVTTXH'),
('NV017','Trần Nguyên Lê','DKL','R03','SR','SALE_SELF','0','RM015','30/9/1999','Nam','Việt Nam','Thôn 4, Hòa Thành, Krong Bong, Dak Lak','066099001455','4/22/2021','CTCCSQLHCVTTXH'),
('NV018','Hà Duy Phú','LD','R03','SR','SALE_SELF','0','RM015','14/8/1993','Nam','Việt Nam','Thôn Tây Hóa, Mai Hóa, Tuyên Hóa, Quảng Bình','044093008958','12/16/2022','CTCCSQLHCVTTXH'),
('NV019','Phan Hồng Sơn','GL','R03','SR','SALE_SELF','0','RM015','10/9/1998','Nam','Việt Nam','Thôn 8 Trường Xuân, Đăk Song, Đăk Nông','070098005298','5/19/2023','CTCCSQLHCVTTXH'),
('NV020','Lê Văn Sơn','GL','R03','SR','SALE_SELF','0','RM015','5/14/2001','Nam','Việt Nam','Thôn 10 B Cư A Mung, Ea H''leo, Đăk Lăk','066201010469','6/28/2021','CTCCSQLHCVTTXH'),
('NV021','Lê Hồng Phi','DKN','R03','SR','SALE_SELF','0','RM015','14/10/1992','Nam','Việt Nam','Thôn 4 Đức Sơn, Anh Sơn, Nghệ An','040092024169','3/1/2023','CTCCSQLHCVTTXH'),
('NV022','Đoàn Xuân Trí','DKN','R03','SR','SALE_SELF','0','RM015','18/03/1998','Nam','Việt Nam','Tổ dân phố 3, Thị trấn Kiến Đức, Đăk R'' Lấp, Đăk Nông','067098005938','7/3/2021','CTCCSQLHCVTTXH'),
('KT023','Nguyễn Văn Sự','GL','R03','TA','TECH_PROV',NULL,NULL,'9/9/1989','Nam','Việt Nam','Đoàn Kết, Ayun Hạ, Phú Thiện, Gia Lai','035089011340','2/14/2022','CTCCSQLHCVTTXH'),
('KT024','Huỳnh Đức','DKL','R03','TA','TECH_PROV',NULL,NULL,'8/30/1999','Nam','Việt Nam','Thôn 6, Hòa Tân, Krông Bông, Đăk Lăk','066099004250','12/27/2021','CTCCSQLHCVTTXH'),
('KT025','Đinh Quốc Vinh','DKN','R03','TA','TECH_PROV',NULL,NULL,'8/20/1988','Nam','Việt Nam','TDP2, Nghĩa Tân, Thành phố Gia Nghĩa, Đắk Nông','066088007505','9/1/2021','CTCCSQLHCVTTXH'),
('KT026','Lê Hữu Trung','LD','R03','TA','TECH_PROV',NULL,NULL,'6/30/1981','Nam','Việt Nam','Aấp Thới Thuận A, Thị Trấn Thới Lai, Thới Lai, Cần Thơ','092081002785','1/20/2022','CTCCSQLHCVTTXH'),
('ASM029','Lê Ngọc Nhân',NULL,'R04','ASM','MGR_PROV',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
('KT030','Trần Văn Hào','KG','R04','TA','TECH_PROV',NULL,NULL,'1/1/1995','Nam','Việt Nam','Trung Bình Nhất, Vĩnh Trạch, Thoại Sơn, An Giang','089095013549','6/9/2021','CTCCSQLHCVTTXH'),
('KT031','Nguyễn Văn Mãi','BP','R01','TA','TECH_PROV',NULL,NULL,'7/16/1983','Nam','Việt Nam','Tổ dân phố 5, Thị Trấn Kiến Đức, Đăk R''lâp, Đăk Nông','075083017511','12/27/2021','CTCCSQLHCVTTXH'),
('ASM032','Nguyễn Tấn Lộc',NULL,'R04','ASM','MGR_PROV',NULL,NULL,'12/30/1984','Nam','Việt Nam','Ấp Phú Hữu, xã Phú Hoà. Tỉnh An Giang','089084013690','1/6/2025','Bộ Công An'),
('RM023','Bùi Cao Cường',NULL,'R05','RM','MGR_REGION',NULL,NULL,'6/8/1983','Nam','Việt Nam','Phượng Trì, Thị Trấn Phùng, Đan Phượng, Hà Nội','001083019981','4/25/2021','CTCCSQLHCVTTXH'),
('NV024','Đỗ Trung Kiên',NULL,'R05','SR','SALE_SELF',NULL,NULL,'27/10/2001','Nam','Việt Nam','Xóm chợ Xanh Khánh Thiện, Yên Khánh, Ninh Bình','037201002142','6/28/2021','CTCCSQLHCVTTXH');

-- =====================================================
-- 3) CLEANUP DATA
-- =====================================================
UPDATE employee_import_staging
SET asm_map_emp = NULL
WHERE asm_map_emp IN ('0', '0.0', 'NULL', 'null', '');

UPDATE employee_import_staging
SET rm_map_emp = NULL
WHERE rm_map_emp IN ('0', '0.0', 'NULL', 'null', '');

UPDATE employee_import_staging
SET ma_tinh = NULL
WHERE ma_tinh IN ('', 'NULL', 'null');

-- =====================================================
-- 4) IMPORT ROLES
-- =====================================================
INSERT INTO roles (
    code, name, type, description, status, created_at, updated_at
)
SELECT DISTINCT
    ma_cv,
    CASE
        WHEN ma_cv = 'SALE_SELF' THEN 'Sale'
        WHEN ma_cv = 'MGR_PROV' THEN 'ASM'
        WHEN ma_cv = 'MGR_REGION' THEN 'RM'
        WHEN ma_cv = 'TECH_PROV' THEN 'Kỹ thuật'
        ELSE COALESCE(chuc_vu, ma_cv)
    END,
    CASE
        WHEN ma_cv = 'SALE_SELF' THEN 'SALE'
        WHEN ma_cv IN ('MGR_PROV', 'MGR_REGION') THEN 'MANAGER'
        WHEN ma_cv = 'TECH_PROV' THEN 'TECH'
        ELSE 'OTHER'
    END,
    CONCAT('Imported from employee CSV - ', COALESCE(chuc_vu, ma_cv)),
    1,
    NOW(),
    NOW()
FROM employee_import_staging
WHERE ma_cv IS NOT NULL;

-- =====================================================
-- 5) IMPORT REGIONS
-- =====================================================
INSERT INTO regions (
    code, name, status, created_at, updated_at
)
SELECT DISTINCT
    ma_kv,
    ma_kv,
    1,
    NOW(),
    NOW()
FROM employee_import_staging
WHERE ma_kv IS NOT NULL;

-- =====================================================
-- 6) IMPORT PROVINCES
-- =====================================================
INSERT INTO provinces (
    region_id, code, name, status, created_at, updated_at
)
SELECT
    r.id,
    x.ma_tinh,
    x.ma_tinh,
    1,
    NOW(),
    NOW()
FROM (
    SELECT
        ma_tinh,
        MIN(ma_kv) AS ma_kv
    FROM employee_import_staging
    WHERE ma_tinh IS NOT NULL
      AND ma_kv IS NOT NULL
    GROUP BY ma_tinh
) x
JOIN regions r ON r.code = x.ma_kv;

-- =====================================================
-- 7) IMPORT EMPLOYEES
-- =====================================================
INSERT INTO employees (
    code,
    name,
    tax_code,
    dependent_count,
    insurance_base,
    is_union_member,
    status,
    joined_at,
    left_at,
    created_at,
    updated_at
)
SELECT
    emp_id,
    MAX(full_name) AS name,
    NULL,
    0,
    0.00,
    0,
    1,
    NULL,
    NULL,
    NOW(),
    NOW()
FROM employee_import_staging
WHERE emp_id IS NOT NULL
GROUP BY emp_id;

-- =====================================================
-- 8) IMPORT EMPLOYEE_SCOPES
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
    CASE
        WHEN ro.code = 'MGR_REGION' THEN NULL
        ELSE p.id
    END AS province_id,
    CASE
        WHEN ro.code IN ('SALE_SELF', 'TECH_PROV') THEN 1
        ELSE 0
    END AS is_personal_target,
    CASE
        WHEN ro.code IN ('MGR_PROV', 'MGR_REGION') THEN 1
        ELSE 0
    END AS is_manager_target,
    '2026-01-01',
    NULL,
    1,
    NOW(),
    NOW()
FROM employee_import_staging s
JOIN employees e ON e.code = s.emp_id
JOIN roles ro ON ro.code = s.ma_cv
LEFT JOIN regions rg ON rg.code = s.ma_kv
LEFT JOIN provinces p ON p.code = s.ma_tinh;

-- =====================================================
-- 9) IMPORT MANAGER_MAPPINGS
-- =====================================================
INSERT INTO manager_mappings (
    period,
    sales_employee_id,
    asm_employee_id,
    rm_employee_id,
    region_id,
    province_id,
    created_at,
    updated_at
)
SELECT
    '202601' AS period,
    sales_emp.id AS sales_employee_id,
    asm_emp.id AS asm_employee_id,
    rm_emp.id AS rm_employee_id,
    rg.id AS region_id,
    p.id AS province_id,
    NOW(),
    NOW()
FROM employee_import_staging s
JOIN employees sales_emp ON sales_emp.code = s.emp_id
JOIN roles sales_role ON sales_role.code = s.ma_cv
LEFT JOIN employees asm_emp ON asm_emp.code = s.asm_map_emp
LEFT JOIN employees rm_emp ON rm_emp.code = s.rm_map_emp
LEFT JOIN regions rg ON rg.code = s.ma_kv
LEFT JOIN provinces p ON p.code = s.ma_tinh
WHERE sales_role.code = 'SALE_SELF';

-- =====================================================
-- 10) CHECK DATA
-- =====================================================
SELECT 'roles' AS table_name, COUNT(*) AS total_rows FROM roles
UNION ALL
SELECT 'regions', COUNT(*) FROM regions
UNION ALL
SELECT 'provinces', COUNT(*) FROM provinces
UNION ALL
SELECT 'employees', COUNT(*) FROM employees
UNION ALL
SELECT 'employee_scopes', COUNT(*) FROM employee_scopes
UNION ALL
SELECT 'manager_mappings', COUNT(*) FROM manager_mappings;

COMMIT;
SET FOREIGN_KEY_CHECKS = 1;