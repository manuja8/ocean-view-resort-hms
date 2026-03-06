package com.oceanview.testutil;

import com.oceanview.config.DBConnection;
import org.h2.tools.RunScript;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.time.LocalDate;

public final class TestDb {

    private static boolean initialized = false;

    private TestDb() {
    }

    public static synchronized void init() {
        if (initialized) return;

        try (Connection con = DBConnection.getInstance().getConnection()) {
            RunScript.execute(con, new InputStreamReader(
                    TestDb.class.getClassLoader().getResourceAsStream("schema-h2.sql"),
                    StandardCharsets.UTF_8
            ));
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to init H2 schema", e);
        }
    }

    public static void seedBasicData() {
        try (Connection con = DBConnection.getInstance().getConnection();
             Statement st = con.createStatement()) {

            st.execute("INSERT INTO user_roles(role_id, role_name) VALUES (1,'admin'),(2,'receptionist')");
            st.execute("INSERT INTO users(user_id,username,full_name,address,contact_no,role_id,password_hash,is_active,is_blocked) " +
                    "VALUES (1,'admin','Admin User','Galle','0770000000',1,'x',TRUE,FALSE)");
            st.execute("INSERT INTO guests(guest_id,full_name,address,contact_no,identification_no,identification_type,created_by_user_id) " +
                    "VALUES (1,'Kasun Perera','Galle','0771111111','200012345V','NIC',1)");

            st.execute("INSERT INTO room_types(room_type_id,type_name,price) VALUES (1,'Deluxe',15000.00)");
            st.execute("INSERT INTO rooms(room_id,room_number,room_type_id,status,created_by_user_id) VALUES (1,'D01',1,'available',1)");

        } catch (Exception e) {
            throw new RuntimeException("Failed to seed test data", e);
        }
    }

    public static void clean() {
        try (Connection con = DBConnection.getInstance().getConnection();
             Statement st = con.createStatement()) {
            st.execute("DELETE FROM complaints");
            st.execute("DELETE FROM bills");
            st.execute("DELETE FROM reservations");
            st.execute("DELETE FROM rooms");
            st.execute("DELETE FROM room_types");
            st.execute("DELETE FROM guests");
            st.execute("DELETE FROM users");
            st.execute("DELETE FROM user_roles");
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean test DB", e);
        }
    }

    public static void seedReservation(int reservationId, int guestId, int roomId,
                                       LocalDate checkIn, LocalDate checkOut,
                                       String status, int createdByUserId) {
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO reservations(reservation_id, reservation_number, guest_id, room_id, " +
                             "check_in_date, check_out_date, status, created_by_user_id, created_at, updated_at) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)"
             )) {

            ps.setInt(1, reservationId);
            ps.setString(2, "RES-" + reservationId);
            ps.setInt(3, guestId);
            ps.setInt(4, roomId);
            ps.setTimestamp(5, Timestamp.valueOf(checkIn.atStartOfDay()));  // Converts LocalDate to Timestamp
            ps.setTimestamp(6, Timestamp.valueOf(checkOut.atStartOfDay()));  // Converts LocalDate to Timestamp
            ps.setString(7, status);
            ps.setInt(8, createdByUserId);

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to seed reservation", e);
        }
    }
}