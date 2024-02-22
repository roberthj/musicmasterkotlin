package com.roberthj.musicmasterkotlin.models.spotifyapiresponse

import com.fasterxml.jackson.annotation.JsonProperty

data class Item(
    @JsonProperty("external_urls")
    val externalUrls: ExternalUrls,
    val followers: Followers,
    val genres: ArrayList<String>,
    val href: String,
    val id: String,
    val images: ArrayList<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
) {
}