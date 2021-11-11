package com.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.servivce.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void should_return_message(){
        HomeController controller = new HomeController();
        String response = controller.hello("World");
        assertEquals("Hello, World", response);
    }
}