package com.roberthj.musicmasterkotlin.client

import com.roberthj.musicmasterkotlin.models.Event
import org.springframework.stereotype.Service

@Service
class TicketMasterApiClientImpl: TicketMasterApiClient {
    override fun findEventsForArtist(artist: String): List<Event> {

        val fixedEvent = Event(
            name= artist,
            "Concert",
            "id1",
            "www.event.com",
            "2024-05-01",
            "Notes",
            "Globen",
            "Johanneshov",
            "Stockholm",
            "Sweden")

     return listOf(fixedEvent)

    }

}