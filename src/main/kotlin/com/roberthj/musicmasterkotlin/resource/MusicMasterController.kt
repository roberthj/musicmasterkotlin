package com.roberthj.musicmasterkotlin.resource

import com.roberthj.musicmasterkotlin.models.Artist
import com.roberthj.musicmasterkotlin.service.MusicMasterService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController



@RestController
@RequestMapping("/v1")
class MusicMasterController(val musicMasterService: MusicMasterService) {

    @GetMapping("/find_events/{artist}")
    fun findEventByArtistName(@PathVariable(value = "artist") artist: String): Artist? {
        return musicMasterService.findEventByArtistName("metallica")
    }

}