package com.example.beacon.interfac.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SequenceOfPulsesResourceTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation jUnitRestDocumentation = new JUnitRestDocumentation();

//    private RestDocumentationResultHandler document;

//    private ResponseFieldsSnippet responseFieldsSnippetPulse;

    private final String startTimestamp = "2019-09-12T14:04:00.000Z";

    private final String endTimestamp = "2019-09-12T14:05:00.000Z";

    @Before
    public void setup() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(this.jUnitRestDocumentation))
                .build();
    }

    @Test
    public void skipList() throws Exception {
        this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/beacon/2.0/skiplist/time/{startTimestamp}/{endTimestamp}",
                        startTimestamp, endTimestamp))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(document("beacon/2.0/skiplist/time",

                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),

                        pathParameters(parameterWithName("startTimestamp").description("Start timestamp"),parameterWithName("endTimestamp").description("End timestamp")),
                        responseFields(beneathPath("skiplist[0]"),
                                fieldWithPath("pulse.uri").description("URI"),
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
//                                fieldWithPath("pulse.listValues[1].uri").description("Hour uri"),
//                                fieldWithPath("pulse.listValues[1].type").description("Hour type"),
//                                fieldWithPath("pulse.listValues[1].value").description("Hour value"),
//
//                                fieldWithPath("pulse.listValues[2].uri").description("Day uri"),
//                                fieldWithPath("pulse.listValues[2].type").description("Day type"),
//                                fieldWithPath("pulse.listValues[2].value").description("Day value"),
//
//                                fieldWithPath("pulse.listValues[3].uri").description("Month uri"),
//                                fieldWithPath("pulse.listValues[3].type").description("Month type"),
//                                fieldWithPath("pulse.listValues[3].value").description("Month value"),
//
//                                fieldWithPath("pulse.listValues[4].uri").description("Year uri"),
//                                fieldWithPath("pulse.listValues[4].type").description("Year type"),
//                                fieldWithPath("pulse.listValues[4].value").description("Year value"),
//
                                fieldWithPath("pulse.precommitmentValue").description("precommitmentValue"),
                                fieldWithPath("pulse.statusCode").description("statusCode"),
                                fieldWithPath("pulse.signatureValue").description("signatureValue"),
                                fieldWithPath("pulse.outputValue").description("outputValue"))
                ));
    }


}