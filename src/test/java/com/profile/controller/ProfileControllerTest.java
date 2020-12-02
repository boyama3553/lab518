package com.profile.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProfileControllerTest {

    @Autowired
    private ProfileController profileController;

    @Test
    public void controllerLoads() throws Exception {
        assertThat(profileController).isNotNull();
    }
}
