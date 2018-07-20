package artefacts;

public class CarrierArtefact extends Artefact {

	public int caveGoingTo;
	public CarrierArtefact(String agentName, int caveGoingTo) {
		super(agentName);
		this.caveGoingTo = caveGoingTo;
	}

}
