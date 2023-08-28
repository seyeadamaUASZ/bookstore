package com.sid.gl.feature;

import com.sid.gl.TestConfiguration;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.hamcrest.Matchers;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.hamcrest.CoreMatchers.containsString;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;


import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class BookStepDefinition {
    @Autowired
    private MockMvc mockMvc;

    ResultActions action;

    @When("the client calls api\\/v{int}\\/book")
    public void theClientCallsApiVBook(int arg0) throws Exception {
        action = mockMvc.perform(get("/api/v1/book").contentType(MediaType.APPLICATION_JSON));
    }
    @Then("the client receives status code of {int}")
    public void the_client_receive_status_code_of(Integer status) throws Exception {
        action.andExpect(status().is(status));
    }


    @And("the client receives response data")
    public void theClientReceivesResponseData() throws UnsupportedEncodingException {
        MvcResult result = action.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertNotNull(contentAsString);
    }
}
