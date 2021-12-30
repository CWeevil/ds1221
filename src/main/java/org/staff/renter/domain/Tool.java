package org.staff.renter.domain;

public class Tool {
    private String code;

    private ToolType type;

    private String brand;

    public Tool(String toolCode, ToolType toolType, String company) {
        code = toolCode;
        type = toolType;
        brand = company;
    }

    public String getCode() {
        return code;
    }

    public ToolType getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }
}
