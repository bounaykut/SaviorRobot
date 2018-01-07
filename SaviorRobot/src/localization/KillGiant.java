package localization;
import app.Cell;
import app.Main;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class KillGiant implements Behavior {

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Main.giant;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		
		//locate the giant: first go to any black cell 
		Cell blackCell = null;
		for(Cell c: Main.cells) {
			if(c.getColor() == Main.Color.BLACK) {
				blackCell = c;
				break;
			}
		}
		Main.moveShortest(blackCell);
		
		//then map previously undiscovered grids
		Main.isSafe = true;
		Main.action = true;
		//NeighborCheck nCheck = new NeighborCheck();
		//MoveTo moveTo = new MoveTo();

		//Behavior[] behaviorList = {moveTo,nCheck};
		//Arbitrator  = new Arbitrator(behaviorList);
		Main.mappingArbitrator.go();
		
		
		//move to cell in which there is giant
		Cell giantCell = null;
		for(Cell c: Main.cells) {
			if(c.getColor() == Main.Color.RED) {
				giantCell = c;
				break;
			}
		}
		Main.moveShortest(giantCell);
		
		Sound.beepSequenceUp();
		
		
		Main.giant = false;
		Main.prince = true;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
