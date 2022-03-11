package com.valeos.restapidemo.events;

//import org.junit.Test;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("valeos")
                .description("valeos is...")
                .build();
        
        assertThat(event).isNotNull();
    }
}