package com.valeos.restapidemo.events;


import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {



}
