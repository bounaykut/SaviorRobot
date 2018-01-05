package localization;
import app.Cell;

import app.Main;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import mapping.MoveTo;
import mapping.NeighborCheck;

public class KillGiant implements Behavior {

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Main.giant;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
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
		
		//if prince is unlocated before, then map previously undiscovered grids
		if(!Main.isPrinceLocated()) {
			Main.isSafe = true;
			Main.action = true;
			NeighborCheck nCheck = new NeighborCheck();
			MoveTo moveTo = new MoveTo();
			
			Behavior[] behaviorList = {moveTo,nCheck};
			Arbitrator arbitrator = new Arbitrator(behaviorList);
			arbitrator.go();
		}
		
		
		Main.giant = false;
		Main.prince = true;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
