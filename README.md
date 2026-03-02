# PAM 2026 P4 — Backend API (Delcom Plants & Tarot)

API backend berbasis **Ktor + Kotlin + PostgreSQL** yang menyediakan dua sumber data:

- 🌿 **Plants** — CRUD data tumbuhan Delcom
- 🃏 **Tarot**  — CRUD kartu Tarot (Major & Minor Arcana)

---

## 📋 Prasyarat

| Tool | Versi Minimum |
|---|---|
| JDK | 17+ |
| Kotlin | 2.3.0 |
| Gradle | 8.x (wrapper sudah tersedia) |
| PostgreSQL | 14+ |

---

## 🚀 Langkah Instalasi

### 1. Clone / Ekstrak Proyek

```bash
# Jika dari Git
git clone <url-repo>
cd pam-2026-p4-ifs18005-be-main

# Jika dari ZIP
unzip pam-2026-p4-ifs18005-be-main.zip
cd pam-2026-p4-ifs18005-be-main
```

---

### 2. Siapkan Database PostgreSQL

```sql
CREATE DATABASE db_pam_plants;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE db_pam_plants TO postgres;
```

Kemudian jalankan migration:

```bash
psql -U postgres -d db_pam_plants -f data.sql
```

> Tabel `tarots` juga dibuat otomatis saat aplikasi pertama dijalankan.

---

### 3. Konfigurasi Environment

```bash
cp .env.example .env
```

Edit `.env`:

```env
APP_HOST=localhost
APP_PORT=8000

DB_HOST=127.0.0.1
DB_PORT=5432
DB_NAME=db_pam_plants
DB_USER=postgres
DB_PASSWORD=postgres
```

---

### 4. Jalankan Aplikasi

```bash
# Linux / macOS
./gradlew run

# Windows
gradlew.bat run
```

**Atau build & jalankan sebagai JAR:**

```bash
./gradlew buildFatJar
java -jar build/libs/pam-2026-p4-ifs18005-be-all.jar
```

Verifikasi:

```bash
curl http://localhost:8000
# API Delcom - Plants & Tarot telah berjalan.
```

---

## 📡 Endpoint API

### 🌿 Plants

| Method | Endpoint | Deskripsi |
|---|---|---|
| GET | `/plants` | Ambil semua tumbuhan (`?search=`) |
| POST | `/plants` | Tambah tumbuhan (multipart/form-data) |
| GET | `/plants/{id}` | Detail tumbuhan |
| PUT | `/plants/{id}` | Update tumbuhan |
| DELETE | `/plants/{id}` | Hapus tumbuhan |
| GET | `/plants/{id}/image` | Gambar tumbuhan |

---

### 🃏 Tarots

| Method | Endpoint | Deskripsi |
|---|---|---|
| GET | `/tarots` | Ambil semua kartu (`?search=&arcana=Major\|Minor`) |
| POST | `/tarots` | Tambah kartu (multipart/form-data) |
| GET | `/tarots/{id}` | Detail kartu |
| PUT | `/tarots/{id}` | Update kartu |
| DELETE | `/tarots/{id}` | Hapus kartu |
| GET | `/tarots/{id}/image` | Gambar kartu |

**Field POST/PUT Tarots (form-data):**

| Field | Tipe | Wajib | Keterangan |
|---|---|---|---|
| `namaKartu` | text | ✅ | Nama kartu tarot |
| `arcana` | text | ✅ | `Major` atau `Minor` |
| `nomorKartu` | number | ✅ | Nomor urut kartu |
| `suits` | text | ❌ | Cups / Wands / Swords / Pentacles |
| `deskripsi` | text | ✅ | Deskripsi kartu |
| `maknaTegak` | text | ✅ | Makna posisi tegak |
| `maknaTerbalik` | text | ✅ | Makna posisi terbalik |
| `elemen` | text | ❌ | Api / Air / Tanah / Udara |
| `gambar` | file | ❌ | Gambar kartu (jpg/png) |

---

### 👤 Profile

| Method | Endpoint | Deskripsi |
|---|---|---|
| GET | `/profile` | Data profil |
| GET | `/profile/photo` | Foto profil |

---

## 📦 Contoh Request cURL

```bash
# Tambah kartu tarot
curl -X POST http://localhost:8000/tarots \
  -F "namaKartu=The Fool" \
  -F "arcana=Major" \
  -F "nomorKartu=0" \
  -F "deskripsi=Awal baru dan petualangan tanpa batas." \
  -F "maknaTegak=Awal baru, spontanitas, jiwa bebas" \
  -F "maknaTerbalik=Kecerobohan, risiko tidak perlu" \
  -F "elemen=Udara"

# Filter Major Arcana
curl "http://localhost:8000/tarots?arcana=Major"

# Cari kartu
curl "http://localhost:8000/tarots?search=fool"
```

---

## 🗂️ Struktur Proyek (File Baru)

```
src/main/kotlin/
├── dao/TarotDAO.kt             # DAO entity kartu tarot
├── data/TarotRequest.kt        # Request model tarot
├── entities/Tarot.kt           # Data class Tarot
├── repositories/
│   ├── ITarotRepository.kt     # Interface repository
│   └── TarotRepository.kt      # Implementasi repository
├── services/TarotService.kt    # Business logic tarot
└── tables/TarotTable.kt        # Schema tabel Exposed ORM
```

---

## ⚠️ Troubleshooting

| Masalah | Solusi |
|---|---|
| `Connection refused` ke PostgreSQL | `sudo service postgresql start` |
| `.env` tidak terbaca | Pastikan `.env` ada di root proyek |
| Port 8000 sudah dipakai | Ubah `APP_PORT` di `.env` |
| `gradlew: Permission denied` | `chmod +x gradlew` |

---

## 🛠️ Teknologi

- **Ktor 3.4.0** — HTTP framework
- **Exposed 0.61.0** — ORM Kotlin
- **PostgreSQL** — Database
- **Koin 4.x** — Dependency Injection
- **kotlinx.serialization** — JSON
- **dotenv-kotlin** — Environment variables
