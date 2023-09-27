package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public List<Message> getAllMessages() {

        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), 
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public boolean checkIfUserExistsBy_PostedBy(int posted_by) {

        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, posted_by);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count is greater than 0, the user exists.
            }

            return false; // User does not exists.
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            return false; // In case of exception, return false.
        } 

    }

    public int createMessage(Message message) {

        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            int affectedRows = ps.executeUpdate();

            if(affectedRows == 0) {
                throw new SQLException("Creating message failed, no rows affected.");
            }

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Creating message failed, no ID obtained.");
            }
            // System.out.println("Message has been inserted!");
        } catch(SQLException e) {
            e.printStackTrace();
            // Handle any database-related exceptions
            throw new RuntimeException("Error creating message: " + e.getMessage());
        }
    }

}