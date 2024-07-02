# Femenza+ Aplikacija

## Pregled

**Femenza+** je Android aplikacija namenjena prikazu dnevnega menija restavracije na Fakulteti za elektrotehniko, Univerza v Ljubljani. Aplikacija pridobiva meni s spletne strani fakultete in ga uporabnikom prikaže na prijazen način.

## Funkcionalnosti

- **Prikaz dnevnega menija**: Aplikacija pridobi in prikaže meni za vsak delovni dan.
- **Navigacija**: Uporabniki lahko s pomočjo spodnje navigacijske vrstice preklapljajo med različnimi dnevi v tednu.
- **Podrobnosti menija**: Podroben pogled vsakega jedilnika, vključno z imenom, tipom in opisom.
- **Celozaslonski zaslon za nalaganje**: Prikaz animiranega zaslona medtem ko se meni nalaga.
- **Obravnava brez povezave**: Ponuja povratno informacijo, če nalaganje menija ne uspe, npr. zaradi pomanjkanja internetne povezave.

## Struktura

### `DayFragment.kt`

Ta fragment je odgovoren za prikaz menija za določen dan. Inicializira RecyclerView s seznamom menijskih postavk za izbrani dan.

### `MainActivity.kt`

Glavna aktivnost, ki gostuje navigacijo in orodno vrstico. Nastavi spodnjo navigacijo in inicializira privzeti fragment glede na trenutni dan.

### `Menu.kt`

Vsebuje podatkovne razrede in nize, ki predstavljajo meni in dneve v tednu. Vključuje tudi metode za pridobitev trenutnega dneva in za razčlenitev podatkov o meniju.

### `MenuAdapter.kt`

Adapter za RecyclerView v `DayFragment.kt`, ki veže podatke o meniju na elemente v pogledu. Prav tako upravlja dogodke ob kliku za prikaz podrobnih informacij o posamezni menijski postavki.

### `MenuItem.kt`

Podatkovni razred, ki predstavlja menijsko postavko s svojimi lastnostmi, kot so ikona, naslov, tip in podrobnosti.

### `Scraper.kt`

Upravlja pridobivanje in razčlenjevanje podatkov o meniju s spletne strani fakultete z uporabo knjižnice Jsoup. Določa strukturo pridobljenih podatkov o meniju in jih shranjuje za kasnejšo uporabo v aplikaciji.

### `SplashActivity.kt`

Prikazuje začetni zaslon z animacijo medtem ko se podatki o meniju nalagajo. Prav tako upravlja prehod v `MainActivity`, ko so podatki naloženi.

## Namestitev

1. Klonirajte repozitorij:
   ```sh
   git clone https://github.com/mmzezi/FEMenzaPlus
   ```

2. Odprite projekt v Android Studiu.

3. Zgradite projekt in ga zaženite na Android napravi ali emulatorju.

## Uporaba

1. **Zaženite aplikacijo**: Odprite aplikacijo, da vidite začetni zaslon medtem ko se podatki o meniju nalagajo.

2. **Navigacija med dnevi**: Uporabite spodnjo navigacijsko vrstico za preklapljanje med različnimi dnevi v tednu in ogled njihovih menijev.

3. **Ogled podrobnosti menija**: Dotaknite se menijske postavke za prikaz podrobnih informacij o njej v dialogu.

## Prispevanje

1. Forkajte repozitorij.
2. Ustvarite novo vejo (`git checkout -b feature-branch`).
3. Naredite svoje spremembe.
4. Potrdite svoje spremembe (`git commit -am 'Dodaj novo funkcionalnost'`).
5. Potisnite na vejo (`git push origin feature-branch`).
6. Odprite pull request.


## Zahvale

- Fakulteta za elektrotehniko, Univerza v Ljubljani za zagotavljanje podatkov o meniju.
- Jsoup za razčlenjevanje HTML.

---

**Opomba**: Zamenjajte URL repozitorija in druge vrednosti z dejanskimi vrednostmi glede na vaše specifičnosti projekta.