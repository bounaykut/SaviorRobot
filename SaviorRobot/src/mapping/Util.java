package mapping;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.omg.CORBA.portable.OutputStream;

import app.Cell;
import app.Main;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;

public class Util {

	public static void neighborCheck() {
		
		Cell currentCell = Main.getCell(Main.x, Main.y);
		if(currentCell == null) {
			currentCell = new Cell();
			currentCell.setX(Main.x);
			currentCell.setY(Main.y);


			Main.cells.add(currentCell);
		}

		if(currentCell.getCount() == 0) {
			currentCell.setDone(true);
			currentCell.setColor(Main.getCellColor()); 
			
			if(currentCell.getColor() == Main.Color.RED) {
				Main.armMotor.rotate(-60);
				Sound.beepSequenceUp();
			}
		}
		
		
		if(currentCell.getColor() == Main.Color.GREEN) {
			
			Sound.beep();
			Sound.beep();
			Sound.beep();
			
			return;
		}
	
		

		currentCell.setCount(currentCell.getCount() + 1);


		if(currentCell.getColor() == Main.Color.BLACK && !Main.isSafe) {
			Main.isBlack = true;
			Main.blackCellCount++;
		}
		else {
			Main.isBlack = false;


			if(Main.degree == 0) {
				if(Main.getDistance() > 0.27) {
					
					Cell upCell = Main.getCell(Main.x,Main.y+1);
					if(upCell == null) {
						upCell = new Cell();
						upCell.setX(Main.x);
						upCell.setY(Main.y+1);	
						currentCell.setUp(upCell);
						upCell.setBottom(currentCell);
						currentCell.getDistance()[0] = Main.getDistance(); 
						Main.cells.add(upCell);
					}
				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {


					Cell leftCell = Main.getCell(Main.x-1, Main.y);
					if(leftCell == null) {
						leftCell = new Cell();
						leftCell.setX(Main.x-1);
						leftCell.setY(Main.y);	
						currentCell.setLeft(leftCell);
						leftCell.setRight(currentCell); 
						currentCell.getDistance()[1] = Main.getDistance(); 
						Main.cells.add(leftCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {


					Cell bottomCell = Main.getCell(Main.x, Main.y-1);
					if(bottomCell == null) {
						bottomCell = new Cell();
						bottomCell.setX(Main.x);
						bottomCell.setY(Main.y-1);
						currentCell.setBottom(bottomCell);
						bottomCell.setUp(currentCell); 
						currentCell.getDistance()[2] = Main.getDistance(); 
						Main.cells.add(bottomCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {


					Cell rightCell = Main.getCell(Main.x+1, Main.y);
					if(rightCell == null) {
						rightCell = new Cell();
						rightCell.setX(Main.x+1);
						rightCell.setY(Main.y);
						currentCell.setRight(rightCell);
						rightCell.setLeft(currentCell); 
						currentCell.getDistance()[3] = Main.getDistance(); 
						Main.cells.add(rightCell);
					}
					//ultrasonic sensoru 80 derece daha dondur basladigi yere

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(-270);





			}
			else if(Main.degree == 80) {

				if(Main.getDistance() > 0.27) {


					Cell leftCell = Main.getCell(Main.x-1, Main.y);
					if(leftCell == null) {
						leftCell = new Cell();
						leftCell.setX(Main.x-1);
						leftCell.setY(Main.y);
						currentCell.setLeft(leftCell);
						leftCell.setRight(currentCell); 
						currentCell.getDistance()[1] = Main.getDistance(); 
						Main.cells.add(leftCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);



				if(Main.getDistance() > 0.27) {


					Cell bottomCell = Main.getCell(Main.x, Main.y-1);
					if(bottomCell == null) {
						bottomCell = new Cell();
						bottomCell.setX(Main.x);
						bottomCell.setY(Main.y-1);
						currentCell.setBottom(bottomCell);
						bottomCell.setUp(currentCell); 
						currentCell.getDistance()[2] = Main.getDistance(); 
						Main.cells.add(bottomCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {


					Cell rightCell = Main.getCell(Main.x+1, Main.y);
					if(rightCell == null) {
						rightCell = new Cell();
						rightCell.setX(Main.x+1);
						rightCell.setY(Main.y);
						currentCell.setRight(rightCell);
						rightCell.setLeft(currentCell); 
						currentCell.getDistance()[3] = Main.getDistance(); 
						Main.cells.add(rightCell);
					}
					//ultrasonic sensoru 80 derece daha dondur basladigi yere

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {

					Cell upCell = Main.getCell(Main.x,Main.y+1);
					if(upCell == null) {
						upCell = new Cell();
						upCell.setX(Main.x);
						upCell.setY(Main.y+1);
						currentCell.setUp(upCell);
						upCell.setBottom(currentCell);
						currentCell.getDistance()[0] = Main.getDistance(); 
						Main.cells.add(upCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(-270);





			}
			else if(Main.degree == 160) {


				if(Main.getDistance() > 0.27) {


					Cell bottomCell = Main.getCell(Main.x, Main.y-1);
					if(bottomCell == null) {
						bottomCell = new Cell();
						bottomCell.setX(Main.x);
						bottomCell.setY(Main.y-1);
						currentCell.setBottom(bottomCell);
						bottomCell.setUp(currentCell); 
						currentCell.getDistance()[2] = Main.getDistance(); 
						Main.cells.add(bottomCell);
					}

				}	else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {

					Cell rightCell = Main.getCell(Main.x+1, Main.y);
					if(rightCell == null) {
						rightCell = new Cell();
						rightCell.setX(Main.x+1);
						rightCell.setY(Main.y);
						currentCell.setRight(rightCell);
						rightCell.setLeft(currentCell); 
						currentCell.getDistance()[3] = Main.getDistance(); 
						Main.cells.add(rightCell);
					}
					//ultrasonic sensoru 80 derece daha dondur basladigi yere

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {


					Cell upCell = Main.getCell(Main.x,Main.y+1);
					if(upCell == null) {
						upCell = new Cell();
						upCell.setX(Main.x);
						upCell.setY(Main.y+1);
						currentCell.setUp(upCell);
						upCell.setBottom(currentCell);
						currentCell.getDistance()[0] = Main.getDistance(); 
						Main.cells.add(upCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {


					Cell leftCell = Main.getCell(Main.x-1, Main.y);
					if(leftCell == null) {
						leftCell = new Cell();
						leftCell.setX(Main.x-1);
						leftCell.setY(Main.y);
						currentCell.setLeft(leftCell);
						leftCell.setRight(currentCell); 
						currentCell.getDistance()[1] = Main.getDistance(); 
						Main.cells.add(leftCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(-270);






			}
			else if(Main.degree == 240) {

				if(Main.getDistance() > 0.27) {


					Cell rightCell = Main.getCell(Main.x+1, Main.y);
					if(rightCell == null) {
						rightCell = new Cell();
						rightCell.setX(Main.x+1);
						rightCell.setY(Main.y);
						currentCell.setRight(rightCell);
						rightCell.setLeft(currentCell); 
						currentCell.getDistance()[3] = Main.getDistance(); 
						Main.cells.add(rightCell);
					}
					//ultrasonic sensoru 80 derece daha dondur basladigi yere

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {


					Cell upCell = Main.getCell(Main.x,Main.y+1);
					if(upCell == null) {
						upCell = new Cell();
						upCell.setX(Main.x);
						upCell.setY(Main.y+1);
						currentCell.setUp(upCell);
						upCell.setBottom(currentCell);
						currentCell.getDistance()[0] = Main.getDistance(); 
						Main.cells.add(upCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {



					Cell leftCell = Main.getCell(Main.x-1, Main.y);
					if(leftCell == null) {
						leftCell = new Cell();
						leftCell.setX(Main.x-1);
						leftCell.setY(Main.y);
						currentCell.setLeft(leftCell);
						leftCell.setRight(currentCell); 
						currentCell.getDistance()[1] = Main.getDistance(); 
						Main.cells.add(leftCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(90);


				if(Main.getDistance() > 0.27) {


					Cell bottomCell = Main.getCell(Main.x, Main.y-1);
					if(bottomCell == null) {
						bottomCell = new Cell();
						bottomCell.setX(Main.x);
						bottomCell.setY(Main.y-1);
						currentCell.setBottom(bottomCell);
						bottomCell.setUp(currentCell); 
						currentCell.getDistance()[2] = Main.getDistance(); 
						Main.cells.add(bottomCell);
					}

				}else {
					if(currentCell.getCount() == 1) {
						currentCell.setWallCount(currentCell.getWallCount()+1);
					}
				}
				Main.sensorMotor.rotate(-270);


			}


		}





		Main.mapDone = true;
		for(Cell cell:Main.cells) {
			if(!cell.isDone()) {
				Main.mapDone = false;
				break;
			}
		}

		//Main.graphicsLCD.clear();
		//Main.graphicsLCD.drawString(""+Main.cells.size(), Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);


		try {



		java.io.OutputStream outputStream = Main.client.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(dataOutputStream));



		bf.write(currentCell.getX()+"");
		bf.newLine();
		bf.write(currentCell.getY()+"");
		bf.newLine();

		if(currentCell.getUp() == null) {
			bf.write(1+"");
		}else {
			bf.write(0+"");
		}	
		bf.newLine();
		if(currentCell.getLeft() == null) {
			bf.write(1+"");
		}else {
			bf.write(0+"");
		}
		bf.newLine();
		if(currentCell.getBottom() == null) {
			bf.write(1+"");
		}else {
			bf.write(0+"");
		}
		bf.newLine();
		if(currentCell.getRight() == null) {
			bf.write(1+"");
		}else {
			bf.write(0+"");
		}
		bf.newLine();
		bf.flush();


			if(Main.mapDone) {
				Main.robot.stop();
				//Main.graphicsLCD.drawString("map bitti"+Main.cells.size(), Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
				//Delay.msDelay(3000);
				if(Main.firstPart) {
					Main.serializeMap();
					Main.firstPart = false;
				}
				/*try {
				dataOutputStream.close();
				Main.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
				Sound.beep();
				Sound.beep();
				Sound.beep();
			}else {
				Util.moveTo();
			}



		}catch (Exception e) {
			e.printStackTrace();
		}


	}

	public static void moveTo() {

		Cell currentCell = Main.getCell(Main.x,Main.y); 

		if(Main.isBlack && !Main.isSafe) {

			//turn by 160 degrees and move 33 cm
			Main.robot.rotate(160);
			Main.robot.travel(33);
			//and change main degree to (main.degree+160)%320
			Main.degree = (Main.degree + 160) % 320;

			Main.x = Main.previousCell.getX();
			Main.y = Main.previousCell.getY();

		}
		else {


			ArrayList<Cell> nList = new ArrayList<>();

			if(currentCell.getUp() != null) {
				nList.add(currentCell.getUp());
				Main.graphicsLCD.clear();
				Main.graphicsLCD.drawString("up " + currentCell.getUp().getX() +  " and " + currentCell.getUp().getY(), Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
				//Delay.msDelay(2000);
			}
			if(currentCell.getLeft() != null) {
				nList.add(currentCell.getLeft());
				Main.graphicsLCD.clear();
				Main.graphicsLCD.drawString("left " + currentCell.getLeft().getX() +  " and " + currentCell.getLeft().getY(), Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
				//Delay.msDelay(2000);
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
				//Delay.msDelay(2000);
			}


			ArrayList<Cell> choices = new ArrayList<>();
			int minCount = 1000000;
			int coordinate[] = {0,0};
			for(Cell cell:nList) {
				if(cell.getCount() < minCount) {
					minCount = cell.getCount();
					coordinate[0] = cell.getX();
					coordinate[1] = cell.getY();
					//choices.add(cell);

					Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString("mins " + coordinate[0] +  " and " + coordinate[1], Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
					//Delay.msDelay(2000);
				}
			}

			/*//to prevent go back previous position unnecessarily
			if(choices.size() > 1) {
				for(Cell cell:choices) {
					if(cell != Main.previousCell) {
						coordinate[0] = cell.getX();
						coordinate[1] = cell.getY();
					}
				}
			}*/

			if(Main.x != coordinate[0]) {

				if(Main.x > coordinate[0]) {
					// 80 - Main.degree kadar don
					if(80-Main.degree == -240) {
						Main.robot.rotate(80);
					}
					else{
						Main.robot.rotate(80-Main.degree);
					}
					/*Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString("1 "+Main.degree, Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
					//33 cm duz git*/
					Main.robot.travel(33);

					Main.degree = 80;
					Main.x--;
				}else {
					//240 - Main.Degree kadar don
					if(240-Main.degree == 240) {
						Main.robot.rotate(-80);
					}
					else {
						Main.robot.rotate(240-Main.degree);
					}
					/*Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString("2 "+Main.degree, Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
					//33 cm duz git*/
					Main.robot.travel(33);

					Main.degree = 240;
					Main.x++;
				}

			}else {

				if(Main.y > coordinate[1]) {
					// 160 - Main.degree kadar don
					Main.robot.rotate(160-Main.degree);
					/*Main.graphicsLCD.clear();
					Main.graphicsLCD.drawString("3 "+Main.degree, Main.graphicsLCD.getWidth()/2, Main.graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
					//33 cm duz git*/
					Main.robot.travel(33);

					Main.degree = 160;
					Main.y--;
				}else {
					//320 - Main.Degree kadar don
					if(320-Main.degree == 240) {
						Main.robot.rotate(-80);
					}
					else if(320-Main.degree != 320) {
						Main.robot.rotate(320-Main.degree);
					}
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
		
		Util.neighborCheck();



	}
}
