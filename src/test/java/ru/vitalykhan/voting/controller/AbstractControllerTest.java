package ru.vitalykhan.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@SpringJUnitWebConfig(locations = {"classpath:spring.xml"})
@Transactional
public abstract class AbstractControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    public ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }
}
