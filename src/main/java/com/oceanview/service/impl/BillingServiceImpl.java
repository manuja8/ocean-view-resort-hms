package com.oceanview.service.impl;

import com.oceanview.builder.BillBuilder;
import com.oceanview.dao.BillDAO;
import com.oceanview.dao.BillPrintLogDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.dao.impl.BillPrintLogDAOImpl;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dto.BillDTO;
import com.oceanview.entity.Bill;
import com.oceanview.entity.Reservation;
import com.oceanview.factory.BillFactory;
import com.oceanview.mapper.BillMapper;
import com.oceanview.mapper.ReservationMapper;

public class BillingServiceImpl implements com.oceanview.service.BillingService {

    private final BillDAO billDAO = new BillDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final BillPrintLogDAO printLogDAO = new BillPrintLogDAOImpl();

    @Override
    public BillDTO generateBill(int reservationNo, double discountAmount, double taxPercent, int createdByUserId, boolean forceRecalculate) {

        Reservation reservation = reservationDAO.findById(reservationNo);
        if (reservation == null) throw new IllegalArgumentException("Reservation not found.");

        Bill existing = billDAO.findByReservation(reservationNo);
        if (existing != null && !forceRecalculate) {

            existing.setReservation(reservation);
            Bill rebuilt = new BillBuilder(reservation)
                    .addRoomCharge()
                    .applyDiscount(existing.getDiscount())
                    .applyTaxAmount(existing.getTax())
                    .build();

            rebuilt.setBillNo(existing.getBillNo());
            rebuilt.setBillDate(existing.getBillDate());
            rebuilt.setCanceled(existing.isCanceled());
            return BillMapper.toDTO(rebuilt);
        }

        if (existing != null && forceRecalculate) {
            billDAO.cancel(existing.getBillNo(), createdByUserId);
        }

        // Factory creates base bill
        Bill base = BillFactory.create(reservation, createdByUserId);

        // Builder calculates amounts
        Bill built = new BillBuilder(reservation)
                .addRoomCharge()
                .applyDiscount(discountAmount)
                .applyTaxPercent(taxPercent)
                .build();


        base.setTotalAmount(built.getTotalAmount());
        base.setDiscount(built.getDiscount());
        base.setTax(built.getTax());
        base.setNumNights(built.getNumNights());
        base.setItemizedCharges(built.getItemizedCharges());

        int billId = billDAO.save(base);
        base.setBillNo(billId);

        return BillMapper.toDTO(base);
    }

    @Override
    public BillDTO getBill(int reservationNo) {
        Bill b = billDAO.findByReservation(reservationNo);
        if (b == null) return null;

        Reservation reservation = reservationDAO.findById(reservationNo);
        if (reservation != null) {
            b.setReservation(reservation);

            Bill rebuilt = new BillBuilder(reservation)
                    .addRoomCharge()
                    .applyDiscount(b.getDiscount())
                    .applyTaxAmount(b.getTax())
                    .build();

            rebuilt.setBillNo(b.getBillNo());
            rebuilt.setBillDate(b.getBillDate());
            rebuilt.setCanceled(b.isCanceled());
            return BillMapper.toDTO(rebuilt);
        }

        return BillMapper.toDTO(b);
    }

    @Override
    public BillDTO getBillById(int billId) {
        Bill b = billDAO.findById(billId);
        if (b == null) return null;

        Reservation reservation = reservationDAO.findById(b.getReservation().getReservationNo());
        if (reservation != null) {
            b.setReservation(reservation);

            Bill rebuilt = new BillBuilder(reservation)
                    .addRoomCharge()
                    .applyDiscount(b.getDiscount())
                    .applyTaxAmount(b.getTax())
                    .build();

            rebuilt.setBillNo(b.getBillNo());
            rebuilt.setBillDate(b.getBillDate());
            rebuilt.setCanceled(b.isCanceled());
            return BillMapper.toDTO(rebuilt);
        }

        return BillMapper.toDTO(b);
    }

    // called when printing
    public void logPrint(int billId, int userId) {
        printLogDAO.logPrint(billId, userId);
    }
}