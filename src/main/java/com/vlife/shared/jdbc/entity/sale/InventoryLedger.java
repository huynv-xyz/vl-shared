package com.vlife.shared.jdbc.entity.sale;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlife.shared.jdbc.entity.base.Identifiable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.naming.NamingStrategies;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.naming.SnakeCaseStrategy;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "inventory_ledger", namingStrategy = NamingStrategies.Raw.class)
public class InventoryLedger implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("product_id")
    private Integer productId;

    @MappedProperty("warehouse_id")
    private Integer warehouseId;

    @MappedProperty("quantity_change")
    private BigDecimal quantityChange; // + nhập / - xuất

    @MappedProperty("ref_type")
    private String refType; // EXPORT / IMPORT / RETURN

    @MappedProperty("ref_id")
    private Integer refId;

    @MappedProperty("created_at")
    private LocalDateTime createdAt;

    @Override public Integer getId() { return id; }
    @Override public void setId(Integer id) { this.id = id; }
}