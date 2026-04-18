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

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@MappedEntity(value = "sales_transactions", namingStrategy = NamingStrategies.Raw.class)
public class SalesTransaction implements Identifiable<Integer> {

    @Id
    @GeneratedValue
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("posting_date")
    private LocalDateTime postingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MappedProperty("document_date")
    private LocalDateTime documentDate;

    @MappedProperty("document_no")
    private String documentNo;

    @MappedProperty("customer_code")
    private String customerCode;

    @MappedProperty("customer_name")
    private String customerName;

    @MappedProperty("description")
    private String description;

    @MappedProperty("product_code")
    private String productCode;

    @MappedProperty("product_name")
    private String productName;

    @MappedProperty("unit")
    private String unit;

    @MappedProperty("sale_qty")
    private Double saleQty;

    @MappedProperty("return_qty")
    private Double returnQty;

    @MappedProperty("sale_user_code")
    private String saleUserCode;

    @MappedProperty("sale_user_name")
    private String saleUserName;

    @MappedProperty("sale_user_title")
    private String saleUserTitle;

    @MappedProperty("contact_name")
    private String contactName;

    @MappedProperty("vthh_con")
    private String vthhCon;

    @MappedProperty("vthh_group_name")
    private String vthhGroupName;

    @MappedProperty("customer_type")
    private String customerType;

    @MappedProperty("ext_detail_2")
    private String extDetail2;

    @MappedProperty("is_gift")
    private Integer isGift;

    @MappedProperty("private_code")
    private String privateCode;

    @MappedProperty("sl_rieng_tl")
    private Double slRiengTl;

    @MappedProperty("sl_tl_nhom")
    private Double slTlNhom;

    @MappedProperty("sl_l_b2c")
    private Double slLB2c;

    @MappedProperty("sl_l_b2b")
    private Double slLB2b;

    @MappedProperty("sl_hdn")
    private Double slHdn;

    @MappedProperty("diem_hdn")
    private Double diemHdn;

    @MappedProperty("process_month")
    private Integer processMonth;

    @MappedProperty("npp")
    private String npp;

    @MappedProperty("valid_code")
    private String validCode;

    @MappedProperty("hdn_status")
    private String hdnStatus;

    @MappedProperty("common_group")
    private String commonGroup;

    @MappedProperty("region")
    private String region;

    @MappedProperty("sl_hdn_k0_ma_rieng")
    private Double slHdnK0MaRieng;

    @MappedProperty("status")
    private Integer status;

    @MappedProperty("note")
    private String note;

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

    public LocalDateTime getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDateTime postingDate) {
        this.postingDate = postingDate;
    }

    public LocalDateTime getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDateTime documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(Double saleQty) {
        this.saleQty = saleQty;
    }

    public Double getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(Double returnQty) {
        this.returnQty = returnQty;
    }

    public String getSaleUserCode() {
        return saleUserCode;
    }

    public void setSaleUserCode(String saleUserCode) {
        this.saleUserCode = saleUserCode;
    }

    public String getSaleUserName() {
        return saleUserName;
    }

    public void setSaleUserName(String saleUserName) {
        this.saleUserName = saleUserName;
    }

    public String getSaleUserTitle() {
        return saleUserTitle;
    }

    public void setSaleUserTitle(String saleUserTitle) {
        this.saleUserTitle = saleUserTitle;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getVthhCon() {
        return vthhCon;
    }

    public void setVthhCon(String vthhCon) {
        this.vthhCon = vthhCon;
    }

    public String getVthhGroupName() {
        return vthhGroupName;
    }

    public void setVthhGroupName(String vthhGroupName) {
        this.vthhGroupName = vthhGroupName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getExtDetail2() {
        return extDetail2;
    }

    public void setExtDetail2(String extDetail2) {
        this.extDetail2 = extDetail2;
    }

    public Integer getIsGift() {
        return isGift;
    }

    public void setIsGift(Integer isGift) {
        this.isGift = isGift;
    }

    public String getPrivateCode() {
        return privateCode;
    }

    public void setPrivateCode(String privateCode) {
        this.privateCode = privateCode;
    }

    public Double getSlRiengTl() {
        return slRiengTl;
    }

    public void setSlRiengTl(Double slRiengTl) {
        this.slRiengTl = slRiengTl;
    }

    public Double getSlTlNhom() {
        return slTlNhom;
    }

    public void setSlTlNhom(Double slTlNhom) {
        this.slTlNhom = slTlNhom;
    }

    public Double getSlLB2c() {
        return slLB2c;
    }

    public void setSlLB2c(Double slLB2c) {
        this.slLB2c = slLB2c;
    }

    public Double getSlLB2b() {
        return slLB2b;
    }

    public void setSlLB2b(Double slLB2b) {
        this.slLB2b = slLB2b;
    }

    public Double getSlHdn() {
        return slHdn;
    }

    public void setSlHdn(Double slHdn) {
        this.slHdn = slHdn;
    }

    public Double getDiemHdn() {
        return diemHdn;
    }

    public void setDiemHdn(Double diemHdn) {
        this.diemHdn = diemHdn;
    }

    public Integer getProcessMonth() {
        return processMonth;
    }

    public void setProcessMonth(Integer processMonth) {
        this.processMonth = processMonth;
    }

    public String getNpp() {
        return npp;
    }

    public void setNpp(String npp) {
        this.npp = npp;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public String getHdnStatus() {
        return hdnStatus;
    }

    public void setHdnStatus(String hdnStatus) {
        this.hdnStatus = hdnStatus;
    }

    public String getCommonGroup() {
        return commonGroup;
    }

    public void setCommonGroup(String commonGroup) {
        this.commonGroup = commonGroup;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getSlHdnK0MaRieng() {
        return slHdnK0MaRieng;
    }

    public void setSlHdnK0MaRieng(Double slHdnK0MaRieng) {
        this.slHdnK0MaRieng = slHdnK0MaRieng;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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