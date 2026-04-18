START TRANSACTION;

-- =========================================================
-- 1) payroll_scope_results
-- thêm snapshot employee/role/region/province
-- thêm snapshot income + tách rõ 80/20
-- thêm cột mô tả actual dùng cho personal target / manager target
-- =========================================================

ALTER TABLE `payroll_scope_results`
  ADD COLUMN `employee_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `employee_id`,
  ADD COLUMN `role_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `role_id`,
  ADD COLUMN `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `role_code`,
  ADD COLUMN `region_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `region_id`,
  ADD COLUMN `region_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `region_code`,
  ADD COLUMN `province_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `province_id`,
  ADD COLUMN `province_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `province_code`,
  ADD COLUMN `income_amount` decimal(18,2) NOT NULL DEFAULT '0.00' AFTER `target_income`,
  ADD COLUMN `salary_80_amount` decimal(18,2) NOT NULL DEFAULT '0.00' AFTER `debt_rate`,
  ADD COLUMN `bonus_20_amount` decimal(18,2) NOT NULL DEFAULT '0.00' AFTER `salary_80_amount`,
  ADD COLUMN `actual_scope_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `is_bonus_eligible`,
  ADD COLUMN `actual_scope_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `actual_scope_type`;

ALTER TABLE `payroll_scope_results`
  ADD KEY `idx_employee_code` (`employee_code`),
  ADD KEY `idx_role_code` (`role_code`),
  ADD KEY `idx_region_code` (`region_code`),
  ADD KEY `idx_province_code` (`province_code`);

-- =========================================================
-- 2) payroll_employee_results
-- thêm snapshot employee_code
-- thêm các cột trung gian TNCN để audit rõ
-- =========================================================

ALTER TABLE `payroll_employee_results`
  ADD COLUMN `employee_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `employee_id`,
  ADD COLUMN `taxable_income_before_deduction` decimal(18,2) NOT NULL DEFAULT '0.00' AFTER `gross_total`,
  ADD COLUMN `personal_deduction_amount` decimal(18,2) NOT NULL DEFAULT '0.00' AFTER `taxable_income_before_deduction`,
  ADD COLUMN `dependent_deduction_amount` decimal(18,2) NOT NULL DEFAULT '0.00' AFTER `personal_deduction_amount`,
  ADD COLUMN `taxable_income` decimal(18,2) NOT NULL DEFAULT '0.00' AFTER `dependent_deduction_amount`,
  ADD COLUMN `tax_bracket_no` int DEFAULT NULL AFTER `taxable_income`,
  ADD COLUMN `tax_rate` decimal(9,4) NOT NULL DEFAULT '0.0000' AFTER `tax_bracket_no`,
  ADD COLUMN `quick_deduction_amount` decimal(18,2) NOT NULL DEFAULT '0.00' AFTER `tax_rate`;

ALTER TABLE `payroll_employee_results`
  ADD KEY `idx_employee_code` (`employee_code`),
  ADD KEY `idx_tax_bracket_no` (`tax_bracket_no`);

-- =========================================================
-- 3) extra_bonus_audits
-- thêm snapshot rule_code
-- thêm snapshot employee/region/province để audit dễ
-- =========================================================

ALTER TABLE `extra_bonus_audits`
  ADD COLUMN `sales_employee_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `sales_employee_id`,
  ADD COLUMN `asm_employee_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `asm_employee_id`,
  ADD COLUMN `rm_employee_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `rm_employee_id`,
  ADD COLUMN `bonus_split_rule_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `bonus_split_rule_id`,
  ADD COLUMN `region_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `region_id`,
  ADD COLUMN `region_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `region_code`,
  ADD COLUMN `province_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `province_id`,
  ADD COLUMN `province_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL AFTER `province_code`;

ALTER TABLE `extra_bonus_audits`
  ADD KEY `idx_sales_employee_code` (`sales_employee_code`),
  ADD KEY `idx_bonus_split_rule_code` (`bonus_split_rule_code`),
  ADD KEY `idx_region_code` (`region_code`),
  ADD KEY `idx_province_code` (`province_code`);

COMMIT;