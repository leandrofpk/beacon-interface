package com.example.beaconinterface.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CertificateResourceTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation jUnitRestDocumentation = new JUnitRestDocumentation();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(this.jUnitRestDocumentation))
                .build();
    }

    @Test
//    public void getCertificateByCertificateIdentifierShouldReturnOk() throws Exception {
    public void getCertificateByCertificateIdentifier() throws Exception {
        this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/beacon/2.0/certificate/{certificateIdentifier}", "dewerwrwre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(document("beacon/2.0/certificate/get-by-certificateIdentifier",
                        pathParameters(parameterWithName("certificateIdentifier")
                                .description("It is the hash() of the public Certificate"))
//                        .responseFields(
//                                fieldWithPath("hash")
//                                        .description("It is the hash() of a Base 64 encoded PEM formatted file (X.509 ASN.1 1583 encoding)."))
                ));
    }
}