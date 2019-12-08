package com.cn.gtool.bean.dto;

public class AddMachineDTO {

    private String machineCode;
    private String machineName;

    private int userId;
    private String payCode;

    public String getMachineCode() {
        return machineCode;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }
}
