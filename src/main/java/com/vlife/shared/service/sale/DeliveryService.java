package com.vlife.shared.service.sale;

import com.vlife.shared.jdbc.dao.sale.*;
import com.vlife.shared.jdbc.entity.sale.*;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class DeliveryService {

    private final DeliveryDao deliveryDao;
    private final DeliveryItemDao deliveryItemDao;
    private final ExportDao exportDao;
    private final ExportItemDao exportItemDao;
    private final OrderItemDao orderItemDao;
    private final InventoryLedgerDao inventoryLedgerDao;
    private final ArLedgerDao arLedgerDao;

    public DeliveryService(
            DeliveryDao deliveryDao,
            DeliveryItemDao deliveryItemDao,
            ExportDao exportDao,
            ExportItemDao exportItemDao,
            OrderItemDao orderItemDao,
            InventoryLedgerDao inventoryLedgerDao,
            ArLedgerDao arLedgerDao
    ) {
        this.deliveryDao = deliveryDao;
        this.deliveryItemDao = deliveryItemDao;
        this.exportDao = exportDao;
        this.exportItemDao = exportItemDao;
        this.orderItemDao = orderItemDao;
        this.inventoryLedgerDao = inventoryLedgerDao;
        this.arLedgerDao = arLedgerDao;
    }

    // =========================================================
    // CONFIRM DELIVERY
    // =========================================================
    @Transactional
    public void confirmDelivery(Integer deliveryId) {

        // ===== 1. LOAD DELIVERY
        Delivery d = deliveryDao.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        if ("DONE".equals(d.getStatus())) {
            throw new RuntimeException("Delivery already confirmed");
        }

        if (d.getWarehouseId() == null) {
            throw new RuntimeException("warehouse_id is required");
        }

        // ===== 2. LOAD ITEMS
        List<DeliveryItem> items = deliveryItemDao.findByDeliveryId(deliveryId);

        if (items.isEmpty()) {
            throw new RuntimeException("Delivery items empty");
        }

        // ===== 3. CHECK EXPORT EXIST
        if (exportDao.existsByDeliveryId(deliveryId)) {
            throw new RuntimeException("Delivery already exported");
        }

        // ===== 4. CHECK KHÔNG VƯỢT ORDER
        Map<Integer, BigDecimal> orderedMap =
                orderItemDao.findByOrderId(d.getOrderId())
                        .stream()
                        .collect(Collectors.toMap(
                                OrderItem::getProductId,
                                OrderItem::getQuantity
                        ));

        Map<Integer, BigDecimal> exportedMap =
                Optional.ofNullable(exportItemDao.sumExportedByOrderId(d.getOrderId()))
                        .orElse(Collections.emptyMap());

        for (DeliveryItem di : items) {

            BigDecimal ordered = orderedMap.getOrDefault(di.getProductId(), BigDecimal.ZERO);
            BigDecimal exported = exportedMap.getOrDefault(di.getProductId(), BigDecimal.ZERO);

            if (exported.add(di.getQuantity()).compareTo(ordered) > 0) {
                throw new RuntimeException("Vượt số lượng đơn hàng product_id=" + di.getProductId());
            }
        }

        // ===== 5. INSERT EXPORT
        Export e = new Export();

        e.setExportNo(generateExportNo());
        e.setExportDate(LocalDate.now());

        e.setDeliveryId(d.getId());
        e.setOrderId(d.getOrderId());
        e.setWarehouseId(d.getWarehouseId());

        e.setStatus("DONE");
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());

        e = exportDao.insert(e);

        // ===== 6. EXPORT ITEMS
        List<ExportItem> exportItems = new ArrayList<>();

        for (DeliveryItem di : items) {
            ExportItem ei = new ExportItem();

            ei.setExportId(e.getId());
            ei.setProductId(di.getProductId());
            ei.setQuantity(di.getQuantity());
            ei.setNote(di.getNote());

            exportItems.add(ei);
        }

        exportItemDao.saveAll(exportItems);

        // ===== 7. INVENTORY LEDGER
        for (ExportItem ei : exportItems) {

            InventoryLedger ledger = new InventoryLedger();

            ledger.setProductId(ei.getProductId());
            ledger.setWarehouseId(d.getWarehouseId());
            ledger.setQuantityChange(ei.getQuantity().negate());

            ledger.setRefType("EXPORT");
            ledger.setRefId(e.getId());

            ledger.setCreatedAt(LocalDateTime.now());

            inventoryLedgerDao.insert(ledger);
        }

        // ===== 8. AR LEDGER
        Map<Integer, BigDecimal> priceMap =
                orderItemDao.findByOrderId(d.getOrderId())
                        .stream()
                        .collect(Collectors.toMap(
                                OrderItem::getProductId,
                                OrderItem::getUnitPrice
                        ));

        for (ExportItem ei : exportItems) {

            BigDecimal price = priceMap.getOrDefault(ei.getProductId(), BigDecimal.ZERO);
            BigDecimal amount = ei.getQuantity().multiply(price);

            ArLedger ar = new ArLedger();

            ar.setPostingDate(LocalDate.now());
            ar.setCustomerId(d.getCompanyId()); // ⚠️ phải có

            ar.setDocType("BAN_HANG");
            ar.setDocNo(e.getExportNo());

            ar.setOrderId(d.getOrderId());
            ar.setExportId(e.getId()); // ✅ đúng field

            ar.setProductId(ei.getProductId());
            ar.setQuantity(ei.getQuantity());

            ar.setDebitAmount(amount);
            ar.setCreditAmount(BigDecimal.ZERO);

            ar.setCreatedAt(LocalDateTime.now());

            arLedgerDao.insert(ar);
        }

        // ===== 9. UPDATE DELIVERY
        Delivery update = new Delivery();
        update.setStatus("DONE");
        update.setUpdatedAt(LocalDateTime.now());

        deliveryDao.updateSelective(deliveryId, update);
    }

    // =========================================================
    // GENERATE EXPORT NO
    // =========================================================
    private String generateExportNo() {

        String prefix = "PX-" + LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

        Integer count = exportDao.countByPrefix(prefix);
        int next = count + 1;

        return prefix + "-" + String.format("%03d", next);
    }
}