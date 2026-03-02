package org.delcom.data

import kotlinx.serialization.Serializable
import org.delcom.entities.Tarot

@Serializable
data class TarotRequest(
    var namaKartu: String = "",
    var arcana: String = "",
    var nomorKartu: Int = 0,
    var suits: String = "",
    var deskripsi: String = "",
    var maknaTegak: String = "",
    var maknaTerbalik: String = "",
    var elemen: String = "",
    var pathGambar: String = "",
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "namaKartu"    to namaKartu,
        "arcana"       to arcana,
        "nomorKartu"   to nomorKartu,
        "deskripsi"    to deskripsi,
        "maknaTegak"   to maknaTegak,
        "maknaTerbalik" to maknaTerbalik,
    )

    fun toEntity(): Tarot = Tarot(
        namaKartu     = namaKartu,
        arcana        = arcana,
        nomorKartu    = nomorKartu,
        suits         = suits,
        deskripsi     = deskripsi,
        maknaTegak    = maknaTegak,
        maknaTerbalik = maknaTerbalik,
        elemen        = elemen,
        pathGambar    = pathGambar,
    )
}
