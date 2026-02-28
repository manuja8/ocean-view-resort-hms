package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.BillDAO;
import com.oceanview.entity.Bill;
import com.oceanview.entity.Reservation;

import java.sql.*;

public class BillDAOImpl implements BillDAO {

    private Connection con = DBConnection.getInstance().getConnection();

    @Override
    public int save(Bill bill) {
        try {
            String sql = "INSERT INTO bills (reservation_no, total_amount, itemized_charges) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, bill.getReservation().getReservationNo());
            ps.setDouble(2, bill.getTotalAmount());
            ps.setString(3, bill.getItemizedCharges());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Bill findByReservation(int reservationNo) {
        try {
            String sql = "SELECT * FROM bills WHERE reservation_no=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, reservationNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Bill bill = new Bill();
                bill.setBillNo(rs.getInt("bill_no"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bill.setItemizedCharges(rs.getString("itemized_charges"));

                Reservation reservation = new Reservation();
                reservation.setReservationNo(reservationNo);
                bill.setReservation(reservation);

                return bill;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Bill findById(int billId) {
        try {
            String sql = "SELECT * FROM bills WHERE bill_no=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Bill bill = new Bill();
                bill.setBillNo(rs.getInt("bill_no"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bill.setItemizedCharges(rs.getString("itemized_charges"));

                // Optionally set reservation info if needed
                Reservation reservation = new Reservation();
                reservation.setReservationNo(rs.getInt("reservation_no"));
                bill.setReservation(reservation);

                return bill;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}