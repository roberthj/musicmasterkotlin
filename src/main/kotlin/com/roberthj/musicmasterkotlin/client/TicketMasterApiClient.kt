package com.roberthj.musicmasterkotlin.client

import com.roberthj.musicmasterkotlin.models.Event

interface TicketMasterApiClient {

    fun findEventsForArtist(artist: String): List<Event>
}