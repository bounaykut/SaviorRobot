package localization;

import app.Cell;
import app.Main;
import lejos.robotics.subsumption.Behavior;

public class Localize implements Behavior {

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return !Main.action2;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		
		Cell cell = null;
		
		while(cell != null) {
			cell = Main.locate();
			if(cell != null) {
				if(cell.getUp() != null) {
					Main.moveToCell(cell.getUp());
				}
				else if(cell.getLeft() != null) {
					Main.moveToCell(cell.getLeft());
				}
				else if(cell.getBottom() != null) {
					Main.moveToCell(cell.getBottom());
				}
				else if(cell.getRight() != null) {
					Main.moveToCell(cell.getRight());
				}
				
				//eger yeni distancelar yeni sectigimiz cellinkine esit cikarsa kendimizi locate etmis oluruz
				cell = Main.locate();
			}
		
		}
		
		//we are located now
		
		Main.action2 = true;
		Main.weapon = true;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
