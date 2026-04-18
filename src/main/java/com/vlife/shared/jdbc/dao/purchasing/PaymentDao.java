package com.vlife.shared.jdbc.dao.purchasing;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.purchasing.Payment;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class PaymentDao extends BaseDao<Payment, Integer> {

    public PaymentDao(JdbcClient jdbc) {
        super(jdbc, Payment.class);
    }

    public Page<Payment> search(
            @Nullable Integer contractId,
            @Nullable String type,
            Pageable pageable
    ) {
        var sb = SqlBuilder.where();

        if (contractId != null) {
            sb.eq("contract_id", "contractId", contractId);
        }

        if (type != null && !type.isBlank()) {
            sb.eq("type", "type", type.trim());
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public List<Payment> findByContractId(Integer contractId) {
        return queryList(
                SqlBuilder.where()
                        .eq("contract_id", "contractId", contractId)
        );
    }
}