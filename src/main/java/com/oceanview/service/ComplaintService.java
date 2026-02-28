package com.oceanview.service;

import java.util.List;

import com.oceanview.dto.ComplaintDTO;

public interface ComplaintService {

    void createComplaint(ComplaintDTO dto);

    void changeStatus(int complaintId, String status);

    List<ComplaintDTO> getAllComplaints();
}