package org.delcom.services

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.delcom.data.AppException
import org.delcom.data.DataResponse
import org.delcom.data.TarotRequest
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.ITarotRepository
import java.io.File
import java.util.UUID

class TarotService(private val tarotRepository: ITarotRepository) {

    // Ambil semua kartu tarot
    suspend fun getAllTarots(call: ApplicationCall) {
        val search = call.request.queryParameters["search"] ?: ""
        val arcana = call.request.queryParameters["arcana"] ?: ""

        val tarots = tarotRepository.getTarots(search, arcana)

        call.respond(DataResponse("success", "Berhasil mengambil daftar kartu tarot", mapOf("tarots" to tarots)))
    }

    // Ambil kartu tarot berdasarkan ID
    suspend fun getTarotById(call: ApplicationCall) {
        val id = call.parameters["id"] ?: throw AppException(400, "ID kartu tidak boleh kosong!")
        val tarot = tarotRepository.getTarotById(id) ?: throw AppException(404, "Kartu tarot tidak ditemukan!")

        call.respond(DataResponse("success", "Berhasil mengambil data kartu tarot", mapOf("tarot" to tarot)))
    }

    // Parse multipart form data
    private suspend fun getTarotRequest(call: ApplicationCall): TarotRequest {
        val req = TarotRequest()

        val multipart = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 5)
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "namaKartu"     -> req.namaKartu = part.value.trim()
                        "arcana"        -> req.arcana = part.value.trim()
                        "nomorKartu"    -> req.nomorKartu = part.value.toIntOrNull() ?: 0
                        "suits"         -> req.suits = part.value.trim()
                        "deskripsi"     -> req.deskripsi = part.value.trim()
                        "maknaTegak"    -> req.maknaTegak = part.value.trim()
                        "maknaTerbalik" -> req.maknaTerbalik = part.value.trim()
                        "elemen"        -> req.elemen = part.value.trim()
                    }
                }
                is PartData.FileItem -> {
                    val ext = part.originalFileName
                        ?.substringAfterLast('.', "")
                        ?.let { if (it.isNotEmpty()) ".$it" else "" } ?: ""

                    val fileName = UUID.randomUUID().toString() + ext
                    val filePath = "uploads/tarots/$fileName"
                    val file = File(filePath)
                    file.parentFile.mkdirs()
                    part.provider().copyAndClose(file.writeChannel())
                    req.pathGambar = filePath
                }
                else -> {}
            }
            part.dispose()
        }
        return req
    }

    // Validasi request
    private fun validateTarotRequest(req: TarotRequest) {
        val validator = ValidatorHelper(req.toMap())
        validator.required("namaKartu", "Nama kartu tidak boleh kosong")
        validator.required("arcana", "Arcana tidak boleh kosong")
        validator.required("deskripsi", "Deskripsi tidak boleh kosong")
        validator.required("maknaTegak", "Makna tegak tidak boleh kosong")
        validator.required("maknaTerbalik", "Makna terbalik tidak boleh kosong")
        validator.validate()

        if (req.arcana !in listOf("Major", "Minor")) {
            throw AppException(400, "Arcana harus bernilai 'Major' atau 'Minor'!")
        }
    }

    // Tambah kartu tarot
    suspend fun createTarot(call: ApplicationCall) {
        val req = getTarotRequest(call)
        validateTarotRequest(req)

        val exist = tarotRepository.getTarotByName(req.namaKartu)
        if (exist != null) {
            File(req.pathGambar).takeIf { it.exists() }?.delete()
            throw AppException(409, "Kartu tarot dengan nama ini sudah terdaftar!")
        }

        val tarotId = tarotRepository.addTarot(req.toEntity())
        call.respond(DataResponse("success", "Berhasil menambahkan kartu tarot", mapOf("tarotId" to tarotId)))
    }

    // Ubah kartu tarot
    suspend fun updateTarot(call: ApplicationCall) {
        val id = call.parameters["id"] ?: throw AppException(400, "ID kartu tidak boleh kosong!")
        val oldTarot = tarotRepository.getTarotById(id) ?: throw AppException(404, "Kartu tarot tidak ditemukan!")

        val req = getTarotRequest(call)
        if (req.pathGambar.isEmpty()) req.pathGambar = oldTarot.pathGambar

        validateTarotRequest(req)

        if (req.namaKartu != oldTarot.namaKartu) {
            val exist = tarotRepository.getTarotByName(req.namaKartu)
            if (exist != null) {
                File(req.pathGambar).takeIf { it.exists() }?.delete()
                throw AppException(409, "Kartu tarot dengan nama ini sudah terdaftar!")
            }
        }

        if (req.pathGambar != oldTarot.pathGambar) {
            File(oldTarot.pathGambar).takeIf { it.exists() }?.delete()
        }

        val updated = tarotRepository.updateTarot(id, req.toEntity())
        if (!updated) throw AppException(400, "Gagal memperbarui kartu tarot!")

        call.respond(DataResponse("success", "Berhasil mengubah data kartu tarot", null))
    }

    // Hapus kartu tarot
    suspend fun deleteTarot(call: ApplicationCall) {
        val id = call.parameters["id"] ?: throw AppException(400, "ID kartu tidak boleh kosong!")
        val oldTarot = tarotRepository.getTarotById(id) ?: throw AppException(404, "Kartu tarot tidak ditemukan!")

        val deleted = tarotRepository.removeTarot(id)
        if (!deleted) throw AppException(400, "Gagal menghapus kartu tarot!")

        File(oldTarot.pathGambar).takeIf { it.exists() }?.delete()

        call.respond(DataResponse("success", "Berhasil menghapus kartu tarot", null))
    }

    // Ambil gambar kartu tarot
    suspend fun getTarotImage(call: ApplicationCall) {
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest)
        val tarot = tarotRepository.getTarotById(id) ?: return call.respond(HttpStatusCode.NotFound)
        val file = File(tarot.pathGambar)
        if (!file.exists()) return call.respond(HttpStatusCode.NotFound)
        call.respondFile(file)
    }
}
