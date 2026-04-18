package com.vlife.shared.service.purchasing;

import com.vlife.shared.exception.BusinessException;
import com.vlife.shared.jdbc.dao.purchasing.ContractItemDao;
import com.vlife.shared.jdbc.dao.purchasing.ShipmentDao;
import com.vlife.shared.jdbc.dao.purchasing.ShipmentItemDao;
import com.vlife.shared.jdbc.entity.purchasing.ContractItem;
import com.vlife.shared.jdbc.entity.purchasing.Shipment;
import com.vlife.shared.jdbc.entity.purchasing.ShipmentItem;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ShipmentService {

    private final ShipmentDao shipmentDao;
    private final ShipmentItemDao shipmentItemDao;
    private final ContractItemDao contractItemDao;

    public ShipmentService(ShipmentDao shipmentDao,
                           ShipmentItemDao shipmentItemDao,
                           ContractItemDao contractItemDao) {
        this.shipmentDao = shipmentDao;
        this.shipmentItemDao = shipmentItemDao;
        this.contractItemDao = contractItemDao;
    }

    // ========================
    // CREATE
    // ========================
    @Transactional
    public Shipment create(Shipment shipment, List<ShipmentItem> items) {

        validateQuantity(shipment.getContractId(), items, null);

        LocalDateTime now = LocalDateTime.now();

        shipment.setCreatedAt(now);
        shipment.setUpdatedAt(now);

        Shipment saved = shipmentDao.insert(shipment);

        prepareItems(items, saved.getId(), now);

        shipmentItemDao.saveAll(items);

        return saved;
    }

    // ========================
    // UPDATE
    // ========================
    @Transactional
    public Shipment update(Integer id, Shipment shipment, List<ShipmentItem> items) {

        validateQuantity(shipment.getContractId(), items, id);

        LocalDateTime now = LocalDateTime.now();

        shipment.setId(id);
        shipment.setUpdatedAt(now);

        shipmentDao.updateSelective(id, shipment);

        shipmentItemDao.deleteByShipmentId(id);

        prepareItems(items, id, now);

        shipmentItemDao.saveAll(items);

        return shipment;
    }

    // ========================
    // 🔥 PRICE MODEL
    // ========================
    public record PriceBreakdown(
            BigDecimal discount,
            BigDecimal importTax,
            BigDecimal vat,
            BigDecimal finalPrice,
            BigDecimal total
    ) {}

    public PriceBreakdown calcPrice(
            ShipmentItem item,
            ContractItem ci,
            BigDecimal vatRate,
            BigDecimal importRate
    ) {

        BigDecimal qty = nvl(item.getQuantity());
        BigDecimal defect = nvl(item.getDefectQuantity());

        BigDecimal realQty = qty.subtract(defect);
        if (realQty.compareTo(BigDecimal.ZERO) < 0) {
            realQty = BigDecimal.ZERO;
        }

        BigDecimal unit = nvl(ci.getUnitPrice());
        BigDecimal discount = nvl(ci.getDiscountAmount());

        BigDecimal base = unit
                .subtract(discount);

        BigDecimal importTax = base
                .multiply(nvl(importRate))
                .divide(BigDecimal.valueOf(100));

        BigDecimal vat = base
                .add(importTax)
                .multiply(nvl(vatRate))
                .divide(BigDecimal.valueOf(100));

        BigDecimal finalPrice = base
                .add(importTax)
                .add(vat);

        BigDecimal total = realQty.multiply(finalPrice);

        return new PriceBreakdown(
                discount,
                importTax,
                vat,
                finalPrice,
                total
        );
    }

    public BigDecimal calcShipmentTotal(
            List<ShipmentItem> items,
            Map<Integer, ContractItem> contractItemMap,
            BigDecimal vatRate,
            BigDecimal importRate
    ) {

        BigDecimal total = BigDecimal.ZERO;

        for (ShipmentItem i : items) {
            ContractItem ci = contractItemMap.get(i.getProductId());
            if (ci == null) continue;

            total = total.add(
                    calcPrice(i, ci, vatRate, importRate).total()
            );
        }

        return total;
    }

    // ========================
    // VALIDATE QUANTITY
    // ========================
    private void validateQuantity(Integer contractId,
                                  List<ShipmentItem> newItems,
                                  Integer excludeShipmentId) {

        if (contractId == null || newItems == null || newItems.isEmpty()) return;

        Map<Integer, BigDecimal> contractQtyMap =
                contractItemDao.findByContractId(contractId).stream()
                        .collect(Collectors.toMap(
                                ContractItem::getProductId,
                                x -> nvl(x.getQuantity()),
                                BigDecimal::add
                        ));

        Map<Integer, BigDecimal> usedQtyMap = new HashMap<>();

        List<Shipment> shipments = shipmentDao.findByContractId(contractId);

        Set<Integer> shipmentIds = shipments.stream()
                .map(Shipment::getId)
                .filter(id -> excludeShipmentId == null || !id.equals(excludeShipmentId))
                .collect(Collectors.toSet());

        if (!shipmentIds.isEmpty()) {
            List<ShipmentItem> existingItems =
                    shipmentItemDao.findByShipmentIds(shipmentIds);

            for (ShipmentItem i : existingItems) {
                usedQtyMap.merge(
                        i.getProductId(),
                        nvl(i.getQuantity()),
                        BigDecimal::add
                );
            }
        }

        Map<Integer, BigDecimal> newQtyMap = new HashMap<>();

        for (ShipmentItem i : newItems) {
            newQtyMap.merge(
                    i.getProductId(),
                    nvl(i.getQuantity()),
                    BigDecimal::add
            );
        }

        for (Map.Entry<Integer, BigDecimal> entry : newQtyMap.entrySet()) {

            Integer productId = entry.getKey();

            BigDecimal contractQty = contractQtyMap.getOrDefault(productId, BigDecimal.ZERO);
            BigDecimal usedQty = usedQtyMap.getOrDefault(productId, BigDecimal.ZERO);
            BigDecimal newQty = entry.getValue();

            BigDecimal total = usedQty.add(newQty);

            if (total.compareTo(contractQty) > 0) {
                throw new BusinessException(
                        -1,
                        "Sản phẩm vượt số lượng hợp đồng",
                        Map.of(
                                "product_id", productId,
                                "contract_qty", contractQty,
                                "used_qty", usedQty,
                                "new_qty", newQty
                        )
                );
            }
        }
    }

    // ========================
    // PREPARE
    // ========================
    private void prepareItems(List<ShipmentItem> items,
                              Integer shipmentId,
                              LocalDateTime now) {

        if (items == null) return;

        for (ShipmentItem item : items) {

            if (item.getProductId() == null) {
                throw new BusinessException(-400, "product_id is required");
            }

            if (item.getQuantity() == null ||
                    item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException(-400, "quantity must > 0");
            }

            if (nvl(item.getDefectQuantity())
                    .compareTo(nvl(item.getQuantity())) > 0) {
                throw new BusinessException(-400, "defect_quantity > quantity");
            }

            item.setShipmentId(shipmentId);
            item.setCreatedAt(now);
            item.setUpdatedAt(now);
        }
    }

    private BigDecimal nvl(BigDecimal x) {
        return x != null ? x : BigDecimal.ZERO;
    }
}