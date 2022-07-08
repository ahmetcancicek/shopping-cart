package com.shopping.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void it_should_return_message() {
        HomeController controller = new HomeController();
        String response = controller.hello("World");
        assertEquals("Hello, World", response);
    }
}