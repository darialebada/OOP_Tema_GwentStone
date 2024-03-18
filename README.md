### Lebădă Daria-Cristiana, 323CA ###
## OOP_Tema_GwentStone ##

# Introducere
Se simuleaza un joc de carti (2 jucatori) care contine elemente din
jocurile Hearthstone și Gwent.

# Clasele implementate
Am implementat propria clasă Card pentru stocarea informațiilor
pentru fiecare carte. Aceasta este moștenită de alte 3 clase,
reprezentând cele 3 tipuri de cărți disponibile în joc: minion,
environment și hero. În funcția care se moștenește rețin atributele
cărții (healt, name, mana etc), în timp ce clasele moștenite stochează
anumite informații referitoarea la starea în care se află cartea la un
anumit moment (frozen, a atacat).
Pentru o prelucrare mai ușoară a datelor de joc, am creat alte
2 clase: 
- GameActions care reține informațiile administrative ale jocului
(comenzi, datele pentru începerea jocului);
- Player care reține informațiile pentru un player (deck-uri,
cărți în mână, cărți pe tabla de joc, erou, mana).
Mai există și clase auxiliare pentru o gestiune mai bună a pregătirii
jocului și a comenzilor -> Action, StartGame.


# Modul de desfășurare
Avem un ArrayList<Player> de dimensiune 2 în care reținem datele
pentru fiecare jucător. În clasa PrepareGame se face mutarea transferarea
datelor din tipurile din fileio în noile clase (CardInput -> Card,
GameInput -> GameActions etc).
În clasa Gameplay se parsează comenzile date la input și se apelează
funcțiile corespunzătoare instrucțiunilor.

# Descriere a anumitor funcții
Pentru acțiunile în care cărțile își utilizează abilitățile sau pur și
simplu doar atacă se urmează aceeași pași: se verifică dacă acțiunea dorită
este validă și se tratează eventualele erori, apoi se aplică abilitatea/ atacul.
Pentru fiecare acțiune trebuie modificate toate cărțile implicate, după caz
(cartea care atacă este trecută că a atacat, iar cea atacată este modificată
în funcție de ce i se aplică).
	
# Probleme întâmpinate
Scrierea datelor în format Json & Jackson a fost o adevărată provocare,
dar după ce m-am documentat și am înțeles cum funcționează a fost mai ușor.





