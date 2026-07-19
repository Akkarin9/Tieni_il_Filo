# Tieni il Filo

App Android per tenere traccia del tuo armadio di filati, progetti a uncinetto, schemi e uncinetti.

## Funzionalità

- **Filati**: catalogo con nome, marca, colore, composizione, quantità (gomitoli/grammi/metri), fonte (negozio fisico/online), foto. Filtri per stato, composizione e ricerca.
- **Uncinetti**: gestione dimensioni, materiali e marche.
- **Progetti**: crea progetti, collegali a filati e schemi, aggiungi foto di progresso, segna come completato.
- **Schemi**: importa PDF, immagini o link da Ravelry/blog, organizzali per tipo, aggiungi ai preferiti.
- **Backup JSON**: esporta e importa tutti i dati.
- **Tema scuro**: supporto nativo.
- **Foto**: scatta dalla fotocamera o scegli dalla galleria.

## Stack

- **Kotlin** + Jetpack Compose (Material 3)
- **Room** (KSP) — database locale SQLite
- **Hilt** (kapt) — dependency injection
- **Navigation Compose** — routing
- **Coil** — caricamento immagini
- **Gson** — serializzazione backup

## Stato

App funzionante e in uso. Migrazioni Room: versione 4.

---

*Vibe coded with [opencode.ai](https://opencode.ai)*
