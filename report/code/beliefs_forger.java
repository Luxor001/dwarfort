thirstiness(0). // grado di sete dell'agente
thirsty :- thirstiness(N) & N >= 10.
strength_kg(10). // forza personale del minatore
carrying(Resource, 0). // kg di materiale in trasporto
atCapacity :- carrying(_, Kg) & strength_kg(S) & S <= Kg.