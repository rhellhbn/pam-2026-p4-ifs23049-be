package org.delcom.module

import org.delcom.repositories.IPlantRepository
import org.delcom.repositories.ITarotRepository
import org.delcom.repositories.PlantRepository
import org.delcom.repositories.TarotRepository
import org.delcom.services.PlantService
import org.delcom.services.ProfileService
import org.delcom.services.TarotService
import org.koin.dsl.module


val appModule = module {
    // Plant Repository
    single<IPlantRepository> {
        PlantRepository()
    }

    // Plant Service
    single {
        PlantService(get())
    }

    // Tarot Repository
    single<ITarotRepository> {
        TarotRepository()
    }

    // Tarot Service
    single {
        TarotService(get())
    }

    // Profile Service
    single {
        ProfileService()
    }
}