package com.shopping.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(PaymentMethodController.class)
@AutoConfigureMockMvc
public class PaymentMethodControllerTest extends BaseControllerTest {

}
