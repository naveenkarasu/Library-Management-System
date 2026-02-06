package com.library.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Date utility class for date manipulation and fine calculation.
 *
 * @author Library Management System
 * @version 1.0
 * @since 2017
 */
public class DateUtil {

    // Fine rate per day in dollars
    public static final BigDecimal FINE_PER_DAY = new BigDecimal("0.50");

    // Default loan period for students (14 days)
    public static final int DEFAULT_LOAN_PERIOD_STUDENT = 14;

    // Default loan period for faculty (30 days)
    public static final int DEFAULT_LOAN_PERIOD_FACULTY = 30;

    // Date format pattern
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

    /**
     * Get current date as SQL Date
     *
     * @return current date
     */
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * Calculate due date based on issue date and loan period
     *
     * @param issueDate  the issue date
     * @param loanPeriod number of days for loan
     * @return due date
     */
    public static Date calculateDueDate(Date issueDate, int loanPeriod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issueDate);
        calendar.add(Calendar.DAY_OF_MONTH, loanPeriod);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * Calculate due date with default student loan period
     *
     * @param issueDate the issue date
     * @return due date (14 days from issue date)
     */
    public static Date calculateDueDateForStudent(Date issueDate) {
        return calculateDueDate(issueDate, DEFAULT_LOAN_PERIOD_STUDENT);
    }

    /**
     * Calculate due date with faculty loan period
     *
     * @param issueDate the issue date
     * @return due date (30 days from issue date)
     */
    public static Date calculateDueDateForFaculty(Date issueDate) {
        return calculateDueDate(issueDate, DEFAULT_LOAN_PERIOD_FACULTY);
    }

    /**
     * Calculate the number of days between two dates
     *
     * @param startDate start date
     * @param endDate   end date
     * @return number of days between dates
     */
    public static long daysBetween(Date startDate, Date endDate) {
        long diffInMillies = endDate.getTime() - startDate.getTime();
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    /**
     * Calculate overdue days from due date to return date or current date
     *
     * @param dueDate    the due date
     * @param returnDate the return date (null if not returned yet)
     * @return number of overdue days (0 if not overdue)
     */
    public static long calculateOverdueDays(Date dueDate, Date returnDate) {
        Date compareDate = returnDate != null ? returnDate : getCurrentDate();
        long days = daysBetween(dueDate, compareDate);
        return days > 0 ? days : 0;
    }

    /**
     * Calculate fine amount based on overdue days
     *
     * @param overdueDays number of days overdue
     * @return fine amount
     */
    public static BigDecimal calculateFine(long overdueDays) {
        if (overdueDays <= 0) {
            return BigDecimal.ZERO;
        }
        return FINE_PER_DAY.multiply(new BigDecimal(overdueDays));
    }

    /**
     * Calculate fine amount from due date to return date
     *
     * @param dueDate    the due date
     * @param returnDate the return date
     * @return fine amount
     */
    public static BigDecimal calculateFine(Date dueDate, Date returnDate) {
        long overdueDays = calculateOverdueDays(dueDate, returnDate);
        return calculateFine(overdueDays);
    }

    /**
     * Check if a date is overdue (past the due date)
     *
     * @param dueDate the due date to check
     * @return true if overdue
     */
    public static boolean isOverdue(Date dueDate) {
        return getCurrentDate().after(dueDate);
    }

    /**
     * Format a date to string
     *
     * @param date the date to format
     * @return formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormatter.format(date);
    }

    /**
     * Parse a string to SQL Date
     *
     * @param dateString the date string in yyyy-MM-dd format
     * @return SQL Date object
     * @throws ParseException if parsing fails
     */
    public static Date parseDate(String dateString) throws ParseException {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        java.util.Date parsed = dateFormatter.parse(dateString);
        return new Date(parsed.getTime());
    }

    /**
     * Add days to a date
     *
     * @param date the original date
     * @param days number of days to add
     * @return new date with added days
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * Get the start of the day for a given date
     *
     * @param date the date
     * @return date at start of day (00:00:00)
     */
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }
}
