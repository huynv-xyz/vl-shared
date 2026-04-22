package com.vlife.shared.jdbc.entity.sale;

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

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "orders", namingStrategy = NamingStrategies.Raw.class)
public class Order extends AuditEntity implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("order_no")
    private String orderNo;

    @MappedProperty("customer_id")
    private Integer customerId;

    @MappedProperty("employee_id")
    private Integer employeeId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("order_date")
    private LocalDate orderDate;

    private String status;

    private String note;

    @Override public Integer getId() { return id; }
    @Override public void setId(Integer id) { this.id = id; }
}