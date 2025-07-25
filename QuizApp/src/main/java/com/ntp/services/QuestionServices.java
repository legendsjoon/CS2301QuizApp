/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntp.services;

import com.ntp.pojo.Question;
import com.ntp.utils.JdbcConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Joon
 */
public class QuestionUpdateServices {
     public void addQuestion(Question q) throws SQLException {
        Connection conn = JdbcConnector.getInstance().connect();
        
        conn.setAutoCommit(false);
        String sql = "INSERT INTO question(content, hint, image, category_id, level_id) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement stm = conn.prepareCall(sql);
        stm.setString(1, q.getContent());
        stm.setString(2, q.getHint());
        stm.setString(3, q.getImage());
        stm.setInt(4, q.getCate().getId());
        stm.setInt(5, q.getLevel().getId());
        
        if (stm.executeUpdate() > 0) {
            int questionId = -1;
            ResultSet r = stm.getGeneratedKeys();
            if (r.next())
                questionId = r.getInt(1);
            
            sql = "INSERT INTO choice(content, is_correct, question_id) VALUES(?, ?, ?)";
            stm = conn.prepareCall(sql);
            
            for (var c: q.getChoices()) {
                stm.setString(1, c.getContent());
                stm.setBoolean(2, c.isIsCorrect());
                stm.setInt(3, questionId);
                
                stm.executeUpdate();
            }
            
            conn.commit();
        } else
            conn.rollback();
    }
}
