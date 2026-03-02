package org.delcom.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Tarot(
    var id: String = UUID.randomUUID().toString(),
    var namaKartu: String,
    var arcana: String,        // "Major" atau "Minor"
    var nomorKartu: Int,
    var suits: String = "",    // Cups, Wands, Swords, Pentacles (hanya untuk Minor Arcana)
    var deskripsi: String,
    var maknaTegak: String,    // Makna ketika kartu tegak
    var maknaTerbalik: String, // Makna ketika kartu terbalik
    var elemen: String = "",   // Api, Air, Tanah, Udara
    var pathGambar: String = "",

    @Contextual
    val createdAt: Instant = Clock.System.now(),
    @Contextual
    var updatedAt: Instant = Clock.System.now(),
)
