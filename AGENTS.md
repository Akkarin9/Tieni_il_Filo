    # AGENTS.md

## Project
Android app "Tieni il Filo" — crochet/yarn inventory and project tracker. Spec: `TIeni_il_Filo_prompt.md`.

## Stack
- Kotlin + Jetpack Compose (Material 3)
- Room (KSP) + Hilt (kapt) + Navigation Compose
- Coil (images) + Gson (JSON backup)
- Font: Nunito via Downloadable Fonts (Google Play Services)
- Photo picker: `ActivityResultContracts.PickVisualMedia`
- Photos copied to internal storage `files/images/`, URI in Room

## JDK
- Minimum JDK 17 (AGP requirement). JDK 21 LTS recommended.
- Android Studio bundles a JDK — prefer that for non-programmers.
- No separate system JDK needed if building only inside Android Studio.

## Architecture
```
app/src/main/java/com/tieniilfilo/app/
  TieniIlFiloApp.kt / MainActivity.kt
  ui/          # App, theme, navigation, components, screens
  data/        # local (Room), repository, di, backup
  util/        # PhotoStorage, PreferencesManager
```

## Build / open in Android Studio
1. Install Android Studio (includes JDK + SDK).
2. File → Open → folder `Tieni il filo`.
3. Let it generate `gradle-wrapper.jar` and `local.properties`.
4. Run app (green play) on emulator or device.
5. CLI (optional, needs JDK 17+ on PATH): `./gradlew assembleDebug`, `./gradlew lint`, `./gradlew test`.

## Conventions (do not skip)
- Package: `com.tieniilfilo.app` · minSdk 26 · targetSdk 34
- MVVM: ViewModels expose `StateFlow`; screens use `collectAsState` / `collectAsStateWithLifecycle`
- Add/edit ALWAYS via BottomSheet modals with chips/selectors (no separate form screens)
- FAB is contextual per bottom tab (Filati / Progetti / Schemi)
- Uncinetti live under Filati (route `hooks`, not a bottom tab)
- Sample data seeded on first DB create (`DatabaseSeeder`); demo mode can wipe sample rows
- Prefer Italian UI strings
- Use skills to help you work
- Ask the user when requirements are ambiguous; propose aesthetic / performance / security improvements when relevant
- Prefer subagents for large parallel research; keep code style consistent with existing files
- Never commit secrets; never commit `local.properties` or `gradle-wrapper.jar` binaries unless requested

## Key routes
`onboarding` · `home` · `yarns` · `yarn_detail/{id}` · `hooks` · `projects` · `project_detail/{id}` · `patterns` · `pattern_detail/{id}` · `gallery` · `stats` · `settings`

## Database
Entities: Yarn, Hook, Project, ProjectPhoto, Pattern, PatternPhoto + ProjectYarnCrossRef, ProjectHookCrossRef.  
Project has optional `patternId` (1:N), not N:N pattern crossref.

## Backlog (not MVP)
- Home screen widgets · push reminders for paused projects · Google Drive sync
- Barcode/QR yarn labels · social share · i18n · Wear OS row counter · AI yarn photo recognition
