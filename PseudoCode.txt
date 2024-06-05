HashMap(Couleur => Flottant) probabilités;
Pour chaque Couleur c: 
    probabilités(c) := Nombre de positions possible pour la couleur c / Nombre de configurations possible
Pour chaque Couleur c: 
    si Reste_à_construire(c) = 0:
        Redistribuer les probabilités de manière uniforme à toutes les autres couleurs 
Pour chaque Couleur c: 
    si Reste_à_construire(c) > Pièces_de_l_adversaire(c):
        probabilités(c) := probabilités(c) * 2
    sinon 
        probabilités(c) := probabilités(c) / 2
Normaliser(probabilités)
Pour i allant de 0 à 6 exclu:
    Pour j allant de 0 à i+1 exclu:
        Flottant f = nouveau flottant aléatoire entre 0 et 1 
        Couleur c = Tirage_probabiliste(probabilités, f)
        Montagne[i][j] := c
        Reste_à_construire(c) := Reste_à_construire(c) - 1
        si Reste_à_construire(c) = 0:
            Redistribuer les probabilités de manière uniforme à toutes les autres couleurs  