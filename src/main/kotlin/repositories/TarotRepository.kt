package org.delcom.repositories

import org.delcom.dao.TarotDAO
import org.delcom.entities.Tarot
import org.delcom.helpers.daroTarotToModel
import org.delcom.helpers.suspendTransaction
import org.delcom.tables.TarotTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class TarotRepository : ITarotRepository {

    override suspend fun getTarots(search: String, arcana: String): List<Tarot> = suspendTransaction {
        var query = TarotTable.selectAll()

        if (arcana.isNotBlank()) {
            query = query.andWhere { TarotTable.arcana eq arcana }
        }

        if (search.isNotBlank()) {
            val keyword = "%${search.lowercase()}%"
            query = query.andWhere { TarotTable.namaKartu.lowerCase() like keyword }
        }

        query.orderBy(TarotTable.nomorKartu to SortOrder.ASC)
            .limit(78)
            .map { row ->
                Tarot(
                    id            = row[TarotTable.id].value.toString(),
                    namaKartu     = row[TarotTable.namaKartu],
                    arcana        = row[TarotTable.arcana],
                    nomorKartu    = row[TarotTable.nomorKartu],
                    suits         = row[TarotTable.suits],
                    deskripsi     = row[TarotTable.deskripsi],
                    maknaTegak    = row[TarotTable.maknaTegak],
                    maknaTerbalik = row[TarotTable.maknaTerbalik],
                    elemen        = row[TarotTable.elemen],
                    pathGambar    = row[TarotTable.pathGambar],
                    createdAt     = row[TarotTable.createdAt],
                    updatedAt     = row[TarotTable.updatedAt],
                )
            }
    }

    override suspend fun getTarotById(id: String): Tarot? = suspendTransaction {
        TarotDAO
            .find { TarotTable.id eq UUID.fromString(id) }
            .limit(1)
            .map(::daroTarotToModel)
            .firstOrNull()
    }

    override suspend fun getTarotByName(name: String): Tarot? = suspendTransaction {
        TarotDAO
            .find { TarotTable.namaKartu eq name }
            .limit(1)
            .map(::daroTarotToModel)
            .firstOrNull()
    }

    override suspend fun addTarot(tarot: Tarot): String = suspendTransaction {
        val dao = TarotDAO.new {
            namaKartu     = tarot.namaKartu
            arcana        = tarot.arcana
            nomorKartu    = tarot.nomorKartu
            suits         = tarot.suits
            deskripsi     = tarot.deskripsi
            maknaTegak    = tarot.maknaTegak
            maknaTerbalik = tarot.maknaTerbalik
            elemen        = tarot.elemen
            pathGambar    = tarot.pathGambar
            createdAt     = tarot.createdAt
            updatedAt     = tarot.updatedAt
        }
        dao.id.value.toString()
    }

    override suspend fun updateTarot(id: String, newTarot: Tarot): Boolean = suspendTransaction {
        val dao = TarotDAO
            .find { TarotTable.id eq UUID.fromString(id) }
            .limit(1)
            .firstOrNull()

        if (dao != null) {
            dao.namaKartu     = newTarot.namaKartu
            dao.arcana        = newTarot.arcana
            dao.nomorKartu    = newTarot.nomorKartu
            dao.suits         = newTarot.suits
            dao.deskripsi     = newTarot.deskripsi
            dao.maknaTegak    = newTarot.maknaTegak
            dao.maknaTerbalik = newTarot.maknaTerbalik
            dao.elemen        = newTarot.elemen
            dao.pathGambar    = newTarot.pathGambar
            dao.updatedAt     = newTarot.updatedAt
            true
        } else false
    }

    override suspend fun removeTarot(id: String): Boolean = suspendTransaction {
        val rows = TarotTable.deleteWhere { TarotTable.id eq UUID.fromString(id) }
        rows == 1
    }
}
