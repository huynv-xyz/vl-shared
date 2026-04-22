ALTER TABLE `contracts`

-- Hình thức thanh toán (VD: TT 30-70, LC, DP...)
ADD COLUMN `payment_method` VARCHAR(100)
    COMMENT 'Hình thức thanh toán',

-- Điều kiện giao hàng (Incoterm: EXW, FOB, CIF...)
ADD COLUMN `term` VARCHAR(50)
    COMMENT 'Điều kiện giao hàng (Incoterm)';