package com.oceanview.facade;

import com.oceanview.dao.ReportDAO;
import com.oceanview.dao.impl.ReportDAOImpl;
import com.oceanview.dto.ReportRowDTO;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ReportFacade {

    private final ReportDAO reportDAO;


    public ReportFacade() {
        this(new ReportDAOImpl());
    }

 
    public ReportFacade(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public List<ReportRowDTO> generateRows(String type, LocalDate from, LocalDate to) {

        String t = normalizeType(type);
        switch (t) {
            case "revenue":
                return buildRevenueRows(from, to);
            case "payments":
                return buildPaymentsRows(from, to);
            case "occupancy":
            default:
                return buildOccupancyRows(from, to);
        }
    }

    private String normalizeType(String type) {
        if (type == null || type.isBlank()) return "occupancy";
        return type.trim().toLowerCase();
    }

    private List<ReportRowDTO> buildRevenueRows(LocalDate from, LocalDate to) {
        List<ReportRowDTO> rows = new ArrayList<>();

        double revenue = reportDAO.sumPaidRevenue(from, to);
        int totalBills = reportDAO.countBills(from, to);
        int paidBills = reportDAO.countPaidBills(from, to);
        int unpaidBills = reportDAO.countUnpaidBills(from, to);

        rows.add(new ReportRowDTO("Total Revenue (Paid Bills)", moneyLkr(revenue)));
        rows.add(new ReportRowDTO("Total Bills", String.valueOf(totalBills)));
        rows.add(new ReportRowDTO("Paid Bills", String.valueOf(paidBills)));
        rows.add(new ReportRowDTO("Unpaid Bills", String.valueOf(unpaidBills)));

        return rows;
    }

    private List<ReportRowDTO> buildPaymentsRows(LocalDate from, LocalDate to) {
        List<ReportRowDTO> rows = new ArrayList<>();

        int payCount = reportDAO.countPayments(from, to);
        double paySum = reportDAO.sumPayments(from, to);

        rows.add(new ReportRowDTO("Total Payments", String.valueOf(payCount)));
        rows.add(new ReportRowDTO("Total Amount Received", moneyLkr(paySum)));

        rows.addAll(reportDAO.paymentStatusBreakdown(from, to));
        return rows;
    }

    private List<ReportRowDTO> buildOccupancyRows(LocalDate from, LocalDate to) {
        List<ReportRowDTO> rows = new ArrayList<>();

        int totalRooms = reportDAO.countRooms();
        int available = reportDAO.countRoomsByStatus("available");
        int occupied = reportDAO.countRoomsByStatus("occupied");
        int maintenance = reportDAO.countRoomsByStatus("maintenance");

        int totalRes = reportDAO.countReservations(from, to);
        int booked = reportDAO.countReservationsByStatus("booked", from, to);
        int checkedIn = reportDAO.countReservationsByStatus("checked_in", from, to);
        int checkedOut = reportDAO.countReservationsByStatus("checked_out", from, to);
        int cancelled = reportDAO.countReservationsByStatus("cancelled", from, to);

        rows.add(new ReportRowDTO("Total Rooms", String.valueOf(totalRooms)));
        rows.add(new ReportRowDTO("Available Rooms", String.valueOf(available)));
        rows.add(new ReportRowDTO("Occupied Rooms", String.valueOf(occupied)));
        rows.add(new ReportRowDTO("Maintenance Rooms", String.valueOf(maintenance)));

        rows.add(new ReportRowDTO("Reservations (in range)", String.valueOf(totalRes)));
        rows.add(new ReportRowDTO("Booked", String.valueOf(booked)));
        rows.add(new ReportRowDTO("Checked In", String.valueOf(checkedIn)));
        rows.add(new ReportRowDTO("Checked Out", String.valueOf(checkedOut)));
        rows.add(new ReportRowDTO("Cancelled", String.valueOf(cancelled)));

        return rows;
    }

    private String moneyLkr(double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return "LKR " + nf.format(amount);
    }
}