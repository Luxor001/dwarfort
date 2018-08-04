thirstiness(0).
thirsty :- thirstiness(N) & N >= 10.

strength_kg(10).
carrying(Resource, 0).
atCapacity :- carrying(_, Kg) & strength_kg(S) & S <= Kg.

//forgerNeeds(Resource)

//resource(Resource, Location)
//storage(Location)