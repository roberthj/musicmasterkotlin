package com.roberthj.musicmasterkotlin.client

import com.fasterxml.jackson.annotation.JsonProperty

data class SpotifyApiAuthResponse(@JsonProperty("access_token") val accessToken: String,
                                  @JsonProperty("token_type") val tokenType: String,
                                  @JsonProperty("expires_in") val expiresIn: Int) {
}