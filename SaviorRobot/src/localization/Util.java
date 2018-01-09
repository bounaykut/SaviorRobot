package localization;

import java.util.ArrayList;

import app.Cell;
import app.Main;
import lejos.hardware.Sound;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Util {


	
	public static void localize() {
		
		ArrayList<Cell> candidateList = null;
		boolean flag = false;
		while(!flag) {
			candidateList = Main.locate();
			
		
			if(candidateList != null) {
				for(int i=0;i<candidateList.size();i++) {
					
					Main.x = candidateList.get(i).getX();
					Main.y = candidateList.get(i).getY();
					Main.degree = Main.degreeList.get(i);
					
					Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString(Main.x + " and "+ Main.y, Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
					Main.graphicsLCD.refresh();
					
					if(candidateList.size() != 1) {
						if(candidateList.get(i).getUp() != null) {
							Main.moveToCell(candidateList.get(i).getUp());
							
							
							ArrayList<Cell> neighborList = Main.locate();
							for(Cell c: neighborList) {
								if(c.getBottom() == candidateList.get(i)) {
									flag = true;
									break;
								}
							}
							
						}
						else if(candidateList.get(i).getLeft() != null) {
							Main.moveToCell(candidateList.get(i).getLeft());
							

							ArrayList<Cell> neighborList = Main.locate();
							for(Cell c: neighborList) {
								if(c.getRight() == candidateList.get(i)) {
									flag = true;
									break;
								}
							}
							
						}
						else if(candidateList.get(i).getBottom() != null) {
							Main.moveToCell(candidateList.get(i).getBottom());
							

							ArrayList<Cell> neighborList = Main.locate();
							for(Cell c: neighborList) {
								if(c.getUp() == candidateList.get(i)) {
									flag = true;
									break;
								}
							}
							
						}
						else if(candidateList.get(i).getRight() != null) {
							Main.moveToCell(candidateList.get(i).getRight());

							ArrayList<Cell> neighborList = Main.locate();
							for(Cell c: neighborList) {
								if(c.getLeft() == candidateList.get(i)) {
									flag = true;
									break;
								}
							}
							
							
						}
						
						
						if(!flag) {
							Main.moveToCell(candidateList.get(i));
						}else {
							break;
						}
					}else {
						flag=true;
						break;
					}
				
				}
				
				
			}
		
		}
		
		//*****************************debug first part***********************\\
		Main.graphicsLCD.clear();
		//Main.graphicsLCD.drawString(Main.x +"x "+ Main.y +"y "+Main.degree +"degree ", Main.graphicsLCD.getWidth()/2, 0, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
		Main.graphicsLCD.refresh();
		Delay.msDelay(4000);
		//*****************************debug first part***********************\\
		//we are located now
		
		Util.takeWeapon();
		
	}

	public static void takeWeapon() {

		//move to cell in which there is weapon
		Cell weaponCell = null;
		for(Cell c: Main.cells) {
			if(c.getColor() == Main.Color.BLUE) {
				weaponCell = c;
				break;
			}
		}
		Main.graphicsLCD.clear();
		Main.graphicsLCD.refresh();
		//Main.graphicsLCD.drawString(weaponCell.getX() + " and "+ weaponCell.getY(), Main.graphicsLCD.getWidth()/2, 0, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
		Main.moveShortest(weaponCell);
		System.out.println("bitti");


		//*****************TODO***************\\ take the weapon with grabbing arms
		Main.armMotor.rotate(60);


		Util.killGiant();
	}

	public static void killGiant() {
		//locate the giant: first go to any black cell 
		Cell blackCell = null;
		for(Cell c: Main.cells) {
			if(c.getColor() == Main.Color.BLACK) {
				blackCell = c;
				break;
			}
		}
		Main.moveShortest(blackCell);

		//then map previously undiscovered grids(kill giant and save prince)
		Main.isSafe = true;
		mapping.Util.neighborCheck();
		
		
		//Util.savePrince();


	}
	
	/*public static void savePrince() {
		
		
		//go save prince 
		Cell princeCell = null;
		for(Cell c: Main.cells) {
			if(c.getColor() == Main.Color.GREEN) {
				princeCell = c;
				break;
			}
		}
		Main.moveShortest(princeCell);
		
		
		Sound.beep();
		Sound.beep();
		Sound.beep();
			
	}*/

}
