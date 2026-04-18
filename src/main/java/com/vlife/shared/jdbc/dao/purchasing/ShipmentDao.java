package com.vlife.shared.jdbc.dao.purchasing;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.jdbc.dao.base.BaseDao;
import com.vlife.shared.jdbc.entity.purchasing.Shipment;
import com.vlife.shared.jdbc.entity.purchasing.ShipmentItem;
import com.vlife.shared.jdbc.util.SqlBuilder;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
public class ShipmentDao extends BaseDao<Shipment, Integer> {

    public ShipmentDao(JdbcClient jdbc) {
        super(jdbc, Shipment.class);
    }

    public Page<Shipment> search(Integer contractId, String status, Pageable pageable) {
        var sb = SqlBuilder.where();

        if (contractId != null) {
            sb.eq("contract_id", "contractId", contractId);
        }

        if (status != null) {
            sb.eq("status", "status", status);
        }

        return queryPage(sb, pageable, "id DESC");
    }

    public List<Shipment> findByContractId(Integer contractId) {
        if (contractId == null) {
            return Collections.emptyList();
        }

        var sb = SqlBuilder.where()
                .eq("contract_id", "contractId", contractId)
                .orderBy("id DESC");

        return queryList(sb);
    }
}