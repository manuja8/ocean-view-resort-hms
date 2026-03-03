package com.oceanview.dao;

import com.oceanview.dto.ReportRowDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportDAO {

    // Occupancy
    int countRooms();

    int countRoomsByStatus(String status);

    int countReservations(LocalDate from, LocalDate to);

    int countReservationsByStatus(String status, LocalDate from, LocalDate to);

    // Revenue / Bills
    int countBills(LocalDate from, LocalDate to);              // is_canceled = 0

    int countPaidBills(LocalDate from, LocalDate to);          // has payments

    int countUnpaidBills(LocalDate from, LocalDate to);        // no payments

    double sumPaidRevenue(LocalDate from, LocalDate to);       // sum(total_amount) for paid bills

    // Payments
    int countPayments(LocalDate from, LocalDate to);

    double sumPayments(LocalDate from, LocalDate to);

    List<ReportRowDTO> paymentStatusBreakdown(LocalDate from, LocalDate to);
}