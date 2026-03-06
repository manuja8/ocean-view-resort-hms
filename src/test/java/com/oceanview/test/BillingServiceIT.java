package com.oceanview.test;

import com.oceanview.dto.BillDTO;
import com.oceanview.service.impl.BillingServiceImpl;
import com.oceanview.testutil.TestDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BillingServiceIT extends BaseH2Test {

    private BillingServiceImpl billingService;

    @BeforeEach
    void setup() {
        TestDb.clean();
        TestDb.seedBasicData();
        TestDb.seedReservation(
                1, 1, 1,
                LocalDate.parse("2026-03-10"),
                LocalDate.parse("2026-03-13"),
                "booked", 1
        );
        billingService = new BillingServiceImpl();
    }

    @Test
    void generateBill_shouldCreateBill() {
        BillDTO bill = billingService.generateBill(1, 1000.0, 10.0, 1, false);
        assertNotNull(bill);
        assertEquals(3, bill.getNumNights());
        assertEquals(1000.0, bill.getDiscount(), 0.01);
        assertTrue(bill.getTotalAmount() > 0);
    }

    @Test
    void generateBill_twiceWithoutForce_shouldReturnExisting() {
        BillDTO b1 = billingService.generateBill(1, 500.0, 10.0, 1, false);
        BillDTO b2 = billingService.generateBill(1, 9999.0, 50.0, 1, false);

        // since force=false, it should reuse existing bill's discount/tax
        assertEquals(b1.getBillNo(), b2.getBillNo());
        assertEquals(b1.getDiscount(), b2.getDiscount(), 0.01);
    }

    @Test
    void generateBill_withForce_shouldCancelOldAndCreateNew() {
        BillDTO b1 = billingService.generateBill(1, 500.0, 10.0, 1, false);
        BillDTO b2 = billingService.generateBill(1, 1000.0, 10.0, 1, true);

        assertNotEquals(b1.getBillNo(), b2.getBillNo());
    }
}