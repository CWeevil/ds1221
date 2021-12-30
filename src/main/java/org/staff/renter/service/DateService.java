package org.staff.renter.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.TemporalAdjusters.firstInMonth;

public class DateService {

    public static DateService instance;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/uu");

    private DateService() {}

    public static DateService getInstance() {
        if (instance == null) {
            instance = new DateService();
        }

        return instance;
    }

    /**
     * Calculate the due date for a given checkout.
     * @param checkoutDate The date the checkout occurs. Will throw an IllegalArgumentException if null
     * @param rentalDays The number of days to check out for
     * @return A date representing the day the tool is due
     */
    public LocalDate getDueDate(LocalDate checkoutDate, int rentalDays) {
        if (checkoutDate == null) {
            throw new IllegalArgumentException("Checkout date must not be null to find the due date");
        }

        return checkoutDate.plusDays(rentalDays);
    }

    /**
     * Calculate the number of days that will be charged for a given time period
     * @param checkout The day the tool is checked out. Will throw an IllegalArgumentException if null
     * @param dueDate The day that the tool is due. Will throw an IllegalArgumentException if null
     * @param chargesWeekends A boolean indicating whether weekend days should be charged
     * @param chargesHolidays A boolean indicating whether holidays should be charged
     * @return The number of charged days in the provided range
     */
    public int calculateChargedDays(LocalDate checkout, LocalDate dueDate, boolean chargesWeekends, boolean chargesHolidays) {
        if (checkout == null || dueDate == null) {
            throw new IllegalArgumentException("To calculate the number of charged days, both checkout date and due date must not be null");
        }

        if (dueDate.isBefore(checkout)) {
            throw new IllegalArgumentException("Invalid range, due date must come after checkout date");
        }

        //checkout date + 1 because charged days start counting the day after checkout
        //due date + 1 because the argument passed to datesUntil() is exclusive
        long charges = checkout.plusDays(1).datesUntil(dueDate.plusDays(1)).filter(date -> !freeHoliday(date, chargesHolidays) && !freeWeekend(date, chargesWeekends)).count();

        return Math.toIntExact(charges);
    }

    /**
     * Helper method to format a date, allows a single source of truth for date formatting in the entire app
     * @param date Date to format
     * @return The date passed in formatted as a string
     */
    public String formatDate(LocalDate date) {
        return date.format(dateFormatter);
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private boolean freeWeekend(LocalDate currDay, boolean chargesWeekends) {
        if (chargesWeekends) {
            return false;
        }

        return isWeekend(currDay);
    }

    private boolean freeHoliday(LocalDate currDay, boolean chargesHolidays) {
        if (chargesHolidays) {
            return false;
        }

        boolean isFourthOfJuly = false;
        if (currDay.getMonth() == Month.JULY) {
            isFourthOfJuly = (currDay.getDayOfMonth() == 4 && !isWeekend(currDay)) ||
                    (currDay.getDayOfMonth() == 3 && currDay.getDayOfWeek() == DayOfWeek.FRIDAY) ||
                    (currDay.getDayOfMonth() == 5 && currDay.getDayOfWeek() == DayOfWeek.MONDAY);
        }
        boolean isLaborDay = currDay.getMonth() == Month.SEPTEMBER && currDay.with(firstInMonth(DayOfWeek.MONDAY)).equals(currDay);

        return isFourthOfJuly || isLaborDay;
    }
}
