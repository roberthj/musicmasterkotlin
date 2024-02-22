package com.roberthj.musicmasterkotlin.client

import java.time.LocalDateTime

data class SpotifyApiAuthCache(var token: String = "",
                               var expiresAt: LocalDateTime = LocalDateTime.now()) {
}