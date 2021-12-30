package org.staff.renter.domain;

import org.staff.renter.service.DateService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class RentalAgreement {
    private Tool tool;

    private int days;

    private LocalDate checkoutDate;

    private LocalDate dueDate;

    private int chargedDays;

    private int discountPercent;

    private BigDecimal preDiscountCharge;

    private BigDecimal discount;

    private BigDecimal finalCharge;

    private final DecimalFormat moneyFormatter = new DecimalFormat("$#,###.00");

    private RentalAgreement() {
    }

    /**
     * Create a rental agreement with all the fields. Allows RentalAgreements to be easily created completely.
     * I used a static method instead of placing this in the constructor because it feels cleaner to me to keep heavy logic and service calls out of the constructor
     * @param item The tool the rental agreement is being created to rent
     * @param rentalDays Number of days to rent the tool
     * @param percentOff Discount to apply to the rental price
     * @param checkout Date the tool is checked out
     * @return A fully constructed RentalAgreement
     */
    public static RentalAgreement createAgreement(Tool item, int rentalDays, int percentOff, LocalDate checkout) {
        RentalAgreement agreement = new RentalAgreement();
        agreement.tool = item;
        agreement.days = rentalDays;
        agreement.discountPercent = percentOff;
        agreement.checkoutDate = checkout;

        DateService dateService = DateService.getInstance();
        agreement.dueDate = dateService.getDueDate(checkout, rentalDays);

        ToolType rentedType = item.getType();
        agreement.chargedDays = dateService.calculateChargedDays(checkout, agreement.dueDate, rentedType.chargesWeekends(), rentedType.chargesHolidays());
        agreement.preDiscountCharge = rentedType.getCharge().multiply(BigDecimal.valueOf(agreement.chargedDays)).setScale(2, RoundingMode.HALF_UP);
        agreement.discount = agreement.preDiscountCharge.multiply(BigDecimal.valueOf(agreement.discountPercent / 100.0)).setScale(2, RoundingMode.HALF_UP);
        agreement.finalCharge = agreement.preDiscountCharge.subtract(agreement.discount);

        return agreement;
    }

    @Override
    public String toString() {
        ToolType type = tool.getType();
        DateService dateService = DateService.getInstance();

        return "Tool Code: " + tool.getCode() +
                "\nTool Type: " + type.getType() +
                "\nTool Brand: " + tool.getBrand() +
                "\nRental Days: " + days +
                "\nCheckout Date: " + dateService.formatDate(checkoutDate) +
                "\nDue Date: " + dateService.formatDate(dueDate) +
                "\nDaily Rental Charge: " + moneyFormatter.format(type.getCharge()) +
                "\nCharge Days: " + chargedDays +
                "\nPre-discount charge: " + moneyFormatter.format(preDiscountCharge) +
                "\nDiscount Percent: " + discountPercent + "%" +
                "\nDiscount Amount: " + moneyFormatter.format(discount) +
                "\nFinal Charge: " + moneyFormatter.format(finalCharge);
    }

    /**
     * Helper method to easily print the agreement to the console.
     */
    public void printToConsole() {
        System.out.println(this);
    }

    public Tool getTool() {
        return tool;
    }

    public int getDays() {
        return days;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getChargedDays() {
        return chargedDays;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }
}
