package com.tieniilfilo.app.data.local

import com.tieniilfilo.app.data.local.crossref.ProjectHookCrossRef
import com.tieniilfilo.app.data.local.crossref.ProjectYarnCrossRef
import com.tieniilfilo.app.data.local.entity.HookEntity
import com.tieniilfilo.app.data.local.entity.HookMaterial
import com.tieniilfilo.app.data.local.entity.PatternEntity
import com.tieniilfilo.app.data.local.entity.PatternSourceType
import com.tieniilfilo.app.data.local.entity.PatternType
import com.tieniilfilo.app.data.local.entity.ProjectEntity
import com.tieniilfilo.app.data.local.entity.ProjectStatus
import com.tieniilfilo.app.data.local.entity.YarnComposition
import com.tieniilfilo.app.data.local.entity.YarnEntity
import com.tieniilfilo.app.data.local.entity.YarnSource
import com.tieniilfilo.app.data.local.entity.YarnStatus

object DatabaseSeeder {

    suspend fun seed(db: AppDatabase) {
        val yarnDao = db.yarnDao()
        val hookDao = db.hookDao()
        val projectDao = db.projectDao()
        val patternDao = db.patternDao()

        val hook1 = hookDao.insert(HookEntity(sizeMm = 3.5, material = HookMaterial.ERGONOMICO, brand = "Clover Amour"))
        val hook2 = hookDao.insert(HookEntity(sizeMm = 4.0, material = HookMaterial.METALLO, brand = "Prym"))
        val hook3 = hookDao.insert(HookEntity(sizeMm = 5.0, material = HookMaterial.BAMBOO, brand = "KnitPro"))
        val hook4 = hookDao.insert(HookEntity(sizeMm = 2.5, material = HookMaterial.METALLO, brand = "Tulip"))

        val yarn1 = yarnDao.insert(
            YarnEntity(
                name = "Merino Baby", brand = "Lana Grossa", colorName = "Rosa Cipria",
                colorHex = 0xFFF4C2C2.toInt(), composition = YarnComposition.MERINO,
                quantityBallsTotal = 5.0, quantityGramsTotal = 250.0, quantityMetersTotal = 600.0,
                quantityUsed = 30.0, status = YarnStatus.IN_USO,
                notes = "Perfetto per amigurumi morbidi", isSample = true,
                yarnSource = YarnSource.NEGOZIO_FISICO, storeName = "Merceria Bella",
            )
        )
        val yarn2 = yarnDao.insert(
            YarnEntity(
                name = "Cotton Soft", brand = "Schachenmayr", colorName = "Verde Salvia",
                colorHex = 0xFFC2E0C6.toInt(), composition = YarnComposition.COTONE,
                quantityBallsTotal = 3.0, quantityGramsTotal = 150.0, quantityMetersTotal = 360.0,
                quantityUsed = 0.0, status = YarnStatus.DISPONIBILE,
                notes = "Ideale per accessori estivi", isSample = true,
                yarnSource = YarnSource.ONLINE, storeLink = "https://ravelry.com/shop/example",
            )
        )
        val yarn3 = yarnDao.insert(
            YarnEntity(
                name = "Alpaca Blend", brand = "Drops", colorName = "Lavanda",
                colorHex = 0xFFC4C4F7.toInt(), composition = YarnComposition.ALPACA,
                quantityBallsTotal = 3.0, quantityGramsTotal = 150.0, quantityMetersTotal = 360.0,
                quantityUsed = 40.0, status = YarnStatus.IN_USO,
                notes = "Caldo e soffice, perfetto per sciarpe", isSample = true,
                yarnSource = YarnSource.NEGOZIO_FISICO, storeName = "Filo e Fantasia",
            )
        )
        val yarn4 = yarnDao.insert(
            YarnEntity(
                name = "Bamboo Mix", brand = "Katia", colorName = "Crema",
                colorHex = 0xFFFFFDD0.toInt(), composition = YarnComposition.BAMBOO,
                quantityBallsTotal = 4.0, quantityGramsTotal = 200.0, quantityMetersTotal = 480.0,
                quantityUsed = 0.0, status = YarnStatus.DISPONIBILE, isSample = true,
                yarnSource = YarnSource.ONLINE, storeLink = "https://amazon.it/dp/example",
            )
        )
        val yarn5 = yarnDao.insert(
            YarnEntity(
                name = "Wool Classic", brand = "Rowan", colorName = "Coral",
                colorHex = 0xFFE8A090.toInt(), composition = YarnComposition.LANA,
                quantityBallsTotal = 6.0, quantityGramsTotal = 300.0, quantityMetersTotal = 720.0,
                quantityUsed = 0.0, status = YarnStatus.DISPONIBILE, isSample = true,
            )
        )
        yarnDao.insert(
            YarnEntity(
                name = "Baby Alpaca", brand = "Debbie Bliss", colorName = "Grigio Perla",
                colorHex = 0xFFC0C0C0.toInt(), composition = YarnComposition.MISTO,
                customComposition = "50% Alpaca, 50% Lana Merino",
                quantityBallsTotal = 4.0, quantityGramsTotal = 200.0, quantityMetersTotal = 480.0,
                quantityUsed = 0.0, status = YarnStatus.DISPONIBILE, isSample = true,
            )
        )
        yarnDao.insert(
            YarnEntity(
                name = "Silk Dream", brand = "Malabrigo", colorName = "Bordeaux Seta",
                colorHex = 0xFF8B1A1A.toInt(), composition = YarnComposition.SETA,
                quantityBallsTotal = 1.0, quantityGramsTotal = 50.0, quantityMetersTotal = 120.0,
                isWishlist = true, isSample = true,
            )
        )

        val pat1 = patternDao.insert(
            PatternEntity(
                title = "Amigurumi Coniglietto", type = PatternType.AMIGURUMI, sourceType = PatternSourceType.LINK,
                externalLink = "https://ravelry.com/patterns/example-bunny", isBookmarked = true,
                notes = "Livello facile, perfetto per regali", isSample = true,
            )
        )
        val pat2 = patternDao.insert(
            PatternEntity(
                title = "Coperta Granny Square", type = PatternType.COPERTE, sourceType = PatternSourceType.PDF,
                fileUri = "sample_granny_pattern.pdf", notes = "Usare avanzi di filato colorato", isSample = true,
            )
        )
        patternDao.insert(
            PatternEntity(
                title = "Cardigan Primavera", type = PatternType.ABBIGLIAMENTO, sourceType = PatternSourceType.LINK,
                externalLink = "https://ravelry.com/patterns/spring-cardigan", isBookmarked = true, isSample = true,
            )
        )

        val proj1 = projectDao.insert(
            ProjectEntity(
                name = "Coniglietto per Sofia", status = ProjectStatus.IN_CORSO,
                startDate = System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000L,
                patternId = pat1, notes = "Usare uncinetto 3.5, seguire schema originale.", isSample = true,
            )
        )
        val proj2 = projectDao.insert(
            ProjectEntity(
                name = "Coperta della nonna", status = ProjectStatus.IN_PAUSA,
                startDate = System.currentTimeMillis() - 60 * 24 * 60 * 60 * 1000L,
                patternId = pat2, notes = "Quadrati 20x20 cm. Ne ho fatti 12 su 30.", isSample = true,
            )
        )
        val proj3 = projectDao.insert(
            ProjectEntity(
                name = "Sciarpa in Alpaca", status = ProjectStatus.COMPLETATO,
                startDate = System.currentTimeMillis() - 45 * 24 * 60 * 60 * 1000L,
                endDate = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000L,
                notes = "Venuta benissimo!", isSample = true,
            )
        )
        projectDao.insert(
            ProjectEntity(
                name = "Top estivo in cotone", status = ProjectStatus.DA_INIZIARE,
                patternId = pat2, notes = "Comprare il cotone verde salvia", isSample = true,
            )
        )

        projectDao.insertYarnCrossRef(ProjectYarnCrossRef(proj1, yarn1))
        projectDao.insertYarnCrossRef(ProjectYarnCrossRef(proj2, yarn2))
        projectDao.insertYarnCrossRef(ProjectYarnCrossRef(proj2, yarn5))
        projectDao.insertYarnCrossRef(ProjectYarnCrossRef(proj3, yarn3))
        projectDao.insertHookCrossRef(ProjectHookCrossRef(proj1, hook1))
        projectDao.insertHookCrossRef(ProjectHookCrossRef(proj2, hook3))
        projectDao.insertHookCrossRef(ProjectHookCrossRef(proj3, hook2))
    }
}
