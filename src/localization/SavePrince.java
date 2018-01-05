package localization;
import app.Cell;
import app.Main;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class SavePrince implements Behavior {

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Main.prince;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
	
		//move to cell in which there is prince
		Cell princeCell = null;
		for(Cell c: Main.cells) {
			if(c.getColor() == Main.Color.RED) {
				princeCell = c;
				break;
			}
		}
		Main.moveShortest(princeCell);
		
		
		Sound.beep();
		Sound.beep();
		Sound.beep();
		
		Main.localizationArbitrator.stop();
		
		Main.prince = false;
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
