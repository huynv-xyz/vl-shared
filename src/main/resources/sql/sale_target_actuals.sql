SELECT
    t.sale_user_code,
    t.process_month AS period,

    SUM(
        CASE
            WHEN LOWER(TRIM(t.unit)) = 'tấn' THEN (t.sale_qty - t.return_qty) * 1000
            WHEN LOWER(TRIM(t.unit)) = 'kg' THEN (t.sale_qty - t.return_qty)
            WHEN LOWER(TRIM(t.unit)) = 'lít' THEN (t.sale_qty - t.return_qty)
            ELSE (t.sale_qty - t.return_qty)
        END
    ) AS actual_main,

    SUM(CASE
        WHEN UPPER(TRIM(t.vthh_group_name)) LIKE 'BON_GOC%'
        THEN
            CASE
                WHEN LOWER(TRIM(t.unit)) = 'tấn' THEN (t.sale_qty - t.return_qty) * 1000
                ELSE (t.sale_qty - t.return_qty)
            END
        ELSE 0 END) AS actual_bon_goc,

    SUM(CASE
        WHEN UPPER(TRIM(t.vthh_group_name)) LIKE 'BON_LA_B%'
        THEN
            CASE
                WHEN LOWER(TRIM(t.unit)) = 'tấn' THEN (t.sale_qty - t.return_qty) * 1000
                ELSE (t.sale_qty - t.return_qty)
            END
        ELSE 0 END) AS actual_bon_la_bot,

    SUM(CASE
        WHEN UPPER(TRIM(t.vthh_group_name)) LIKE 'BON_LA_L%'
        THEN
            CASE
                WHEN LOWER(TRIM(t.unit)) = 'tấn' THEN (t.sale_qty - t.return_qty) * 1000
                ELSE (t.sale_qty - t.return_qty)
            END
        ELSE 0 END) AS actual_bon_la_long,

    SUM(CASE
        WHEN UPPER(TRIM(t.vthh_group_name)) = 'CLCN'
        THEN
            CASE
                WHEN LOWER(TRIM(t.unit)) = 'tấn' THEN (t.sale_qty - t.return_qty) * 1000
                ELSE (t.sale_qty - t.return_qty)
            END
        ELSE 0 END) AS actual_clcn

FROM sales_transactions t
WHERE t.sale_user_code IS NOT NULL
  AND t.process_month = 1
GROUP BY t.sale_user_code, t.process_month
ORDER BY t.sale_user_code;