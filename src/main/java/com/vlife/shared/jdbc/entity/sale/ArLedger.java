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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "ar_ledger", namingStrategy = NamingStrategies.Raw.class)
public class ArLedger implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("posting_date")
    private LocalDate postingDate;

    @MappedProperty("customer_id")
    private Integer customerId;

    @MappedProperty("doc_type")
    private String docType;

    @MappedProperty("doc_no")
    private String docNo;

    @MappedProperty("order_id")
    private Integer orderId;

    @MappedProperty("export_id")
    private Integer exportId;

    @MappedProperty("product_id")
    private Integer productId;

    private BigDecimal quantity;

    @MappedProperty("debit_amount")
    private BigDecimal debitAmount;

    @MappedProperty("credit_amount")
    private BigDecimal creditAmount;

    @MappedProperty("created_at")
    private LocalDateTime createdAt;

    @Override public Integer getId() { return id; }
    @Override public void setId(Integer id) { this.id = id; }
}