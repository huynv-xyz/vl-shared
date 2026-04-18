START TRANSACTION;

-- =========================================================
-- CLEAN SAMPLE DATA
-- =========================================================

DELETE FROM `extra_bonus_allocations`;
DELETE FROM `extra_bonus_audits`;
DELETE FROM `payroll_employee_results`;
DELETE FROM `payroll_scope_results`;
DELETE FROM `payroll_run_errors`;
DELETE FROM `payroll_runs`;

DELETE FROM `manager_mappings`;
DELETE FROM `sales_actuals`;
DELETE FROM `sales_targets`;
DELETE FROM `employee_scopes`;
DELETE FROM `role_rates`;
DELETE FROM `employees`;
DELETE FROM `provinces`;
DELETE FROM `regions`;

-- Giữ lại roles / config_parameters / progressive_bonus_tiers / bonus_split_rules / tax_brackets
-- vì bạn đã seed sẵn trong file schema

-- =========================================================
-- REGIONS
-- =========================================================

INSERT INTO `regions` (`id`, `code`, `name`, `status`, `created_at`, `updated_at`) VALUES
(1, 'R01', 'Tay Nguyen', 1, NOW(), NOW()),
(2, 'R02', 'Dong Nam Bo', 1, NOW(), NOW());

-- =========================================================
-- PROVINCES
-- =========================================================

INSERT INTO `provinces` (`id`, `region_id`, `code`, `name`, `status`, `created_at`, `updated_at`) VALUES
(1, 1, 'DLK', 'Dak Lak', 1, NOW(), NOW()),
(2, 1, 'GLA', 'Gia Lai', 1, NOW(), NOW()),
(3, 2, 'HCM', 'Ho Chi Minh', 1, NOW(), NOW());

-- =========================================================
-- EMPLOYEES
-- =========================================================

INSERT INTO `employees` (
  `id`, `code`, `name`, `tax_code`, `dependent_count`, `insurance_base`,
  `is_union_member`, `status`, `joined_at`, `left_at`, `created_at`, `updated_at`
) VALUES
(1, 'S001', 'Nguyen Van Sale 1', 'TAX001', 1, 8000000.00, 1, 1, '2024-01-01', NULL, NOW(), NOW()),
(2, 'A001', 'Tran Thi ASM 1', 'TAX002', 0, 10000000.00, 1, 1, '2024-01-01', NULL, NOW(), NOW()),
(3, 'A002', 'Le Van ASM Sale', 'TAX003', 2, 12000000.00, 0, 1, '2024-01-01', NULL, NOW(), NOW()),
(4, 'R001', 'Pham Van RM 1', 'TAX004', 1, 15000000.00, 1, 1, '2024-01-01', NULL, NOW(), NOW()),
(5, 'R002', 'Hoang Van RM Multi', 'TAX005', 0, 18000000.00, 0, 1, '2024-01-01', NULL, NOW(), NOW());

-- =========================================================
-- ROLE RATES
-- role ids theo seed mặc định:
-- 1 = SALE_SELF
-- 2 = MGR_PROV
-- 3 = MGR_REGION
-- 4 = TECH_PROV
-- =========================================================

INSERT INTO `role_rates` (
  `id`, `role_id`, `salary_rate`, `bonus_rate`, `basic_salary_rate`,
  `allowance_rate`, `effective_from`, `effective_to`, `status`, `created_at`, `updated_at`
) VALUES
(1, 1, 0.6000, 0.7000, 0.6000, 0.4000, '2026-01-01', NULL, 1, NOW(), NOW()),
(2, 2, 0.1200, 0.0700, 0.6000, 0.4000, '2026-01-01', NULL, 1, NOW(), NOW()),
(3, 3, 0.1600, 0.1300, 0.6000, 0.4000, '2026-01-01', NULL, 1, NOW(), NOW()),
(4, 4, 0.1200, 0.1000, 0.6000, 0.4000, '2026-01-01', NULL, 1, NOW(), NOW());

-- =========================================================
-- EMPLOYEE SCOPES
-- =========================================================

INSERT INTO `employee_scopes` (
  `id`, `employee_id`, `role_id`, `region_id`, `province_id`,
  `is_personal_target`, `is_manager_target`,
  `effective_from`, `effective_to`, `status`, `created_at`, `updated_at`
) VALUES
-- S001: sale only at Dak Lak
(1, 1, 1, 1, 1, 1, 0, '2026-01-01', NULL, 1, NOW(), NOW()),

