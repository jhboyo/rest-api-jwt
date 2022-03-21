package com.valeos.restapidemo.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is Wrong");
            errors.rejectValue("maxPrice", "wrongValue", "maxPrice is Wrong");
        }

        LocalDateTime endEventTime = eventDto.getEndEventDateTime();
        if (endEventTime.isBefore(eventDto.getBeginEventDateTime()) ||
            endEventTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
            endEventTime.isBefore(eventDto.getBeginEnrollmentDateTime())
        ) {
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }

        // Todo beginEventDateTime
        // Todo closeEnrollmentDateTime


    }
}
