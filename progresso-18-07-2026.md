# Progresso — 18/07/2026

## Completato
- Scaffolding completo: Gradle KTS, Hilt, Room, Navigation Compose, Coil, Gson
- Theme aggiornato: palette più contrasti (Opzione C — Marrone/Pesca/Salvia)
- Onboarding funzionante e navigazione base
- Schermate: Home dashboard, Filati, Uncinetti, Progetti, Schemi, Galleria, Stats, Settings
- Import/export JSON backup, dark mode, foto in internal storage
- Fissato white screen (import mancante in MainActivity)
- MIGRATION_2_3: refact tabella yarns (quantity_balls_total, quantity_grams_total, quantity_meters_total, yarn_source, store_name, store_link)
- MIGRATION_3_4: colonna custom_composition per composizione personalizzata (Misto/Altro)
- **Filati**: pulsante modifica (riusa bottom sheet con pre-popolamento), composizione personalizzata con campo testo per Misto/Altro, foto da fotocamera (TakePicture + permesso CAMERA runtime + FileProvider)
- **Schemi**: eliminazione (icona cestino + conferma dialog), import PDF/IMAGE da sorgente, anteprima foto e apertura PDF via Intent, fotocamera per IMAGE
- **Progetti**: eliminazione (icona cestino + conferma dialog), foto progresso da fotocamera (Galleria + Fotocamera)
- **Infrastruttura**: FileProvider (res/xml/file_paths.xml), permesso CAMERA in manifest
- **Fix**: MIGRATION_2_3 usava column name errato `color_name` invece di `colorName` — aggiunto @ColumnInfo a YarnEntity.colorName

## Da risolvere (prossima sessione)
1. **Tasto Home non funziona** — il pulsante Home nella bottom bar non naviga correttamente
