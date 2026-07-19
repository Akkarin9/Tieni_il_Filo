package com.tieniilfilo.app.data.local.entity

enum class YarnStatus {
    DISPONIBILE,
    IN_USO,
    ESAURITO,
}

enum class YarnComposition {
    LANA,
    COTONE,
    MISTO,
    ACRILICO,
    ALPACA,
    MERINO,
    BAMBOO,
    SETA,
    LINO,
    ALTRO,
}

enum class YarnSource {
    NEGOZIO_FISICO,
    ONLINE,
}

enum class HookMaterial {
    METALLO,
    BAMBOO,
    ERGONOMICO,
    PLASTICA,
    LEGNO,
    ALTRO,
}

enum class ProjectStatus {
    IN_CORSO,
    IN_PAUSA,
    COMPLETATO,
    DA_INIZIARE,
}

enum class PatternType {
    AMIGURUMI,
    ABBIGLIAMENTO,
    ACCESSORI,
    COPERTE,
    CASA,
    ALTRO,
}

enum class PatternSourceType {
    PDF,
    IMAGE,
    LINK,
}
