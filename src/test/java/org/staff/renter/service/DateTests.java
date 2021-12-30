package org.staff.renter.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DateTests {

    @Test
    public void testDueDate() {
        DateService service = DateService.getInstance();
        LocalDate dueDate = service.getDueDate(LocalDate.of(2011, 12, 7), 2);

        assertEquals(LocalDate.of(2011, 12, 9), dueDate);
    }

    @Test
    public void testLongCheckout() {
        DateService service = DateService.getInstance();
        LocalDate dueDate = service.getDueDate(LocalDate.of(2009, 1, 11), 37);

        assertEquals(LocalDate.of(2009, 2, 17), dueDate);
    }

    @Test
    public void testYearBoundary() {
        DateService service = DateService.getInstance();
        LocalDate dueDate = service.getDueDate(LocalDate.of(2005, 12, 29), 12);

        assertEquals(LocalDate.of(2006, 1, 10), dueDate);
    }

    @Test
    public void testChargedWeek() {
        DateService service = DateService.getInstance();
        int chargedDays = service.calculateChargedDays(LocalDate.of(2020, 12, 26), LocalDate.of(2020, 12, 31), true, true);

        assertEquals(5, chargedDays);
    }

    @Test
    public void testChargedWeekend() {
        DateService service = DateService.getInstance();
        int chargedDays = service.calculateChargedDays(LocalDate.of(2020, 12, 31), LocalDate.of(2021, 1, 3), true, true);

        assertEquals(3, chargedDays);
    }

    @Test
    public void testUnchargedWeekend() {
        DateService service = DateService.getInstance();
        int chargedDays = service.calculateChargedDays(LocalDate.of(2020, 12, 31), LocalDate.of(2021, 1, 3), false, true);

        assertEquals(1, chargedDays);
    }

    @Test
    public void testChargedHoliday() {
        DateService service = DateService.getInstance();
        int chargedDays = service.calculateChargedDays(LocalDate.of(2021, 7, 3), LocalDate.of(2021, 7, 6), true, true);

        assertEquals(3, chargedDays);
    }

    @Test
    public void testUncharged4th() {
        DateService service = DateService.getInstance();
        int chargedDays = service.calculateChargedDays(LocalDate.of(2021, 7, 3), LocalDate.of(2021, 7, 6), true, false);

        assertEquals(2, chargedDays);
    }

    @Test
    public void testUnchargedLaborDay() {
        DateService service = DateService.getInstance();
        int chargedDays = service.calculateChargedDays(LocalDate.of(2021, 9, 3), LocalDate.of(2021, 9, 7), true, false);

        assertEquals(3, chargedDays);
    }

    @Test
    public void testInvalidRange() {
        DateService service = DateService.getInstance();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.calculateChargedDays(LocalDate.of(2021, 9, 3), LocalDate.of(2021, 7, 6), true, false));

        assertEquals("Invalid range, due date must come after checkout date", exception.getMessage());
    }
}
