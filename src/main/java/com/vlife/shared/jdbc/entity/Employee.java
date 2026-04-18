package com.vlife.shared.jdbc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Serdeable(naming = SnakeCaseStrategy.class)
@MappedEntity(value = "employees", namingStrategy = NamingStrategies.Raw.class)
public class Employee implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @MappedProperty("code")
    private String code;

    @MappedProperty("name")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("birth_date")
    private LocalDate birthDate;

    @MappedProperty("gender")
    private String gender;

    @MappedProperty("permanent_address")
    private String permanentAddress;

    @MappedProperty("identity_no")
    private String identityNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("identity_issue_date")
    private LocalDate identityIssueDate;

    @MappedProperty("identity_issue_place")
    private String identityIssuePlace;

    @MappedProperty("tax_code")
    private String taxCode;

    @MappedProperty("dependent_count")
    private Integer dependentCount;

    @MappedProperty("insurance_base")
    private BigDecimal insuranceBase;

    @MappedProperty("basic_salary")
    private BigDecimal basicSalary;

    @MappedProperty("allowance_salary")
    private BigDecimal allowanceSalary;

    @MappedProperty("is_union_member")
    private Integer isUnionMember;

    @MappedProperty("status")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("joined_at")
    private LocalDate joinedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("left_at")
    private LocalDate leftAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MappedProperty("updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public LocalDate getIdentityIssueDate() {
        return identityIssueDate;
    }

    public void setIdentityIssueDate(LocalDate identityIssueDate) {
        this.identityIssueDate = identityIssueDate;
    }

    public String getIdentityIssuePlace() {
        return identityIssuePlace;
    }

    public void setIdentityIssuePlace(String identityIssuePlace) {
        this.identityIssuePlace = identityIssuePlace;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Integer getDependentCount() {
        return dependentCount;
    }

    public void setDependentCount(Integer dependentCount) {
        this.dependentCount = dependentCount;
    }

    public BigDecimal getInsuranceBase() {
        return insuranceBase;
    }

    public void setInsuranceBase(BigDecimal insuranceBase) {
        this.insuranceBase = insuranceBase;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getAllowanceSalary() {
        return allowanceSalary;
    }

    public void setAllowanceSalary(BigDecimal allowanceSalary) {
        this.allowanceSalary = allowanceSalary;
    }

    public Integer getIsUnionMember() {
        return isUnionMember;
    }

    public void setIsUnionMember(Integer isUnionMember) {
        this.isUnionMember = isUnionMember;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDate joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDate getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDate leftAt) {
        this.leftAt = leftAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}