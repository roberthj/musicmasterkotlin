package com.roberthj.musicmasterkotlin.client

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.roberthj.musicmasterkotlin.models.Artist
import com.roberthj.musicmasterkotlin.models.spotifyapiresponse.ArtistsRoot
import com.roberthj.musicmasterkotlin.models.spotifyapiresponse.Item
import com.roberthj.musicmasterkotlin.models.spotifyapiresponse.RelatedArtistsRoot
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.URLEncoder
import java.time.LocalDateTime
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

const val BASE_URI_SPOTIFY = "https://api.spotify.com/v1"


@Component
class SpotifyApiClientImpl(
    val httpWebClient: HttpWebClient,
    var apiTokenCache: SpotifyApiAuthCache = SpotifyApiAuthCache(),
    @Value("\${spotify.api.client_id}") val clientId: String,
    @Value("\${spotify.api.client_secret}") val clientSecret: String
) : SpotifyApiClient {

    val objectMapper = jacksonObjectMapper()
    val accessToken: String = getAccessToken()

    override fun getArtistByName(artist: String): List<Artist> {

        val uri = generateFullSearchUri("/search", "artist", artist);

        val headers = getHttpHeaders(accessToken);

        val response = httpWebClient.getSyncronously(uri, headers);

        var responseObject: ArtistsRoot? = null
        try {
            responseObject = objectMapper.readValue(response, ArtistsRoot::class.java)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e) //TODO: Make exception more specific?
        }

        //let = only call that function if artists.items is not null
        return responseObject.artists?.items?.let { extractArtist(it.toList()) }.orEmpty();
    }

    override fun getRelatedArtists(id: String): List<Artist> {

        val uri = generateRelatedArtistsUri(id);

        val headers = getHttpHeaders(accessToken);

        val response = httpWebClient.getSyncronously(uri, headers);

        var responseObject: RelatedArtistsRoot? = null
        try {
            responseObject = objectMapper.readValue(response, RelatedArtistsRoot::class.java)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e) //TODO: Make exception more specific?
        }

        return extractArtist(responseObject.artists.toList())
    }

    fun extractArtist(items: List<Item>): List<Artist> {

        return items
            .map {
                Artist(
                    externalUrl = it.externalUrls.spotify,
                    genres = it.genres,
                    href = it.href,
                    id = it.id,
                    images = it.images,
                    name = it.name,
                    popularity = it.popularity,
                    type = it.type,
                    uri = it.uri
                )
            }
    }

    private fun getHttpHeaders(accessToken: String): HttpHeaders {
        val headers = HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer $accessToken");
        return headers;
    }

    private fun getAccessToken(): String {
        if (apiTokenCache.token == "" || LocalDateTime.now().isAfter(apiTokenCache.expiresAt)) {
            generateAccessToken()
        }
        return apiTokenCache.token

    }

    fun generateFullSearchUri(path: String, type: String, value: String): URI {

        return UriComponentsBuilder
            .fromUriString(BASE_URI_SPOTIFY + path)
            .queryParam("type", type)
            .queryParam("q", URLEncoder.encode(value, "UTF-8"))
            .build(true).toUri();
    }

    private fun generateRelatedArtistsUri(id: String): URI {
        return UriComponentsBuilder.fromUriString("$BASE_URI_SPOTIFY/artists/$id/related-artists")
            .build(true).toUri()
    }


    @OptIn(ExperimentalEncodingApi::class)
    private fun generateAccessToken() {


        var tokenUrl = UriComponentsBuilder.fromUriString("https://accounts.spotify.com/api/token").build(true).toUri();

        val credentials = "$clientId:$clientSecret";

        val encodedCredentials = Base64.encode(credentials.encodeToByteArray())

        val authHeaders = HttpHeaders();
        authHeaders.set("Authorization", "Basic $encodedCredentials");
        authHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED;

        val requestBody = "grant_type=client_credentials";

        val authTokenResponse = httpWebClient.postSyncronously(tokenUrl, authHeaders, requestBody);

        var authResponse: SpotifyApiAuthResponse? = null;
        try {
            val objectMapper = jacksonObjectMapper();
            if (authTokenResponse != null) {
                authResponse = objectMapper.readValue(authTokenResponse)
            }
        } catch (e: JsonProcessingException) {
            println("Error Json")
        }

        if (authResponse != null) {
            apiTokenCache.expiresAt = LocalDateTime.now().plusSeconds(authResponse.expiresIn.toLong())
            apiTokenCache.token = authResponse.accessToken
        };

    }
}