package com.vlife.shared.dto.salarysale;

public class PayrollConfig {

    private Double salaryRatio;
    private Double bonusRatio;
    private Double basicSalaryRatio;
    private Double allowanceRatio;
    private Double minCompletionForBonus;
    private Double minDebtRateForBonus;
    private Double insuranceRate;
    private Double unionFeeRate;

    public Double getSalaryRatio() {
        return salaryRatio;
    }

    public void setSalaryRatio(Double salaryRatio) {
        this.salaryRatio = salaryRatio;
    }

    public Double getBonusRatio() {
        return bonusRatio;
    }

    public void setBonusRatio(Double bonusRatio) {
        this.bonusRatio = bonusRatio;
    }

    public Double getBasicSalaryRatio() {
        return basicSalaryRatio;
    }

    public void setBasicSalaryRatio(Double basicSalaryRatio) {
        this.basicSalaryRatio = basicSalaryRatio;
    }

    public Double getAllowanceRatio() {
        return allowanceRatio;
    }

    public void setAllowanceRatio(Double allowanceRatio) {
        this.allowanceRatio = allowanceRatio;
    }

    public Double getMinCompletionForBonus() {
        return minCompletionForBonus;
    }

    public void setMinCompletionForBonus(Double minCompletionForBonus) {
        this.minCompletionForBonus = minCompletionForBonus;
    }

    public Double getMinDebtRateForBonus() {
        return minDebtRateForBonus;
    }

    public void setMinDebtRateForBonus(Double minDebtRateForBonus) {
        this.minDebtRateForBonus = minDebtRateForBonus;
    }

    public Double getInsuranceRate() {
        return insuranceRate;
    }

    public void setInsuranceRate(Double insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    public Double getUnionFeeRate() {
        return unionFeeRate;
    }

    public void setUnionFeeRate(Double unionFeeRate) {
        this.unionFeeRate = unionFeeRate;
    }
}