package com.valeos.restapidemo.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventsControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
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
//                .free(true)
//                .offline(false)
//                .eventStatus(EventStatus.PUBLISHED)
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
                .andExpect(header().string("Content-Type", MediaTypes.HAL_JSON_VALUE))
                .andExpect((jsonPath("id").value(Matchers.not(100))))
                .andExpect((jsonPath("free").value(Matchers.not(true))))
                .andExpect((jsonPath("eventStatus").value(EventStatus.DRAFT.name())))
        ;
    }


    @Test
    public void createEvent_BadRequest() throws Exception {

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
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }


    @Test
    public void createEvent_BadRequest_Empty_Input() throws Exception {

        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventDto))
                    )
                   .andExpect(status().isBadRequest())
                ;
    }

    @Test
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
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest())
        ;
    }
}