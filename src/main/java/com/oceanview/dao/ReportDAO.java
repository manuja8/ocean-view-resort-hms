package com.oceanview.dao;

import com.oceanview.dto.ReportRowDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportDAO {


    int countRooms();


    int countRoomsByStatus(String status);


    int countReservations(LocalDate from, LocalDate to);


    int countReservationsByStatus(String status, LocalDate from, LocalDate to);


    int countBills(LocalDate from, LocalDate to);


    int countPaidBills(LocalDate from, LocalDate to);


    int countUnpaidBills(LocalDate from, LocalDate to);


    double sumPaidRevenue(LocalDate from, LocalDate to);


    int countPayments(LocalDate from, LocalDate to);

    double sumPayments(LocalDate from, LocalDate to);

    List<ReportRowDTO> paymentStatusBreakdown(LocalDate from, LocalDate to);
}