package com.vlife.shared.dto.salarysale;

public class PayrollScopeCalcItem {

    private Integer employeeId;
    private String employeeCode;
    private String employeeName;

    private Integer roleId;
    private String roleCode;
    private String roleName;

    private Integer regionId;
    private String regionCode;
    private String regionName;

    private Integer provinceId;
    private String provinceCode;
    private String provinceName;

    private Integer isPersonalTarget;
    private Integer isManagerTarget;

    private Integer period;

    // ===== TARGET =====
    private Double targetBonGoc;
    private Double targetBonLaBot;
    private Double targetClcn;
    private Double targetBonLaLong;

    // ===== ACTUAL =====
    private Double actualMain;
    private Double actualBonGoc;
    private Double actualBonLaBot;
    private Double actualClcn;
    private Double actualBonLaLong;

    // ===== KPI =====
    private Double completionRate;

    private String actualScopeType;
    private String actualScopeNote;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getIsPersonalTarget() {
        return isPersonalTarget;
    }

    public void setIsPersonalTarget(Integer isPersonalTarget) {
        this.isPersonalTarget = isPersonalTarget;
    }

    public Integer getIsManagerTarget() {
        return isManagerTarget;
    }

    public void setIsManagerTarget(Integer isManagerTarget) {
        this.isManagerTarget = isManagerTarget;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Double getTargetBonGoc() {
        return targetBonGoc;
    }

    public void setTargetBonGoc(Double targetBonGoc) {
        this.targetBonGoc = targetBonGoc;
    }

    public Double getTargetBonLaBot() {
        return targetBonLaBot;
    }

    public void setTargetBonLaBot(Double targetBonLaBot) {
        this.targetBonLaBot = targetBonLaBot;
    }

    public Double getTargetClcn() {
        return targetClcn;
    }

    public void setTargetClcn(Double targetClcn) {
        this.targetClcn = targetClcn;
    }

    public Double getTargetBonLaLong() {
        return targetBonLaLong;
    }

    public void setTargetBonLaLong(Double targetBonLaLong) {
        this.targetBonLaLong = targetBonLaLong;
    }

    public Double getActualMain() {
        return actualMain;
    }

    public void setActualMain(Double actualMain) {
        this.actualMain = actualMain;
    }

    public Double getActualBonGoc() {
        return actualBonGoc;
    }

    public void setActualBonGoc(Double actualBonGoc) {
        this.actualBonGoc = actualBonGoc;
    }

    public Double getActualBonLaBot() {
        return actualBonLaBot;
    }

    public void setActualBonLaBot(Double actualBonLaBot) {
        this.actualBonLaBot = actualBonLaBot;
    }

    public Double getActualClcn() {
        return actualClcn;
    }

    public void setActualClcn(Double actualClcn) {
        this.actualClcn = actualClcn;
    }

    public Double getActualBonLaLong() {
        return actualBonLaLong;
    }

    public void setActualBonLaLong(Double actualBonLaLong) {
        this.actualBonLaLong = actualBonLaLong;
    }

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    public String getActualScopeType() {
        return actualScopeType;
    }

    public void setActualScopeType(String actualScopeType) {
        this.actualScopeType = actualScopeType;
    }

    public String getActualScopeNote() {
        return actualScopeNote;
    }

    public void setActualScopeNote(String actualScopeNote) {
        this.actualScopeNote = actualScopeNote;
    }
}