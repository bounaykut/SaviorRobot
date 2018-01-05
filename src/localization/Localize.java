package localization;

import java.util.ArrayList;

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
		ArrayList<Cell> candidateList = null;
		boolean flag = false;
		while(!flag) {
			candidateList = Main.locate();
			if(candidateList != null) {
				for(int i=0;i<candidateList.size();i++) {
					if(candidateList.get(i).getUp() != null) {
						Main.moveToCell(candidateList.get(i).getUp());
						
						ArrayList<Cell> neighborList = Main.locate();
						for(Cell c: neighborList) {
							if(c.getBottom() == candidateList.get(i)) {
								flag = true;
							}
						}
					}
					else if(candidateList.get(i).getLeft() != null) {
						Main.moveToCell(candidateList.get(i).getLeft());
						

						ArrayList<Cell> neighborList = Main.locate();
						for(Cell c: neighborList) {
							if(c.getRight() == candidateList.get(i)) {
								flag = true;
							}
						}
					}
					else if(candidateList.get(i).getBottom() != null) {
						Main.moveToCell(candidateList.get(i).getBottom());
						

						ArrayList<Cell> neighborList = Main.locate();
						for(Cell c: neighborList) {
							if(c.getUp() == candidateList.get(i)) {
								flag = true;
							}
						}
					}
					else if(candidateList.get(i).getRight() != null) {
						Main.moveToCell(candidateList.get(i).getRight());
						

						ArrayList<Cell> neighborList = Main.locate();
						for(Cell c: neighborList) {
							if(c.getLeft() == candidateList.get(i)) {
								flag = true;
							}
						}
					}
					
				
				}
				
		
				
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
