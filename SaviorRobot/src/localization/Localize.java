package localization;

import java.util.ArrayList;

import app.Cell;
import app.Main;
import lejos.hardware.lcd.GraphicsLCD;
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
		
		ArrayList<Cell> candidateList = null;
		boolean flag = false;
		while(!flag) {
			candidateList = Main.locate();
			if(candidateList != null) {
				for(int i=0;i<candidateList.size();i++) {
					
					Main.x = candidateList.get(i).getX();
					Main.y = candidateList.get(i).getY();
					Main.degree = Main.degreeList.get(i);
					
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
		
		//*****************************debug first part***********************\\
		Main.graphicsLCD.drawString(Main.x +"x "+ Main.y +"y "+Main.degree +"degree ", Main.graphicsLCD.getWidth()/2, 0, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
		Main.localizationArbitrator.stop();
		//*****************************debug first part***********************\\
		//we are located now
		
		Main.action2 = true;
		Main.weapon = true;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

}
