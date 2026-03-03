package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.FaqDAO;
import com.oceanview.entity.Faq;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FaqDAOImpl implements FaqDAO {

    @Override
    public List<Faq> findAll() {
        String sql = "SELECT faq_id, question, answer, is_active FROM faq ORDER BY faq_id DESC";
        List<Faq> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("FAQ findAll failed", e);
        }

        return list;
    }

    @Override
    public List<Faq> findActiveOnly() {
        String sql = "SELECT faq_id, question, answer, is_active FROM faq WHERE is_active = 1 ORDER BY faq_id DESC";
        List<Faq> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("FAQ findActiveOnly failed", e);
        }

        return list;
    }

    @Override
    public Faq findById(int id) {
        String sql = "SELECT faq_id, question, answer, is_active FROM faq WHERE faq_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("FAQ findById failed", e);
        }
    }

    @Override
    public int save(Faq faq, int createdByUserId) {
        String sql = "INSERT INTO faq (question, answer, is_active, created_by_user_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, NOW(), NOW())";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, faq.getQuestion());
            ps.setString(2, faq.getAnswer());
            ps.setBoolean(3, faq.isActive());
            ps.setInt(4, createdByUserId);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("FAQ save failed", e);
        }
    }

    @Override
    public boolean update(Faq faq, int updatedByUserId) {
        String sql = "UPDATE faq SET question=?, answer=?, is_active=?, updated_by_user_id=?, updated_at=NOW() " +
                "WHERE faq_id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, faq.getQuestion());
            ps.setString(2, faq.getAnswer());
            ps.setBoolean(3, faq.isActive());
            ps.setInt(4, updatedByUserId);
            ps.setInt(5, faq.getFaqId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("FAQ update failed", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM faq WHERE faq_id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("FAQ delete failed", e);
        }
    }

    private Faq map(ResultSet rs) throws SQLException {
        Faq f = new Faq();
        f.setFaqId(rs.getInt("faq_id"));
        f.setQuestion(rs.getString("question"));
        f.setAnswer(rs.getString("answer"));
        f.setActive(rs.getBoolean("is_active"));
        return f;
    }
}