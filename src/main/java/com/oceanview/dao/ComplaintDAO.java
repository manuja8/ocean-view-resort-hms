package com.oceanview.dao;

import com.oceanview.dto.ComplaintDTO;
import com.oceanview.entity.Complaint;

import java.util.List;

public interface ComplaintDAO {
    int save(Complaint complaint);

    boolean update(Complaint complaint);

    boolean delete(int complaintId);

    ComplaintDTO findById(int complaintId);

    List<ComplaintDTO> search(String q, String status, String priority);
}