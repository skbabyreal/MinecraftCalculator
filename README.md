# MinecraftCalculator

Plugin Spigot 1.21 che aggiunge una calcolatrice utilizzabile direttamente in gioco tramite una GUI.

## Funzionalità

- Interfaccia grafica configurabile.
- Operazioni di addizione, sottrazione, moltiplicazione e divisione.
- Supporto per numeri decimali.
- Pulsanti per cancellare o eliminare l'ultimo carattere.
- Messaggio del risultato in chat configurabile.
- Messaggi e materiali personalizzabili in `config.yml`.

## Requisiti

- Java 21 o superiore.
- Spigot 1.21 o compatibile.

## Installazione

1. Compila il progetto con Maven:

   ```bash
   mvn clean package
   ```

2. Copia il JAR generato da `target/` nella cartella `plugins/` del server.
3. Avvia o riavvia il server.
4. Personalizza `plugins/MinecraftCalculator/config.yml` se necessario.

## Utilizzo

Apri la calcolatrice con:

```text
/calculator
```

Alias disponibili: `/calc`, `/calcui`.

Permesso di utilizzo: `minecraftcalculator.use`  
Permesso di ricarica: `minecraftcalculator.reload`

Per ricaricare la configurazione:

```text
/calculator reload
```

## Licenza

Questo progetto non specifica ancora una licenza.
