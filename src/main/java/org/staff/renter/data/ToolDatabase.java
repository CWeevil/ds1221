package org.staff.renter.data;

import org.staff.renter.domain.Tool;
import org.staff.renter.domain.ToolType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ToolDatabase {
    //In a real app, I'd use an actual database
    private final Map<String, Tool> toolTable;
    private final Map<String, ToolType> toolTypeTable;

    private static ToolDatabase instance;

    private ToolDatabase() {
        toolTypeTable = generateToolTypes();
        toolTable = generateToolTable();
    }

    public static ToolDatabase getInstance() {
        if (instance == null) {
            instance = new ToolDatabase();
        }

        return instance;
    }

    public Tool lookup(String toolCode) {
        return toolTable.get(toolCode);
    }

    private Map<String, ToolType> generateToolTypes() {
        Map<String, ToolType> types = new HashMap<>();
        types.put("Ladder", new ToolType("Ladder", new BigDecimal("1.99"), true, true, false));
        types.put("Chainsaw", new ToolType("Chainsaw", new BigDecimal("1.49"), true, false, true));
        types.put("Jackhammer", new ToolType("Jackhammer", new BigDecimal("2.99"), true, false, false));

        return types;
    }

    private Map<String, Tool> generateToolTable() {
        Map<String, Tool> tools = new HashMap<>();
        tools.put("CHNS", new Tool("CHNS", toolTypeTable.get("Chainsaw"), "Stihl"));
        tools.put("LADW", new Tool("LADW", toolTypeTable.get("Ladder"), "Werner"));
        tools.put("JAKD", new Tool("JAKD", toolTypeTable.get("Jackhammer"), "DeWalt"));
        tools.put("JAKR", new Tool("JAKR", toolTypeTable.get("Jackhammer"), "Ridgid"));

        return tools;
    }
}
