package com.vlife.shared.jdbc.entity.purchasing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlife.shared.jdbc.entity.base.AuditEntity;
import com.vlife.shared.jdbc.entity.base.Identifiable;
import io.micronaut.data.annotation.*;
import io.micronaut.data.model.naming.NamingStrategies;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.serde.config.naming.SnakeCaseStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "shipments", namingStrategy = NamingStrategies.Raw.class)
public class Shipment extends AuditEntity implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    private String code;

    @MappedProperty("contract_id")
    private Integer contractId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate etd;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eta;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ata;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("warehouse_at")
    private LocalDate warehouseAt;

    @MappedProperty("container_no")
    private String containerNo;

    @MappedProperty("destination_port_id")
    private Integer destinationPortId;

    @MappedProperty("exchange_rate")
    private BigDecimal exchangeRate;

    private String status;

    private String note;

    @Transient
    private List<ShipmentItem> items;

    @Transient
    private Port destinationPort;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}