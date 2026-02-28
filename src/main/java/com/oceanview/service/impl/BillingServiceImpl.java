package com.oceanview.service.impl;

import com.oceanview.builder.BillBuilder;
import com.oceanview.dao.BillDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dto.BillDTO;
import com.oceanview.entity.Bill;
import com.oceanview.entity.Reservation;
import com.oceanview.mapper.BillMapper;
import com.oceanview.service.BillingService;

public class BillingServiceImpl implements BillingService {

    private BillDAO billDAO = new BillDAOImpl();
    private ReservationDAO reservationDAO = new ReservationDAOImpl();

    @Override
    public BillDTO generateBill(int reservationNo) {

        Reservation reservation = reservationDAO.findById(reservationNo);
        if (reservation == null) return null;

        Bill bill = new BillBuilder(reservation)
                .addRoomCharge()
                .addServiceCharge(1000)
                .addTax(10)
                .build();

        int billId = billDAO.save(bill);
        bill.setBillNo(billId);

        return BillMapper.toDTO(bill);
    }

    @Override
    public BillDTO getBill(int reservationNo) {
        return BillMapper.toDTO(billDAO.findByReservation(reservationNo));
    }
}