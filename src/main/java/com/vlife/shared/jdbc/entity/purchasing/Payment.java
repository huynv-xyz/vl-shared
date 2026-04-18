package com.vlife.shared.jdbc.entity.purchasing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlife.shared.jdbc.entity.base.Identifiable;
import io.micronaut.data.annotation.*;
import io.micronaut.data.model.naming.NamingStrategies;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.naming.SnakeCaseStrategy;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "payments", namingStrategy = NamingStrategies.Raw.class)
public class Payment implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("contract_id")
    private Integer contractId;

    @MappedProperty("shipment_id")
    private Integer shipmentId;

    private String type;

    private BigDecimal amount;

    @MappedProperty("exchange_rate")
    private BigDecimal exchangeRate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("paid_at")
    private LocalDate paidAt;

    private String note;

    @MappedProperty("created_at")
    private LocalDateTime createdAt;

    @MappedProperty("created_by")
    private Integer createdBy;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}