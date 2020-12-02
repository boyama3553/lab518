package com.profile.respository;

import com.profile.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getUserTest() throws Exception {
        String username = "boya";
        User user = userRepository.findByUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void getNonExistingUserTest() throws Exception {
        String username = "nonExist";
        User user = userRepository.findByUsername(username);
        assertNull(user);
    }

}
