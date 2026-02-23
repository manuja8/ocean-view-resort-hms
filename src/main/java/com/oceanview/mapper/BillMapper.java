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
        dto.setItemizedCharges(bill.getItemizedCharges());

        return dto;
    }

    public static Bill toEntity(BillDTO dto) {
        if (dto == null) return null;

        Bill bill = new Bill();
        bill.setBillNo(dto.getBillNo());
        bill.setReservation(ReservationMapper.toEntity(dto.getReservation()));
        bill.setTotalAmount(dto.getTotalAmount());
        bill.setItemizedCharges(dto.getItemizedCharges());

        return bill;
    }
}