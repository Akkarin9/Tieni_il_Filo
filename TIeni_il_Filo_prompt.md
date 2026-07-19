# Prompt: App Android per gestione filati e progetti di uncinetto

Sviluppa un'applicazione Android completa, pensata per un'utente appassionata di uncinetto, che le permetta di gestire in modo semplice e visivamente piacevole la sua scorta di filati, gli uncinetti e i progetti in corso.
il nome dell'applicazione sarà "Tieni il Filo"

## Stack tecnico
- Kotlin + Jetpack Compose (Material 3) per l'interfaccia
- Room database per la persistenza locale dei dati (nessun backend richiesto, app offline-first)
- Architettura MVVM
- Supporto salvataggio foto locali (filati, progetti, schemi)

## Design e stile
- Grafica carina, calda e accattivante: palette pastello (rosa cipria, lavanda, verde salvia, crema), font arrotondati e amichevoli
- Icone illustrate a tema handmade/crochet (gomitoli, uncinetti, matassine)
- Card con angoli arrotondati, ombre soft, transizioni fluide tra le schermate
- Dark mode opzionale con palette coordinata
- Home screen con dashboard: statistiche rapide (n° filati in scorta, progetti attivi, progetti completati)

## Funzionalità principali

### 1. Gestione filati (Magazzino filati)
- Scheda per ogni filato con: nome/marca, colore (nome + eventuale codice colore + color swatch visivo), composizione (lana, cotone, misto, ecc.), peso/spessore (fingering, DK, worsted, ecc.), quantità posseduta (gomitoli/grammi/metri), foto del filato
- Stato: "disponibile", "in uso", "esaurito"
- Collegamento del filato ai progetti in cui è stato/viene usato
- Filtri e ricerca per colore, marca, composizione, stato
- Possibilità di segnare quantità residua quando un filato viene consumato parzialmente

### 2. Gestione uncinetti
- Elenco uncinetti posseduti con misura (mm), materiale (metallo, bamboo, ergonomico, ecc.), marca
- Collegamento uncinetto ↔ progetto (quale uncinetto è stato usato per quale lavoro)

### 3. Gestione progetti
- Scheda progetto con: nome, foto del lavoro (anche più foto, es. avanzamento nel tempo), data inizio/fine, stato (in corso, in pausa, completato, da iniziare)
- Filati usati nel progetto (collegati al magazzino filati) e uncinetto/i usati
- Note libere (es. numero di punti, varianti fatte)
- Collegamento allo schema/pattern usato

### 4. Gestione schemi/pattern
- Libreria schemi: possibilità di caricare PDF, immagini o inserire link esterni (es. Ravelry, blog)
- Categorizzazione per tipo (amigurumi, abbigliamento, accessori, coperte, ecc.)
- Collegamento schema ↔ progetti realizzati con quello schema
- Segnalibro/preferiti per schemi da provare in futuro

### 5. Extra piacevoli
- Galleria fotografica generale di tutti i progetti completati (tipo portfolio)
- Promemoria/lista desideri filati da comprare
- Statistiche simpatiche (es. "quanti gomitoli usati questo mese", colore più usato)
- Backup/esportazione dati locale (es. file JSON) per non perdere i dati

## Navigazione
Bottom navigation bar con 4 sezioni principali: **Home**, **Filati**, **Progetti**, **Schemi**, con un pulsante flottante "+" contestuale per aggiungere rapidamente un elemento nella sezione corrente.

## Note implementative
- Priorità a un'esperienza fluida su mobile, con form di inserimento rapidi (poche schermate, molte scelte tramite chip/selettori invece di digitare tutto)
- Le foto vanno salvate in locale collegate via URI al record del database
- Prevedere dati di esempio precaricati al primo avvio per mostrare come si presenta l'app vuota vs piena