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
@MappedEntity(value = "order_items", namingStrategy = NamingStrategies.Raw.class)
public class OrderItem implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("order_id")
    private Integer orderId;

    @MappedProperty("product_id")
    private Integer productId;

    private BigDecimal quantity;

    @MappedProperty("unit_price")
    private BigDecimal unitPrice;

    private BigDecimal discount;

    @MappedProperty("line_type")
    private String lineType;

    @Override public Integer getId() { return id; }
    @Override public void setId(Integer id) { this.id = id; }
}