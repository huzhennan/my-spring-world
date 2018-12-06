package com.example.demo;

import com.example.demo.entities.User;
import com.example.demo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetUserList() {
        User user =  userMapper.selectById(1L);
        assertNotNull(user);
    }

    @Test
    public void testCreateUser() {
        User user = new User("zhangsan", 22, "zhangsan@example.com");
        userMapper.insert(user);
    }

    @Test
    public void testSelectByName() {
        User user = userMapper.selectByName("Tom");
        assertNotNull(user);
    }
}
