package com.oceanview.dao;

import com.oceanview.entity.Complaint;

import java.util.List;

public interface ComplaintDAO {

    void save(Complaint complaint);

    void updateStatus(int complaintId, String status);

    List<Complaint> findAll();

    List<Complaint> findByStatus(String status);
}