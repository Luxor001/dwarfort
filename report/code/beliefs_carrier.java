strength_kg(10).
carrying(Resource, 0).
atCapacity :- carrying(_, Kg) & strength_kg(S) & S <= Kg.

storageKg(gold, 0).
storageKg(steel, 0).

//goCollect(Resource)

//cave(Index, Miner, X, Y)
//caveE(Index, Miner, X, Y)
cavesEntrances(Entrances) :- .findall(cave(I, Miner, X, Y), cave(I, Miner, X, Y), Entrances).