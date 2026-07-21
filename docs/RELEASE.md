# Guida per sviluppatori — Firma APK e Play Store

Questo documento è per chi vuole compilare l'APK firmato o pubblicare sul Play Store.

## 🔐 Firma APK

### 1. Genera il keystore (una volta sola)

```bash
keytool -genkeypair -v \
  -keystore ~/keystores/tieni-il-filo.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias tieni-filo-key
```

Ti chiederà password, nome, organizzazione. Scegli una password sicura.

> ⚠️ Conserva il file `.jks` e la password **in un posto sicuro**. Se li perdi, non potrai più aggiornare l'app!

### 2. Compila `keystore.properties`

Il file `keystore.properties` nella root del progetto è il template. Inserisci:
- `storeFile` → percorso assoluto del `.jks`
- `storePassword` → password keystore
- `keyAlias` → `tieni-filo-key`
- `keyPassword` → password chiave (puoi usare la stessa)

> ℹ️ `keystore.properties` è già nel `.gitignore` — non finirà mai su GitHub.

### 3. Build

```bash
# APK firmato
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk

# App Bundle (per Play Store)
./gradlew bundleRelease
# Output: app/build/outputs/bundle/release/app-release.aab
```

---

## 🌍 Pubblicare sul Play Store

1. Registrati su [Google Play Console](https://play.google.com/console) — $25 una tantum
2. Genera l'App Bundle: `./gradlew bundleRelease`
3. Carica il file `.aab` nella Console
4. Compila store listing, screenshot (min 2 per schermata), content rating, privacy policy
5. Invia per la review (~1-2 settimane)

### Requisiti store
- **App signing**: puoi delegare la gestione della chiave a Google (consigliato)
- **Screenshot**: servono screenshot reali in `docs/screens/`
- **Privacy policy**: richiesta perché l'app usa permessi CAMERA e storage
- **Content rating**: questionario IARC per classificazione età
- **Data safety form**: dichiarare che l'app non raccoglie dati personali
