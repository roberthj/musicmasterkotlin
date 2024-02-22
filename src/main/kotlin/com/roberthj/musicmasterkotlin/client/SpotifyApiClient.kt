package com.roberthj.musicmasterkotlin.client

import com.roberthj.musicmasterkotlin.models.Artist

interface SpotifyApiClient {

    fun getArtistByName(artist: String): List<Artist>

    fun getRelatedArtists(artist: String): List<Artist>
}