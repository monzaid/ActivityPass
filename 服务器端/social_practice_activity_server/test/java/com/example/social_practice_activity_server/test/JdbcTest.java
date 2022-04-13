package com.example.social_practice_activity_server.test;

import com.example.social_practice_activity_server.utils.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;

public class JdbcTest {
    @Test
    public void testJdbcTest(){
        for (int i = 0; i < 100; i++){
            Connection connection = JdbcUtils.getConnection();
            System.out.println(connection);
            JdbcUtils.close(connection);
        }
    }
}
