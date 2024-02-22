package com.roberthj.musicmasterkotlin.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.roberthj.musicmasterkotlin.models.spotifyapiresponse.Image

data class Artist(
    @JsonProperty("external_urls")
    val externalUrl: String,
    val genres: ArrayList<String>,
    val href: String,
    val id: String,
    val images: ArrayList<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String,
    var events: List<Event>? = null,
    var relatedArtists: List<Artist>? = null
) {
}