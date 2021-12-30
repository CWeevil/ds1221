package org.staff.renter.service;

import org.junit.jupiter.api.Test;
import org.staff.renter.domain.RentalAgreement;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CheckoutTests {

    @Test
    public void testInvalidDiscount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> generateAgreement("JAKR", 5, 101, 2015, 9, 3));

        assertEquals("Discount must be in the range of 0-100 (inclusive)", exception.getMessage());
    }

    @Test
    public void testInvalidTool() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> generateAgreement("HAMR", 2, 10, 2021, 12, 12));

        assertEquals("Tool code HAMR is not a valid code", exception.getMessage());
    }

    @Test
    public void testLadderHoliday3days() {
        RentalAgreement agreement = generateAgreement("LADW", 3, 10,2020, 7, 2);

        checkAssertions(2, "3.98", ".40", "3.58", agreement);
    }

    @Test
    public void testChainsawHoliday5days() {
        RentalAgreement agreement = generateAgreement("CHNS", 5, 25, 2015, 7, 2);

        checkAssertions(3, "4.47", "1.12", "3.35", agreement);
    }

    @Test
    public void testJackhammerHoliday6days() {
        RentalAgreement agreement = generateAgreement("JAKD", 6, 0, 2015, 9, 3);

        checkAssertions(3, "8.97", "0.00", "8.97", agreement);
    }

    @Test
    public void testJackhammerHoliday9Days() {
        RentalAgreement agreement = generateAgreement("JAKR", 9, 0, 2015, 7, 2);

        checkAssertions(5, "14.95", "0.00", "14.95", agreement);
    }

    @Test
    public void testJackhammerHolidayHalfOff() {
        RentalAgreement agreement = generateAgreement("JAKR", 4, 50, 2020, 7, 2);

        checkAssertions(1, "2.99", "1.50", "1.49", agreement);
    }

    @Test
    public void testNoChargeDays() {
        RentalAgreement agreement = generateAgreement("JAKR", 3, 10, 2020, 7, 2);

        checkAssertions(0, "0.00", "0.00", "0.00", agreement);
    }

    @Test
    public void testLadderNoHoliday5days() {
        RentalAgreement agreement = generateAgreement("LADW", 5, 10, 2021, 12, 12);

        checkAssertions(5, "9.95", "1.00", "8.95", agreement);
    }

    private RentalAgreement generateAgreement(String toolCode, int rentalDays, int discount, int year, int month, int day) {
        CheckoutService service = CheckoutService.getInstance();

        return service.checkout(toolCode, rentalDays, discount, LocalDate.of(year, month, day));
    }

    private void checkAssertions(int chargedDays, String originalCharge, String discount, String finalCharge, RentalAgreement agreement) {
        assertEquals(chargedDays, agreement.getChargedDays(), "Number of charged days was incorrect");
        assertEquals(new BigDecimal(discount), agreement.getDiscount(), "Discount was incorrect");
        assertEquals(new BigDecimal(originalCharge), agreement.getPreDiscountCharge(), "Pre-discount charge was incorrect");
        assertEquals(new BigDecimal(finalCharge), agreement.getFinalCharge(), "Final charge was incorrect");
    }

}
