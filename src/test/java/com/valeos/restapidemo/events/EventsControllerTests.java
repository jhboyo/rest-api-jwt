package com.valeos.restapidemo.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valeos.restapidemo.common.RestDocsConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventsControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;

    @Test
//    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("valeos")
                .description("valeos is")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 03, 22, 17, 53))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 03, 30, 17, 53))
                .beginEventDateTime(LocalDateTime.of(2022, 04, 1, 9, 00))
                .endEventDateTime(LocalDateTime.of(2022, 04, 3, 17, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("seoul square")
                .build();

//        event.setId(10);

        // Mockito 객체로 실제 저장은 null 로 되기 때문에 아래 코드 작성
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE+";charset=UTF-8"))
                .andExpect((jsonPath("free").value(false)))
                .andExpect((jsonPath("offline").value(true)))
                .andExpect((jsonPath("eventStatus").value(EventStatus.DRAFT.name())))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event"))
        ;
    }


    @Test
    @DisplayName("입력 받을 수 없는 값을 사용하는 경우에 에러가 발생하는 테스트")
//    @TestDescription("입력 받을 수 없는 값을 사용하는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {

        Event event = Event.builder()
                .id(100)
                .name("valeos")
                .description("valeos is..")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 03, 22, 17, 53))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 03, 30, 17, 53))
                .beginEventDateTime(LocalDateTime.of(2022, 04, 1, 9, 00))
                .endEventDateTime(LocalDateTime.of(2022, 04, 3, 17, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("seoul square")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

//        event.setId(10);

        // Mockito 객체로 실제 저장은 null 로 되기 때문에 아래 코드 작성
//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }


    @Test
//    @TestDescription("입력값이 비어 있는 경우에 에러가 발생하는 테스트")
    @DisplayName("입력값이 비어 있는 경우에 에러가 발생하는 테스트")
    public void createEvent_BadRequest_Empty_Input() throws Exception {

        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsString(eventDto))
                    )
                   .andExpect(status().isBadRequest())
                ;
    }

    @Test
    @DisplayName("입력값이 잘못 되어있는 경우에 에러가 발생하는 테스트")
//    @TestDescription("입력값이 잘못 되어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_BadRequest_Wrong_Input() throws Exception {

        EventDto eventDto = EventDto.builder()
                                    .name("valeos")
                                    .description("valeos is..")
                                    .beginEnrollmentDateTime(LocalDateTime.of(2022, 03, 22, 17, 53))
                                    .closeEnrollmentDateTime(LocalDateTime.of(2022, 03, 21, 17, 53))
                                    .beginEventDateTime(LocalDateTime.of(2022, 04, 1, 9, 00))
                                    .endEventDateTime(LocalDateTime.of(2022, 04, 3, 17, 00))
                                    .basePrice(1000)
                                    .maxPrice(200)
                                    .limitOfEnrollment(100)
                                    .location("seoul square")
                                    .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(eventDto)))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$[0].objectName").exists())
                        .andExpect(jsonPath("$[0].defaultMessage").exists())
                        .andExpect(jsonPath("$[0].code").exists())
        ;
    }
}