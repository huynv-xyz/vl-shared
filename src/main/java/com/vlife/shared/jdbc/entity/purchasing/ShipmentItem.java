package com.vlife.shared.jdbc.entity.purchasing;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlife.shared.jdbc.entity.base.AuditEntity;
import com.vlife.shared.jdbc.entity.base.Identifiable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.model.naming.NamingStrategies;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.naming.SnakeCaseStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "shipment_items", namingStrategy = NamingStrategies.Raw.class)
public class ShipmentItem extends AuditEntity  implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("shipment_id")
    private Integer shipmentId;

    @MappedProperty("product_id")
    private Integer productId;

    private BigDecimal quantity;

    @MappedProperty("defect_quantity")
    private BigDecimal defectQuantity;

    @MappedProperty("unit_price")
    private BigDecimal unitPrice;

    @Override public Integer getId() { return id; }
    @Override public void setId(Integer id) { this.id = id; }
}