package com.example.beacon.interfac.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SinglePulseResourceTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation jUnitRestDocumentation = new JUnitRestDocumentation();

//    private RestDocumentationResultHandler document;

    private ResponseFieldsSnippet responseFieldsSnippetPulse;

    private final String timestamp = "2019-06-09T16:31:00.000Z";

//    private RestDocumentationResultHandler documentationHandler;

    @Before
    public void setup() {
//        this.documentationHandler = document("{method-name}",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()));

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(this.jUnitRestDocumentation))
//                .alwaysDo(documentationHandler)
                .build();

        responseFieldsSnippetPulse = responseFields(fieldWithPath("pulse.uri").description("URI"),
                fieldWithPath("pulse.version").description("Version"),
                fieldWithPath("pulse.cipherSuite").description("cipherSuite"),
                fieldWithPath("pulse.period").description("period"),
                fieldWithPath("pulse.certificateId").description("certificateId"),
                fieldWithPath("pulse.chainIndex").description("chainIndex"),
                fieldWithPath("pulse.pulseIndex").description("pulseIndex"),
                fieldWithPath("pulse.timeStamp").description("timeStamp"),
                fieldWithPath("pulse.localRandomValue").description("localRandomValue"),
                fieldWithPath("pulse.external.sourceId").description("External source Id"),
                fieldWithPath("pulse.external.statusCode").description("External statuscode"),
                fieldWithPath("pulse.external.value").description("External value"),
                fieldWithPath("pulse.listValues[0].uri").description("Previous uri"),
                fieldWithPath("pulse.listValues[0].type").description("Previous type"),
                fieldWithPath("pulse.listValues[0].value").description("Previous value"),

                fieldWithPath("pulse.listValues[1].uri").description("Hour uri"),
                fieldWithPath("pulse.listValues[1].type").description("Hour type"),
                fieldWithPath("pulse.listValues[1].value").description("Hour value"),

                fieldWithPath("pulse.listValues[2].uri").description("Day uri"),
                fieldWithPath("pulse.listValues[2].type").description("Day type"),
                fieldWithPath("pulse.listValues[2].value").description("Day value"),

                fieldWithPath("pulse.listValues[3].uri").description("Month uri"),
                fieldWithPath("pulse.listValues[3].type").description("Month type"),
                fieldWithPath("pulse.listValues[3].value").description("Month value"),

                fieldWithPath("pulse.listValues[4].uri").description("Year uri"),
                fieldWithPath("pulse.listValues[4].type").description("Year type"),
                fieldWithPath("pulse.listValues[4].value").description("Year value"),

                fieldWithPath("pulse.precommitmentValue").description("precommitmentValue"),
                fieldWithPath("pulse.statusCode").description("statusCode"),
                fieldWithPath("pulse.signatureValue").description("signatureValue"),
                fieldWithPath("pulse.outputValue").description("outputValue"));
    }



    @Test
    public void specificTime() throws Exception {

        this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/beacon/2.0/pulse/time/{timestamp}", this.timestamp))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(document("beacon/2.0/pulse/time/get-by-timestamp",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("timestamp")
                                .description("A time string formatted according to the rules of RFC 3339")),
                        responseFieldsSnippetPulse

                ));

    }

//    @Test
//    public void specificTimeError() throws Exception {
//
//        RestDocumentationResultHandler docs = document("Error Response",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//
//                responseFields(
//                        fieldWithPath("status").optional().type("Integer").description("Application status field.")
////                        fieldWithPath("errorMsg").type("String").description("A global description of the cause of the error")
//
//                )
//        );
//
//        this.mockMvc.perform(
//                RestDocumentationRequestBuilders.get("/beacon/2.0/pulse/time/{timestamp}", "2019-08-15T20:17:00.ZZZZ"))
//                .andExpect(status().isBadRequest())
//                .andDo(document("beacon/2.0/pulse/time/get-by-timestamp",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(parameterWithName("timestamp")
//                                .description("A time string formatted according to the rules of RFC 3339"))
//                ))
//                .andDo(docs);
//
//    }

    @Test
    public void next() throws Exception {
        this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/beacon/2.0/pulse/time/next/{timestamp}", this.timestamp))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(document("beacon/2.0/pulse/time/next/get-next-timestamp",
                        pathParameters(parameterWithName("timestamp")
                                .description("A time string formatted according to the rules of RFC 3339")),
                        responseFieldsSnippetPulse
                ));
    }

    @Test
    public void previous() throws Exception {
        this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/beacon/2.0/pulse/time/previous/{timestamp}", this.timestamp))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(document("beacon/2.0/pulse/time/previous/get-previous-timestamp",
                        pathParameters(parameterWithName("timestamp")
                                .description("A time string formatted according to the rules of RFC 3339")),
                        responseFieldsSnippetPulse
                ));
    }

    @Test
    public void last() throws Exception {

        this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/beacon/2.0/pulse/last" ))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(document("beacon/2.0/pulse/time/last/get-last",
                        responseFieldsSnippetPulse
                ));

    }
}