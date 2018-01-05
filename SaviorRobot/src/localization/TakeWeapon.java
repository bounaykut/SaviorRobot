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
		
		
		
		//locate the giant: first go to any black cell, then search 4 neighbors of black cell 
		Cell blackCell = null;
		for(Cell c: Main.cells) {
			if(c.getColor() == Main.Color.BLACK) {
				blackCell = c;
				break;
			}
		}
		Main.moveShortest(blackCell);
				
		boolean found = false;
		if(blackCell.getUp() != null) {
			//go to upper cell of black cell
			Main.moveToCell(blackCell.getUp());
			if(Main.getCellColor() == Main.Color.RED) {
				found = true;
			}
		}
		if(blackCell.getUp() != null && !found) {
			//get back to black cell
			Main.moveToCell(blackCell);
			//go to left cell of black cell
			Main.moveToCell(blackCell.getLeft());
			if(Main.getCellColor() == Main.Color.RED) {
				found = true;
			}
		}
		if(blackCell.getUp() != null && !found) {
			//get back to black cell
			Main.moveToCell(blackCell);
			//go to bottom cell of black cell
			Main.moveToCell(blackCell.getBottom());
			if(Main.getCellColor() == Main.Color.RED) {
				found = true;
			}
		}
		if(blackCell.getUp() != null && !found) {
			//get back to black cell
			Main.moveToCell(blackCell);
			//go to right cell of black cell
			Main.moveToCell(blackCell.getRight());
			if(Main.getCellColor() == Main.Color.RED) {
				found = true;
			}
		}

		Main.weapon = false;
		Main.giant = true;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
