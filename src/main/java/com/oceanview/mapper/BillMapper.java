package com.oceanview.mapper;

import com.oceanview.dto.BillDTO;
import com.oceanview.entity.Bill;

public class BillMapper {

    public static BillDTO toDTO(Bill bill) {
        if (bill == null) return null;

        BillDTO dto = new BillDTO();
        dto.setBillNo(bill.getBillNo());
        dto.setReservation(ReservationMapper.toDTO(bill.getReservation()));

        dto.setTotalAmount(bill.getTotalAmount());
        dto.setDiscount(bill.getDiscount());
        dto.setTax(bill.getTax());
        dto.setNumNights(bill.getNumNights());
        dto.setCanceled(bill.isCanceled());

        if (bill.getBillDate() != null) dto.setBillDate(bill.getBillDate().toString());
        dto.setItemizedCharges(bill.getItemizedCharges());

        return dto;
    }
}