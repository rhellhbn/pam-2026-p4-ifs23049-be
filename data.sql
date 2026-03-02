CREATE TABLE IF NOT EXISTS plants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama VARCHAR(100) NOT NULL,
    path_gambar VARCHAR(255) NOT NULL,
    deskripsi TEXT NOT NULL,
    manfaat TEXT NOT NULL,
    efek_samping TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS tarots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama_kartu VARCHAR(100) NOT NULL,
    arcana VARCHAR(10) NOT NULL CHECK (arcana IN ('Major', 'Minor')),
    nomor_kartu INTEGER NOT NULL,
    suits VARCHAR(50) NOT NULL DEFAULT '',
    deskripsi TEXT NOT NULL,
    makna_tegak TEXT NOT NULL,
    makna_terbalik TEXT NOT NULL,
    elemen VARCHAR(50) NOT NULL DEFAULT '',
    path_gambar VARCHAR(255) NOT NULL DEFAULT '',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);