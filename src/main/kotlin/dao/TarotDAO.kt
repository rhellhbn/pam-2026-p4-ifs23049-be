package org.delcom.dao

import org.delcom.tables.TarotTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class TarotDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, TarotDAO>(TarotTable)

    var namaKartu     by TarotTable.namaKartu
    var arcana        by TarotTable.arcana
    var nomorKartu    by TarotTable.nomorKartu
    var suits         by TarotTable.suits
    var deskripsi     by TarotTable.deskripsi
    var maknaTegak    by TarotTable.maknaTegak
    var maknaTerbalik by TarotTable.maknaTerbalik
    var elemen        by TarotTable.elemen
    var pathGambar    by TarotTable.pathGambar
    var createdAt     by TarotTable.createdAt
    var updatedAt     by TarotTable.updatedAt
}
