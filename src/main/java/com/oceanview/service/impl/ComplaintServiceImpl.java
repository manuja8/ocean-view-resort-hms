package com.oceanview.service.impl;

import com.oceanview.dao.ComplaintDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.impl.ComplaintDAOImpl;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dto.ComplaintDTO;
import com.oceanview.entity.Complaint;
import com.oceanview.factory.ComplaintFactory;
import com.oceanview.observer.NotificationCenter;
import com.oceanview.observer.event.ComplaintEvent;
import com.oceanview.service.ComplaintService;

import java.util.List;

public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintDAO complaintDAO = new ComplaintDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl(); // to validate reservationId

    @Override
    public int create(ComplaintDTO dto, int userId) {
        validate(dto, true);

      
        if (dto.getReservationId() != null && dto.getReservationId() > 0) {
            if (reservationDAO.findById(dto.getReservationId()) == null) {
                throw new IllegalArgumentException("Reservation ID not found.");
            }
        }

        Complaint c = ComplaintFactory.create(dto, userId);
        int id = complaintDAO.save(c);

        // Observer notify
        NotificationCenter.getInstance()
                .complaintSubject()
                .notifyObservers(new ComplaintEvent(id, c.getGuestId(), c.getSubject(), c.getPriority()));

        return id;
    }

    @Override
    public boolean update(ComplaintDTO dto, int userId) {
        validate(dto, false);

        if (dto.getReservationId() != null && dto.getReservationId() > 0) {
            if (reservationDAO.findById(dto.getReservationId()) == null) {
                throw new IllegalArgumentException("Reservation ID not found.");
            }
        }

        Complaint c = ComplaintFactory.createForUpdate(dto, userId);
        return complaintDAO.update(c);
    }

    @Override
    public ComplaintDTO getById(int id) {
        return complaintDAO.findById(id);
    }

    @Override
    public List<ComplaintDTO> search(String q, String status, String priority) {
        return complaintDAO.search(q, status, priority);
    }

    @Override
    public boolean delete(int id) {
        return complaintDAO.delete(id);
    }

    private void validate(ComplaintDTO dto, boolean create) {
        if (!create && dto.getComplaintId() <= 0) throw new IllegalArgumentException("Invalid complaint.");
        if (dto.getGuestId() <= 0) throw new IllegalArgumentException("Guest is required.");
        if (dto.getSubject() == null || dto.getSubject().trim().isEmpty())
            throw new IllegalArgumentException("Subject is required.");
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty())
            throw new IllegalArgumentException("Description is required.");

        String status = dto.getStatus() == null ? "open" : dto.getStatus().trim().toLowerCase();
        String priority = dto.getPriority() == null ? "medium" : dto.getPriority().trim().toLowerCase();

        if (!(status.equals("open") || status.equals("in_progress") || status.equals("resolved") || status.equals("closed")))
            throw new IllegalArgumentException("Invalid status.");

        if (!(priority.equals("low") || priority.equals("medium") || priority.equals("high")))
            throw new IllegalArgumentException("Invalid priority.");

        dto.setStatus(status);
        dto.setPriority(priority);
    }
}