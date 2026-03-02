package org.delcom.repositories

import org.delcom.entities.Tarot

interface ITarotRepository {
    suspend fun getTarots(search: String, arcana: String): List<Tarot>
    suspend fun getTarotById(id: String): Tarot?
    suspend fun getTarotByName(name: String): Tarot?
    suspend fun addTarot(tarot: Tarot): String
    suspend fun updateTarot(id: String, newTarot: Tarot): Boolean
    suspend fun removeTarot(id: String): Boolean
}
