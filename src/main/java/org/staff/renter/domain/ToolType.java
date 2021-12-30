package org.staff.renter.domain;

import java.math.BigDecimal;

public class ToolType {
    private final String type;
    private final BigDecimal charge;
    private boolean chargesWeekdays;
    private final boolean chargesWeekends;
    private final boolean chargesHolidays;

    public ToolType(String name, BigDecimal amount, boolean costWeekday, boolean costWeekend, boolean costHoliday) {
        type = name;
        charge = amount;
        chargesWeekdays = costWeekday;
        chargesWeekends = costWeekend;
        chargesHolidays = costHoliday;

    }

    public String getType() {
        return type;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public boolean chargesWeekends() {
        return chargesWeekends;
    }

    public boolean chargesHolidays() {
        return chargesHolidays;
    }
}
