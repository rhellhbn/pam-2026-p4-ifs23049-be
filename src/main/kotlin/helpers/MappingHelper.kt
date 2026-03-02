package org.delcom.helpers

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.PlantDAO
import org.delcom.dao.TarotDAO
import org.delcom.entities.Plant
import org.delcom.entities.Tarot
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: PlantDAO) = Plant(
    dao.id.value.toString(),
    dao.nama,
    dao.pathGambar,
    dao.deskripsi,
    dao.manfaat,
    dao.efekSamping,
    dao.createdAt,
    dao.updatedAt
)

fun daroTarotToModel(dao: TarotDAO) = Tarot(
    id            = dao.id.value.toString(),
    namaKartu     = dao.namaKartu,
    arcana        = dao.arcana,
    nomorKartu    = dao.nomorKartu,
    suits         = dao.suits,
    deskripsi     = dao.deskripsi,
    maknaTegak    = dao.maknaTegak,
    maknaTerbalik = dao.maknaTerbalik,
    elemen        = dao.elemen,
    pathGambar    = dao.pathGambar,
    createdAt     = dao.createdAt,
    updatedAt     = dao.updatedAt,
)