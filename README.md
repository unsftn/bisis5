BISIS
=====

Potreban softver
----------------

### JDK 1.8 ###

Za projekat koristimo JDK minimalno u verziji 1.8.

### Gradle ###

[Gradle](http://www.gradle.org) je novi build sistem koji bi trebalo da nasledi
Ant i Maven, a da pri tome bude jednostavniji za korišćenje. Gradle se
instalira prema uputstvu sa svog sajta.

### Eclipse ili IntelliJ IDEA ###

Možemo koristiti Eclipse (napunjen pluginovima za Subversion, Gradle, TestNG i
još 100 drugih stvari) ili IntelliJ IDEA (koji sve pakuje sa sobom).

### TomEE ###

Serverski deo aplikacije će biti instaliran kao war fajl na
[TomEE](http://tomee.apache.org) serveru, i to distribucija TomEE Plus, verzija
minimalno 1.7.1.

### MongoDB ###

Kao baza podataka koristi se [MongoDB](http://www.mongodb.org). Treba ga
instalirati u skladu sa uputstvom za dati operativni sistem.

Layout projekta
---------------

Raspored fajlova u projektu se oslanja na default raspored koji koristi Gradle,
a koji je najvećim delom preuzet od Mavena. Na taj način build skript ostaje
jednostavan.

Bildovanje projekta
-------------------

U konzoli se treba pozicionirati u root direktorijum projekta. Čišćenje
direktorijuma od svih nusproizvoda dobija se pomoću:

    $ gradle clean

Pokretanje svih testova:

    $ gradle test

Pravljenje war fajla:

    $ gradle war

Gradle će povući sve potrebne biblioteke preko weba i upakovati ih tamo gde
treba.

Pokretanje projekta
-------------------

### Početno stanje u bazi ###

Inicijalno je potrebno kreirati bazu **bisis** na Mongu, i dodati kolekcije i
dokumente date u skriptu (TODO).

### Podešavanje TomEE-ja ###

TomEE treba da radi na istoj mašini na kojoj radi i MongoDB. Kopiranje fajla
*bisis5.war* u njegov *webapps* folder je sve što je potrebno za instalaciju.

### Klijentska aplikacija ###

TODO

Razvoj i testiranje
-------------------

Za unit testove koristi se [TestNG](http://testng.org) framework. Gradle skript
je već podešen za to. Kada se testira pristup web servisu, ne mora se pokretati
ceo TomEE, već se u okviru testova koristi embedded verzija servera koju
implementira CXF biblioteka za web servise (ona se nalazi i u TomEE-ju, tako da
je test realan).

Za izvršavanje TomEE-ja u okviru Eclipse ili IntelliJ potrebno je registrovati
server i obaviti odgovarajuća podešavanja za projekat u svakom od IDE-a.

Produkcioni server
------------------

### HTTPS ###

Da bi autentifikacija klijenata imala smisla, u produkciji je obavezno
koristiti HTTPS konekciju. Za to je najbolje podići Apache server ispred
TomEE-ja, konfigurisati SSL i sertifikate na njemu, a za komunikaciju sa
TomEE-jem koristiti ProxyPass direktivu, kao u sledećem primeru.

    <VirtualHost *.443>
        ServerName app.bisis.rs
        ProxyPass / http://localhost:8080/
        ProxyPreserveHost On

        SSLEngine On
        SSLCertificateKeyFile /etc/pki/tls/private/bisis.key
        SSLCertificateFile /etc/pki/tls/certs/bisis.crt
        SSLCertificateChainFile /etc/pki/tls/certs/bisis-intermediate.crt
    </VirtualHost>

    <VirtualHost *.80>
        RewriteEngine On
        RewriteCond %{HTTPS} != on
        RewriteRule (.*) https://${HTTP_HOST}${REQUEST_URI} [NC,R=301,L]
    </VirtualHost>

