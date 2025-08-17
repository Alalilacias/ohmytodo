# Refactor Log

Aquest document ha estat creat per detallar els canvis realitzats durant la refactorització del projecte OhMyTodo realitzat durant els dies 07/08/2025 y 13/08/2025 amb el fi de corregir els problemes que em va mencionar en Pere en l'entrevista realitzada el 07/08/2025.

Amb el propòsit de sintetitzar l'informació, he agrupat els canvis en vers les tecnologies o parts del programa que es veuen afectades per ells. A sota trobareu una taula de continguts amb els distints grups.

## Tabla de continguts

1. [Docker](#docker)
2. [Redis](#redis)
3. [MVC](#mvc)

## Docker

Els canvis a Docker han estat la modificació de les imatges triades, per reduir la mida i temps de construcció i alçament de les imatges. Abans de començar la refactorització, les mides eren les següents:

```bash
IMAGE                SIZE
ohmytodo-app         522MB
postgres:16          436MB
redis:7              117MB

TOTAL                1075MB
```

La primera refactorització realitzada, feta amb el principal propòsit de probar la compatibilitat d'altres imatges amb menys pes, va donar el següent resultat:

```
IMAGE                SIZE
ohmytodo-app         280MB
postgres:16-alpine   276MB
redis:7-alpine       41.4MB

TOTAL                597.4MB
```

Durant aquesta prova, es va comprobar lo que en Pere va dir durant l'entrevista, que triant l'imatge correcte, es podia disminuir la mida de l'imatge de manera significant.

No obstant això, com també es va parlar durant l'entrevista, la presencia de Redis no era necessària i, com es pot llegir en l'apartat de Redis, tota la seva estructura ha sigut retirada. El resultat es el següent:

```
IMAGE                SIZE
ohmytodo-app         270MB
postgres:16-alpine   276MB

TOTAL                546MB
```

Si be aquest resultat es menys dràstic que el canvi d'imatges, en la práctica, en quant a us de recursos del contenidor sera menor que abans.

## Redis

Com vam parlar durant l'entrevista, si be no era necessari utilitzar Redis, fer servir aquesta tecnologia era algo que em feia il·lusió. Ara be, es cert que en el cas actual, no hauria sigut necessari. Per lo tant, amb el propòsit de ser mes coherent amb l'informació que vaig rebre (i aprendre) durant l'entrevista, he decidit canviar la persistencia de les cookies.

En aquest cas, he trobat que configurar JDBC, l'altre alternativa que he trobat recomanada a Stack Overflow, tindria un efecte similar a configurar Redis, que es fer servir un sistema de persistencia innecessari per les necessitats d'un sistema com el necessari per aquesta prova. Per aquest motiu, he triat el mètode mes senzill: persistencia en memoria del contenidor durant la seva durada. No es persisteixen les galetes desprès de baixades i pujades del contenidor, pero el sistema no canvia el seu funcionament. Com es pot comprovar en el commit `9e5de6980024755c77dca7c39436a2b21bc199fe` en el que es retira la configuració de Redis, el canvi no ha afectat l'estructura del projecte.

## MVC

Com vam parlar durant l'entrevista, no vaig entendre be la preferencia per Thymeleaf i el model MVC. Com a resultat, vaig fer servir Javascript per poblar el front. En aquest cas, el que he fet ha sigut una reestructuració del back i el front amb el propòsit de fer servir el model MVC i un us mes fidel de la filosofia i el funcionament desitjat per Thymeleaf.

Aquesta reestructuració va implicar la modificació de les vistes i la manera en la que s'organitzen, els controladors, els serveis i, fins it tot el repositori, per adaptar-los als requeriments per poder fer una adequada gestió i gestió de possibles problemes en l'execució del programa.

Per més informació, els commits, si bé menys verbosos que els inicials, contenen prou informació com per entendre tant els canvis com els motius per les eleccions fetes.

Al final d'aquesta reestructuració tenim tant les funcionalitats REST com les MVC, tot I que el front només treballa amb els endpoints MVC.