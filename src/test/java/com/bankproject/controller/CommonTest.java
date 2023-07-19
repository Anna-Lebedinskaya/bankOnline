package com.bankproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public abstract class CommonTest {

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;
    protected ObjectMapper objectMapper = new ObjectMapper();
    @BeforeAll
    public void prepare() {
        objectMapper.registerModule(new JavaTimeModule()); //чтобы правильно отоброжалось время
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .apply(SecurityMockMvcConfigurers.springSecurity()) //чтобы работала security
                .build();
    }

    protected abstract void createObject() throws Exception;
    protected abstract void updateObject() throws Exception;
    protected abstract void deleteObject() throws Exception;
    protected abstract void getAll() throws Exception;

//    protected String asJsonString(final Object obj) {
//        try {
//            return objectMapper.writeValueAsString(obj);
//        } catch (JsonProcessingException ex) {
//            log.error(ex.getMessage());
//            return null;
//        }
//    }
}
