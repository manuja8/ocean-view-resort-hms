package com.oceanview.facade;

import com.oceanview.dao.*;
import com.oceanview.dao.impl.*;
import com.oceanview.dto.ReportDTO;
import com.oceanview.entity.*;

import java.util.List;

public class ReportFacade {

    private ReservationDAO reservationDAO = new ReservationDAOImpl();
    private GuestDAO guestDAO = new GuestDAOImpl();
    private RoomDAO roomDAO = new RoomDAOImpl();
    private BillDAO billDAO = new BillDAOImpl();
    private PaymentDAO paymentDAO = new PaymentDAOImpl();

    public ReportDTO generateReport() {

        ReportDTO report = new ReportDTO();

        // Reservations
        List<Reservation> reservations = reservationDAO.findAll();
        report.setTotalReservations(reservations.size());

        // Guests
        List<Guest> guests = guestDAO.findAll();
        report.setTotalGuests(guests.size());

        // Rooms
        List<Room> rooms = roomDAO.findAll();
        report.setTotalRooms(rooms.size());

        // Bills
        int totalBills = 0, paidBills = 0, unpaidBills = 0;
        double totalRevenue = 0;

        for (Reservation r : reservations) {
            Bill bill = billDAO.findByReservation(r.getReservationNo());
            if (bill != null) {
                totalBills++;
                boolean paid = paymentDAO.existsByBillId(bill.getBillNo());
                if (paid) {
                    paidBills++;
                    totalRevenue += bill.getTotalAmount();
                } else {
                    unpaidBills++;
                }
            }
        }

        report.setTotalBills(totalBills);
        report.setPaidBills(paidBills);
        report.setUnpaidBills(unpaidBills);
        report.setTotalPayments(paidBills); // total payments = paid bills count
        report.setTotalRevenue(totalRevenue);

        return report;
    }
}