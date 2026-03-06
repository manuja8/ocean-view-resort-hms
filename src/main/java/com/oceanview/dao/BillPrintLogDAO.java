package com.oceanview.dao;

public interface BillPrintLogDAO {
    void logPrint(int billId, int printedByUserId);
}