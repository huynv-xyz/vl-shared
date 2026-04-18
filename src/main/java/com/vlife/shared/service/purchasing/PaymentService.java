package com.vlife.shared.service.purchasing;

import com.vlife.shared.exception.BusinessException;
import com.vlife.shared.jdbc.dao.purchasing.PaymentDao;
import com.vlife.shared.jdbc.entity.purchasing.Payment;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Singleton
public class PaymentService {

    private final PaymentDao paymentDao;
    private final ContractService contractService;

    public PaymentService(PaymentDao paymentDao,
                          ContractService contractService) {
        this.paymentDao = paymentDao;
        this.contractService = contractService;
    }

    @Transactional
    public Payment create(Payment payment) {

        validateAmount(payment, null);

        return paymentDao.insert(payment);
    }

    @Transactional
    public Payment update(Integer id, Payment payment) {

        validateAmount(payment, id);

        payment.setId(id);
        paymentDao.updateSelective(id, payment);

        return payment;
    }

    private void validateAmount(Payment payment, Integer excludeId) {

        Integer contractId = payment.getContractId();

        if (contractId == null) return;

        // ===== tổng giá trị thực =====
        ContractService.ContractSummary summary =
                contractService.calcSummaryByContractId(contractId);

        BigDecimal realAmount = summary.getRealAmount();

        // ===== tổng đã thanh toán =====
        List<Payment> payments = paymentDao.findByContractId(contractId);

        BigDecimal paid = BigDecimal.ZERO;

        for (Payment p : payments) {
            if (excludeId != null && p.getId().equals(excludeId)) continue;
            paid = paid.add(nvl(p.getAmount()));
        }

        BigDecimal newAmount = nvl(payment.getAmount());

        BigDecimal total = paid.add(newAmount);

        // ===== CHECK 🔥 =====
        if (total.compareTo(realAmount) > 0) {
            throw new BusinessException(
                    -1,
                    "Thanh toán vượt giá trị thực nhận",
                    java.util.Map.of(
                            "contract_id", contractId,
                            "real_amount", realAmount,
                            "paid", paid,
                            "new_amount", newAmount,
                            "total", total
                    )
            );
        }
    }

    private BigDecimal nvl(BigDecimal x) {
        return x != null ? x : BigDecimal.ZERO;
    }
}