-- A001: ASM only at Dak Lak
(2, 2, 2, 1, 1, 0, 1, '2026-01-01', NULL, 1, NOW(), NOW()),

-- A002: ASM at Gia Lai + personal sale at Gia Lai
(3, 3, 2, 1, 2, 0, 1, '2026-01-01', NULL, 1, NOW(), NOW()),
(4, 3, 1, 1, 2, 1, 0, '2026-01-01', NULL, 1, NOW(), NOW()),

-- R001: RM only for Tay Nguyen region
(5, 4, 3, 1, NULL, 0, 1, '2026-01-01', NULL, 1, NOW(), NOW()),

-- R002: RM for Tay Nguyen + Dong Nam Bo + personal sale at HCM
(6, 5, 3, 1, NULL, 0, 1, '2026-01-01', NULL, 1, NOW(), NOW()),
(7, 5, 3, 2, NULL, 0, 1, '2026-01-01', NULL, 1, NOW(), NOW()),
(8, 5, 1, 2, 3, 1, 0, '2026-01-01', NULL, 1, NOW(), NOW());

-- =========================================================
-- SALES TARGETS
-- =========================================================

INSERT INTO `sales_targets` (
  `id`, `period`, `employee_id`, `role_id`, `region_id`, `province_id`,
  `target_revenue`, `target_income`, `created_at`, `updated_at`
) VALUES
-- S001 sale only
(1, '202603', 1, 1, 1, 1, 100000000.00, 20000000.00, NOW(), NOW()),

-- A001 asm only
(2, '202603', 2, 2, 1, 1, 150000000.00, 12000000.00, NOW(), NOW()),

-- A002 asm + sale
(3, '202603', 3, 2, 1, 2, 180000000.00, 14000000.00, NOW(), NOW()),
(4, '202603', 3, 1, 1, 2,  80000000.00, 18000000.00, NOW(), NOW()),

-- R001 rm only
(5, '202603', 4, 3, 1, NULL, 400000000.00, 25000000.00, NOW(), NOW()),

-- R002 rm multi + personal
(6, '202603', 5, 3, 1, NULL, 500000000.00, 30000000.00, NOW(), NOW()),
(7, '202603', 5, 3, 2, NULL, 350000000.00, 28000000.00, NOW(), NOW()),
(8, '202603', 5, 1, 2, 3,   90000000.00, 22000000.00, NOW(), NOW());

-- =========================================================
-- SALES ACTUALS
-- =========================================================

INSERT INTO `sales_actuals` (
  `id`, `period`, `employee_id`, `region_id`, `province_id`,
  `actual_revenue`, `debt_rate`, `created_at`, `updated_at`
) VALUES
-- S001: đủ bonus
(1, '202603', 1, 1, 1, 120000000.00, 0.0200, NOW(), NOW()),

-- A001: không đủ bonus vì completion < 1.0
(2, '202603', 2, 1, 1, 120000000.00, 0.0100, NOW(), NOW()),

-- A002 asm + sale đều đủ
(3, '202603', 3, 1, 2, 200000000.00, 0.0300, NOW(), NOW()),

-- R001 đủ
(4, '202603', 4, 1, NULL, 450000000.00, 0.0100, NOW(), NOW()),

-- R002 region 1 đủ
(5, '202603', 5, 1, NULL, 550000000.00, 0.0200, NOW(), NOW()),

-- R002 region 2 không đủ bonus
(6, '202603', 5, 2, NULL, 300000000.00, 0.0200, NOW(), NOW()),

-- R002 personal sale đủ
(7, '202603', 5, 2, 3, 100000000.00, 0.0200, NOW(), NOW());

-- =========================================================
-- MANAGER MAPPINGS
-- phục vụ cho bước extra bonus sau này
-- =========================================================

INSERT INTO `manager_mappings` (
  `id`, `period`, `sales_employee_id`, `asm_employee_id`, `rm_employee_id`,
  `region_id`, `province_id`, `created_at`, `updated_at`
) VALUES
-- S001 sale tại Dak Lak, có ASM A001, RM R001
(1, '202603', 1, 2, 4, 1, 1, NOW(), NOW()),

-- A002 personal sale tại Gia Lai, không có ASM phía trên, RM R001
(2, '202603', 3, NULL, 4, 1, 2, NOW(), NOW()),

-- R002 personal sale tại HCM, không có ASM, RM chính là R002
(3, '202603', 5, NULL, 5, 2, 3, NOW(), NOW());

COMMIT;