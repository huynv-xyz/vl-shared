package com.vlife.shared.jdbc.entity.purchasing;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "contracts", namingStrategy = NamingStrategies.Raw.class)
public class Contract extends AuditEntity  implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    private String code;

    @MappedProperty("supplier_id")
    private Integer supplierId;

    @MappedProperty("currency_id")
    private Integer currencyId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("signed_date")
    private LocalDate signedDate;

    private String status;

    @MappedProperty("deposit_rate")
    private BigDecimal depositRate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("deposit_date")
    private LocalDate depositDate;

    @MappedProperty("vat_rate")
    private BigDecimal vatRate;

    @MappedProperty("import_tax_rate")
    private BigDecimal importTaxRate;

    @Override public Integer getId() { return id; }
    @Override public void setId(Integer id) { this.id = id; }
}