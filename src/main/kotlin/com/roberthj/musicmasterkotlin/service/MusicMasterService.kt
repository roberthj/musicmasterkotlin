package com.roberthj.musicmasterkotlin.service

import com.roberthj.musicmasterkotlin.client.SpotifyApiClient
import com.roberthj.musicmasterkotlin.client.TicketMasterApiClient
import com.roberthj.musicmasterkotlin.models.Artist
import org.springframework.stereotype.Service


@Service
class MusicMasterService(
    val spotifyApiClient: SpotifyApiClient,
    val ticketMasterApiClient: TicketMasterApiClient
) {

    fun findEventByArtistName(artist: String): Artist? {

        val mostPopularArtist =
            spotifyApiClient.getArtistByName(artist).filter { artist.equals(it.name, ignoreCase = true) }
                .maxByOrNull(Artist::popularity)

        val relatedArtists =
            mostPopularArtist?.let {
                spotifyApiClient.getRelatedArtists(it.id)
            } ?: emptyList()

        //Add events for related artists
        relatedArtists
            .forEach {
                Thread.sleep(200)
                it.events = ticketMasterApiClient.findEventsForArtist(it.name)
            }

        mostPopularArtist?.relatedArtists = relatedArtists

        return mostPopularArtist

    }
}