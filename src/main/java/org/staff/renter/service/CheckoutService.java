package org.staff.renter.service;

import org.staff.renter.domain.RentalAgreement;
import org.staff.renter.domain.Tool;

import java.time.LocalDate;

public class CheckoutService {
    private static CheckoutService instance;
    //In a real app, I'd probably make this an interface so that a test implementation could be injected if desired
    private final ToolService toolService;

    private CheckoutService(ToolService tools) {
        toolService = tools;
    }

    //I prefer to make my services singletons just because it feels cleaner to me than a bunch of static methods
    public static CheckoutService getInstance() {
        if (instance == null) {
            //Note here: I probably wouldn't create the tight coupling to ToolService in a real application. Instead, I'd use some framework to register the service as a bean and get it from there
            instance = new CheckoutService(ToolService.getInstance());
        }

        return instance;
    }

    /**
     * Generate a rental agreement to match the provided parameters
     * @param toolCode Code of the tool that should be checked out. If the code is invalid, the method will throw an IllegalArgumentException
     * @param rentalDays Number of days the tool should be checked out for. Must be greater than 0
     * @param discount The discount percentage to apply to the final charge. Must be between 0 and 100 inclusive
     * @param checkout Date to check out the tool on
     * @return A RentalAgreement that specifies all the data associated to this checkout
     */
    public RentalAgreement checkout(String toolCode, int rentalDays, int discount, LocalDate checkout) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Number of days to rent must be greater than 0");
        }

        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be in the range of 0-100 (inclusive)");
        }

        Tool tool = toolService.getTool(toolCode);
        if (tool == null) {
            throw new IllegalArgumentException("Tool code " + toolCode + " is not a valid code");
        }

        return RentalAgreement.createAgreement(tool, rentalDays, discount, checkout);
    }
}
