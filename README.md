# Mokėjimų rizikos vertinimo sistema – prototipas

Kursinio darbo II dalis. Organizacija per API nusiunčia mokėjimą, sistema pritaiko taisykles R1–R4,
apskaičiuoja balą ir grąžina sprendimą: `APPROVE`, `REVIEW` arba `BLOCK`.
Projektavimo dokumentas: [projektavimo-dokumentas.md](projektavimo-dokumentas.md).

## Reikalingi įrankiai

| Įrankis | Versija |
|---|---|
| Java (JDK) | 21 |
| Maven | nereikia diegti, naudojamas `mvnw` (įtrauktas į projektą) |
| Spring Boot | 4.1.2 |
| Duomenų bazė | H2 atmintyje (paleidžiama kartu su programa, diegti nereikia) |

## Konfigūracija

Papildomai nieko nustatyti nereikia. Nustatymai yra `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:riskdb
spring.jpa.defer-datasource-initialization=true
spring.h2.console.enabled=true
```

API raktai `demo-key-a` ir `demo-key-b` yra tik demonstraciniai, ne tikri slapti raktai.
Jie įrašomi į duomenų bazę iš `src/main/resources/data.sql`.

## Paleidimas

Windows:
```bash
.\mvnw.cmd spring-boot:run
```
Linux / macOS:
```bash
./mvnw spring-boot:run
```

Kai programa pasileidžia:
- Swagger (API bandymui): http://localhost:8080/swagger-ui/index.html
- H2 konsolė: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:riskdb`, vartotojas `sa`, slaptažodis tuščias)

Duomenų bazė yra atmintyje: kiekvieną kartą paleidus programą ji sukuriama iš naujo su pavyzdiniais duomenimis.

## Testai

```bash
.\mvnw.cmd test
```

| Testų klasė | Ką tikrina |
|---|---|
| `RiskEngineTest` | Pagrindinis modulis: įprastas atvejis (APPROVE), ribinis atvejis (balas lygiai 40 → REVIEW), griežtesnis profilis (BLOCK) |
| `PaymentApiTest` | Klaida (neteisinga valiuta → 400, niekas neišsaugoma), neteisingas raktas → 401, **organizacijų duomenų izoliacija** (A negali perskaityti B mokėjimo → 404) |
| `R1`–`R4...RuleTest` | Kiekviena taisyklė atskirai, įskaitant ribas (pvz., 04:59 / 05:00) |

## Pavyzdiniai duomenys

| Organizacija | API raktas | REVIEW nuo | BLOCK nuo |
|---|---|---|---|
| `ORG_A` (Shop A) | `demo-key-a` | 40 | 70 |
| `ORG_B` (Shop B) | `demo-key-b` | 25 | 40 |

| Organizacija | Klientas | Istorija |
|---|---|---|
| ORG_A | C1 | 10 mokėjimų po 40 €, gavėjas R-12 |
| ORG_A | C2 | 8 mokėjimai po 300 € (vidurkis 300 €), gavėjas R-10 |
| ORG_B | C2 | 2 mokėjimai po 100 €, gavėjas R-50 (atskira ORG_B istorija) |

Taisyklių balai (visoms organizacijoms): R1 = 30, R2 = 25, R3 = 40, R4 = 15. Balas daugiausia 100.

## API

| Metodas | Adresas | Aprašymas |
|---|---|---|
| `POST` | `/payments` | Įvertinti mokėjimą (antraštė `X-API-Key`) |
| `GET` | `/payments/{id}` | Peržiūrėti savo organizacijos išsaugotą mokėjimą |

## Demonstracijos žingsniai

Swagger lange pasirinkite `POST /payments` → *Try it out*, įrašykite `X-API-Key` ir užklausos turinį.

**1. Įprastas mokėjimas** (raktas `demo-key-a`):
```json
{"clientId":"C1","amount":20,"currency":"EUR","recipientId":"R-12","country":"LT","timestamp":"2026-11-10T14:30:00"}
```
Laukiama: `APPROVE`, balas 0.

**2. Ribinis atvejis** (raktas `demo-key-a`):
```json
{"clientId":"C2","amount":1000,"currency":"EUR","recipientId":"R-77","country":"LT","timestamp":"2026-11-10T03:00:00"}
```
Laukiama: `REVIEW`, balas 40 (R2 +25, R4 +15).

**3. Kitas organizacijos profilis**: tas pats mokėjimas su raktu `demo-key-b`.
Laukiama: `BLOCK`, balas 40. Įsiminkite grąžintą `paymentId`.

**4. Klaida**: 2 žingsnio mokėjimas, bet `"amount": -50` arba `"currency": "XXX"`.
Laukiama: `400`, `{"error":"INVALID_PAYMENT", "reason": ...}`.

**5. Neteisingas raktas**: bet kuri užklausa su raktu `wrong-key`. Laukiama: `401`.

**6. Duomenų izoliacija**: `GET /payments/{paymentId iš 3 žingsnio}`:
- su raktu `demo-key-b` → `200`, matomas mokėjimas;
- su raktu `demo-key-a` → `404`, ORG_A negali matyti ORG_B duomenų.

Pastaba: kiekvienas mokėjimas išsaugomas istorijoje. Pakartojus 2 žingsnį, R-77 jau bus žinomas gavėjas,
todėl sprendimas pasikeis. Norint pradėti iš naujo, programą reikia paleisti iš naujo.

## Pakeitimai nuo I dalies

| I dalyje planuota | Prototipe | Kodėl |
|---|---|---|
| PostgreSQL | H2 atmintyje | Prototipas turi pasileisti viena komanda. PostgreSQL ir Docker – III daliai |
| Profilio keitimas (`PUT /profile`) | Slenksčiai saugomi kiekvienai organizacijai, balai bendri | Organizacijos konfigūracija numatyta III daliai |
| Peržiūros eilė, R5 eksperimentas | Neįgyvendinta | III dalis (kokybės atributo įrodymas) |
| API raktai kaip maišos | Demonstraciniai raktai atviru tekstu | Prototipo supaprastinimas |
| Svetimi duomenys – 403 | 404 | Užklausa filtruoja pagal organizaciją, todėl svetimas mokėjimas tiesiog nerandamas |
