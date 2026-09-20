Am implementat marsGrid utlizand clasa centrala GridController,care modeleaza toate componentele retelei,stocand lista de producatori,consumatori si baterii,impreuna cu strea sitemului esteInBlackout si istoricul evenimentului,istoricEvenimente.

Functile principale pt gestionarea retelei sunt:
-adaugarea de componente:Motodele addProducator,addConsumator,addBaterie, care verifica datele de intrare,id unic,putere sau capacitate pozitiva,si adauga obiectele in listele specifice lor
-statusGrid afiseaza starea detaliata a fiecarei componente si starea generala a retelei (Stabil/BLACKOUT)
-istoricEvenimente afiseaza evenimentele majore inregistrate pe parcursul simularii
-setDefect permite schimbarea  statusOperational (true/false) al oricarei componente existente

Ierarhia de clase extinde clasa de baza abstracta ComponentaRetea :

-ProducatorEnergie (Abstract): Implementeaza logica de productie in subclasele concrete: 
--PanouSolar (depinde de factorSoare)
--TurbinaEoliana (depinde de factorVant) 
--ReactorNuclear (productie constanta)
Acestea aplica verificarea isStatusOperational  in calculeazaProductie si returneaza 0 daca sunt defecte

-ConsumatorEnergie (Abstract):
--Are atributele cerereEnergie si prioritate
Subclasele :
--SistemSuportViata cu prioritate 1
--LaboratorStiintific cu prioritate 2 
-- SistemIluminat cu prioriatet3
seteaza automat prioritatea pt fiecare


-Baterie (Concreta):
Gestioneaza stocarea si eliberarea energiei prin metodele:
--incarca(surplus) 
--descarca(cerere)
Verifica  isStatusOperational pentru a contribui

Metoda simuleazaTick(factorSoare, factorVant) urmeaza urmatorii pasi:

- Resetez toti consumatorii la starea "alimentat"
- Calculez cat produc in total toti producatorii. Pentru fiecare verific daca e operational si ii dau factorul potrivit (soare pt panouri, vant pt turbine, 0 pt reactor).
- Calculez cat cere in total toata reteaua (suma cererilor consumatorilor alimentati).
- Fac diferenta (producție - cerere). Acum pot fi 2 cazuri:
--- Daca am SURPLUS (delta > 0):

Parcurg bateriile si le incarc cu surplusul
Ce nu incape in baterii se pierde

--- Daca am DEFICIT (delta < 0):

Incerc sa descarc bateriile pentru a acoperi deficitul
Daca mai ramane deficit, intru in TRIAGE - decuplez consumatorii in ordine (prioritate 3, apoi 2). Prioritatea 1 nu se atinge NICIODATA!
Am facut o functie separata executaTriage() pentru asta, ca sa fie mai clar
Daca nici dupa triage nu ajunge energie, intru in BLACKOUT si opresc tot

La final afisez un rezumat cu productia, cererea, starea bateriilor si cine a fost decuplat
La Triage am folosit sort() pentru a ordona consumatorii dupa prioritate (de la 3 la 1).


BONUS:
-intrebarea1:

1.daca se supraincarca o bateri cu o cantitate uriasa de energie,ar putea fi overflow
2.Factori in afra intervalului [0,1]-> FactorSoare si factorvant ar trebui sa fie procente 
3.daca toti producatorii sunt defecti, productia totala este 0.chiar daca bateriile sunt pline, la un moment dat se vor goli si intra in blackout.
