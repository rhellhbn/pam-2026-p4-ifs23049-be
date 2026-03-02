package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TarotTable : UUIDTable("tarots") {
    val namaKartu    = varchar("nama_kartu", 100)
    val arcana       = varchar("arcana", 10)
    val nomorKartu   = integer("nomor_kartu")
    val suits        = varchar("suits", 50).default("")
    val deskripsi    = text("deskripsi")
    val maknaTegak   = text("makna_tegak")
    val maknaTerbalik = text("makna_terbalik")
    val elemen       = varchar("elemen", 50).default("")
    val pathGambar   = varchar("path_gambar", 255).default("")
    val createdAt    = timestamp("created_at")
    val updatedAt    = timestamp("updated_at")
}
