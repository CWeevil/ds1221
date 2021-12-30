package org.staff.renter.service;

import org.staff.renter.data.ToolDatabase;
import org.staff.renter.domain.Tool;

public class ToolService {
    //This class is a bit unnecessary for the project but was added to represent what I would do in a real application
    //For a real application, the ToolDatabase would be an actual database and this class would be in charge of keeping track of connection information and being the gatekeeper for accessing the data
    private static ToolService instance;
    private static final ToolDatabase data = ToolDatabase.getInstance();

    private ToolService() {}

    public static ToolService getInstance() {
        if (instance == null) {
            instance = new ToolService();
        }

        return instance;
    }

    public Tool getTool(String toolCode) {
        return data.lookup(toolCode);
    }
}
