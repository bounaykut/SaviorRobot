package mapping;
import java.util.ArrayList;

import app.Cell;
import app.Main;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class MoveTo implements Behavior {

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return !Main.action && !Main.mapDone;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		Cell currentCell = Main.getCell(Main.x,Main.y); 
		
		if(Main.isBlack && !Main.isSafe) {
				
			//turn by 180 degrees and move 33 cm
			Main.robot.rotate(180);
			Main.robot.travel(33);
			//and change main degree to (main.degree+180)%360
			Main.degree = (Main.degree + 180) % 360;
			
			Main.x = Main.previousCell.getX();
			Main.y = Main.previousCell.getY();
			
		}
		else {
			
			
			ArrayList<Cell> nList = new ArrayList<>();
			
			if(currentCell.getUp() != null) {
				nList.add(currentCell.getUp());
				Main.graphicsLCD.clear();
				Main.graphicsLCD.drawString("up " + currentCell.getUp().getX() +  " and " + currentCell.getUp().getY(), Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
				Delay.msDelay(2000);
			}
			if(currentCell.getLeft() != null) {
				nList.add(currentCell.getLeft());
				Main.graphicsLCD.clear();
				Main.graphicsLCD.drawString("left " + currentCell.getLeft().getX() +  " and " + currentCell.getLeft().getY(), Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
				Delay.msDelay(2000);
			}
			if(currentCell.getBottom()  != null) {
				nList.add(currentCell.getBottom());
				Main.graphicsLCD.clear();
				Main.graphicsLCD.drawString("bottom " + currentCell.getBottom().getX() +  " and " + currentCell.getBottom().getY(), Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
			}
			if(currentCell.getRight()  != null) {
				nList.add(currentCell.getRight());
				Main.graphicsLCD.clear();
				Main.graphicsLCD.drawString("right " + currentCell.getRight().getX() +  " and " + currentCell.getRight().getY(), Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
				Delay.msDelay(2000);
			}
			
			int minCount = 1000000;
			int coordinate[] = {0,0};
			for(Cell cell:nList) {
				if(cell.getCount() < minCount) {
					minCount = cell.getCount();
					coordinate[0] = cell.getX();
					coordinate[1] = cell.getY();
					Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString("mins " + coordinate[0] +  " and " + coordinate[1], Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
					Delay.msDelay(2000);
				}
			}
			
			if(Main.x != coordinate[0]) {
				
				if(Main.x > coordinate[0]) {
					// 90 - Main.degree kadar don
					Main.robot.rotate(90-Main.degree);
					/*Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString("1 "+Main.degree, Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
					//33 cm duz git*/
					Main.robot.travel(33);
					
					Main.degree = 90;
					Main.x--;
				}else {
					//270 - Main.Degree kadar don
					Main.robot.rotate(270-Main.degree);
					/*Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString("2 "+Main.degree, Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
					//33 cm duz git*/
					Main.robot.travel(33);
					
					Main.degree = 270;
					Main.x++;
				}
				
			}else {
				
				if(Main.y > coordinate[1]) {
					// 180 - Main.degree kadar don
					Main.robot.rotate(180-Main.degree);
					/*Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString("3 "+Main.degree, Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
					//33 cm duz git*/
					Main.robot.travel(33);
					
					Main.degree = 180;
					Main.y--;
				}else {
					//360 - Main.Degree kadar don
					Main.robot.rotate(360-Main.degree);
					/*Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString("4 "+Main.degree, Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
					//33 cm duz git*/
					Main.robot.travel(33);
					
					Main.degree = 0;
					Main.y++;
				}
				
			}
			
		}
		
		
		Main.previousCell = currentCell;
		
		
		Main.action = true;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
	

}
