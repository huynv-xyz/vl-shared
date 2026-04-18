package com.vlife.shared.service.purchasing;

import com.vlife.shared.jdbc.dao.purchasing.*;
import com.vlife.shared.jdbc.entity.purchasing.*;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class ContractService {

    private final ContractItemDao contractItemDao;
    private final ShipmentDao shipmentDao;
    private final ShipmentItemDao shipmentItemDao;
    private final PaymentDao paymentDao;
    private final ContractDao contractDao;

    public ContractService(ContractItemDao contractItemDao,
                           ShipmentDao shipmentDao,
                           ShipmentItemDao shipmentItemDao,
                           PaymentDao paymentDao,
                           ContractDao contractDao) {
        this.contractItemDao = contractItemDao;
        this.shipmentDao = shipmentDao;
        this.shipmentItemDao = shipmentItemDao;
        this.paymentDao = paymentDao;
        this.contractDao = contractDao;
    }

    // ========================
    // API ENTRY
    // ========================
    public ContractSummary calcSummaryByContractId(Integer contractId) {

        if (contractId == null) {
            return ContractSummary.empty();
        }

        List<ContractItem> contractItems =
                contractItemDao.findByContractId(contractId);

        List<ShipmentItem> shipmentItems =
                getShipmentItems(contractId);

        List<Payment> payments =
                paymentDao.findByContractId(contractId);

        Contract contract =
                contractDao.findById(contractId).orElse(null);

        BigDecimal vatRate =
                contract != null ? nvl(contract.getVatRate()) : BigDecimal.ZERO;

        BigDecimal importTaxRate =
                contract != null ? nvl(contract.getImportTaxRate()) : BigDecimal.ZERO;

        return calcSummary(
                contractItems,
                shipmentItems,
                payments,
                vatRate,
                importTaxRate
        );
    }

    // ========================
    // CORE CALCULATION
    // ========================
    public ContractSummary calcSummary(List<ContractItem> contractItems,
                                       List<ShipmentItem> shipmentItems,
                                       List<Payment> payments,
                                       BigDecimal vatRate,
                                       BigDecimal importTaxRate) {

        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        // ========================
        // PLAN (🔥 có VAT + import + discount)
        // ========================
        for (ContractItem i : contractItems) {

            BigDecimal qty = nvl(i.getQuantity());
            BigDecimal unit = nvl(i.getUnitPrice());
            BigDecimal discount = nvl(i.getDiscountAmount());

            BigDecimal finalPrice =
                    calcFinalUnitPrice(unit, discount, vatRate, importTaxRate);

            totalQty = totalQty.add(qty);
            totalAmount = totalAmount.add(qty.multiply(finalPrice));
        }

        // ========================
        // DEFECT (🔥 dùng giá shipment thực tế)
        // ========================
        BigDecimal totalDefectQty = BigDecimal.ZERO;
        BigDecimal totalDefectAmount = BigDecimal.ZERO;

        for (ShipmentItem i : shipmentItems) {

            BigDecimal defect = nvl(i.getDefectQuantity());

            BigDecimal base =
                    nvl(i.getUnitPrice());

            BigDecimal finalPrice =
                    calcFinalUnitPrice(base, BigDecimal.ZERO, vatRate, importTaxRate);

            totalDefectQty = totalDefectQty.add(defect);
            totalDefectAmount = totalDefectAmount.add(defect.multiply(finalPrice));
        }

        // ========================
        // PAYMENT
        // ========================
        BigDecimal totalPaid = BigDecimal.ZERO;

        for (Payment p : payments) {
            totalPaid = totalPaid.add(nvl(p.getAmount()));
        }

        BigDecimal remaining = totalAmount.subtract(totalPaid);

        return new ContractSummary(
                totalQty,
                totalAmount,
                totalDefectQty,
                totalDefectAmount,
                totalPaid,
                remaining
        );
    }

    // ========================
    // 🔥 PRICE ENGINE (core logic)
    // ========================
    private BigDecimal calcFinalUnitPrice(BigDecimal unit,
                                          BigDecimal discount,
                                          BigDecimal vatRate,
                                          BigDecimal importTaxRate) {

        // base = unit - discount
        BigDecimal base = nvl(unit).subtract(nvl(discount));

        // import tax
        BigDecimal importTax = base
                .multiply(nvl(importTaxRate))
                .divide(BigDecimal.valueOf(100));

        // VAT
        BigDecimal vat = base
                .add(importTax)
                .multiply(nvl(vatRate))
                .divide(BigDecimal.valueOf(100));

        // final
        return base.add(importTax).add(vat);
    }

    // ========================
    // LOAD SHIPMENT ITEMS
    // ========================
    private List<ShipmentItem> getShipmentItems(Integer contractId) {

        List<Shipment> shipments =
                shipmentDao.findByContractId(contractId);

        if (shipments.isEmpty()) {
            return List.of();
        }

        Set<Integer> shipmentIds = shipments.stream()
                .map(Shipment::getId)
                .collect(Collectors.toSet());

        return shipmentItemDao.findByShipmentIds(shipmentIds);
    }

    private BigDecimal nvl(BigDecimal x) {
        return x != null ? x : BigDecimal.ZERO;
    }

    // ========================
    // RESULT DTO
    // ========================
    public static class ContractSummary {

        private final BigDecimal totalQuantity;
        private final BigDecimal totalAmount;

        private final BigDecimal totalDefectQuantity;
        private final BigDecimal totalDefectAmount;

        private final BigDecimal totalPaidAmount;
        private final BigDecimal remainingAmount;

        public ContractSummary(BigDecimal totalQuantity,
                               BigDecimal totalAmount,
                               BigDecimal totalDefectQuantity,
                               BigDecimal totalDefectAmount,
                               BigDecimal totalPaidAmount,
                               BigDecimal remainingAmount) {
            this.totalQuantity = totalQuantity;
            this.totalAmount = totalAmount;
            this.totalDefectQuantity = totalDefectQuantity;
            this.totalDefectAmount = totalDefectAmount;
            this.totalPaidAmount = totalPaidAmount;
            this.remainingAmount = remainingAmount;
        }

        public static ContractSummary empty() {
            return new ContractSummary(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        public BigDecimal getTotalQuantity() { return totalQuantity; }
        public BigDecimal getTotalAmount() { return totalAmount; }

        public BigDecimal getTotalDefectQuantity() { return totalDefectQuantity; }
        public BigDecimal getTotalDefectAmount() { return totalDefectAmount; }

        public BigDecimal getTotalPaidAmount() { return totalPaidAmount; }
        public BigDecimal getRemainingAmount() { return remainingAmount; }

        public BigDecimal getRealQuantity() {
            return totalQuantity.subtract(totalDefectQuantity);
        }

        public BigDecimal getRealAmount() {
            return totalAmount.subtract(totalDefectAmount);
        }
    }
}