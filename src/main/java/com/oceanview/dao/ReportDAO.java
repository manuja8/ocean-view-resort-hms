package com.oceanview.dao;

import com.oceanview.dto.ReportRowDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportDAO {

    // ------------------- ROOMS / OCCUPANCY -------------------

    int countRooms();

    /**
     * Counts rooms by status (e.g., available, occupied, maintenance).
     * Comparison is case-insensitive.
     */
    int countRoomsByStatus(String status);

    // ------------------- RESERVATIONS -------------------

    /**
     * Counts reservations in the given date range.
     * Implementation may use check-in date or fallback date columns if needed.
     */
    int countReservations(LocalDate from, LocalDate to);

    /**
     * Counts reservations by status in the given date range
     * (e.g., booked, checked_in, checked_out, cancelled).
     * Comparison is case-insensitive.
     */
    int countReservationsByStatus(String status, LocalDate from, LocalDate to);

    // ------------------- BILLS / REVENUE -------------------

    /**
     * Counts bills in the given date range (excluding cancelled bills).
     */
    int countBills(LocalDate from, LocalDate to);

    /**
     * "Paid bill" means: a bill with at least one COMPLETED payment.
     * (Pending/failed payments do NOT make a bill "paid".)
     */
    int countPaidBills(LocalDate from, LocalDate to);

    /**
     * "Unpaid bill" means: a bill with NO COMPLETED payments.
     */
    int countUnpaidBills(LocalDate from, LocalDate to);

    /**
     * Paid revenue is calculated as SUM(total_amount) for bills that have at least one COMPLETED payment.
     */
    double sumPaidRevenue(LocalDate from, LocalDate to);

    // ------------------- PAYMENTS -------------------

    /**
     * Counts payment records in the given date range (all statuses).
     */
    int countPayments(LocalDate from, LocalDate to);

    /**
     * Total amount received in the given date range (COMPLETED payments only).
     */
    double sumPayments(LocalDate from, LocalDate to);

    /**
     * Breakdown of payment count by payment_statuses.status_name in the given date range.
     */
    List<ReportRowDTO> paymentStatusBreakdown(LocalDate from, LocalDate to);
}