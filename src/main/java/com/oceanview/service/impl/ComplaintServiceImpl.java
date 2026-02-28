package com.oceanview.service.impl;

import com.oceanview.dao.ComplaintDAO;
import com.oceanview.dao.impl.ComplaintDAOImpl;
import com.oceanview.dto.ComplaintDTO;
import com.oceanview.mapper.ComplaintMapper;
import com.oceanview.service.ComplaintService;

import java.util.List;
import java.util.stream.Collectors;

public class ComplaintServiceImpl implements ComplaintService {

    private ComplaintDAO complaintDAO = new ComplaintDAOImpl();

    @Override
    public void createComplaint(ComplaintDTO dto) {
        complaintDAO.save(ComplaintMapper.toEntity(dto));
    }

    @Override
    public void changeStatus(int complaintId, String status) {
        complaintDAO.updateStatus(complaintId, status);
    }

    @Override
    public List<ComplaintDTO> getAllComplaints() {
        return complaintDAO.findAll()
                .stream()
                .map(ComplaintMapper::toDTO)
                .collect(Collectors.toList());
    }
}