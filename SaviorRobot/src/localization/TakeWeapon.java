package localization;
import app.Cell;
import app.Main;
import lejos.robotics.subsumption.Behavior;

public class TakeWeapon implements Behavior {

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Main.weapon;
	}

	@Override
	public void action() {
				
		//move to cell in which there is weapon
		Cell weaponCell = null;
		for(Cell c: Main.cells) {
			if(c.getColor() == Main.Color.BLUE) {
				weaponCell = c;
				break;
			}
		}
		Main.moveShortest(weaponCell);
		
		
		
		//*****************TODO***************\\ take the weapon with grabbing arms
		
	

		Main.weapon = false;
		Main.giant = true;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
