package artefacts;

public class CarrierArtefact extends Artefact {

	public String caveGoingTo;
	public CarrierArtefact(String agentName, String caveGoingTo) {
		super(agentName);
		this.caveGoingTo = caveGoingTo;
	}

}
