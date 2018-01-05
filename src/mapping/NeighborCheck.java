package mapping;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import app.Cell;
import app.Main;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class NeighborCheck implements Behavior {

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return Main.action;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
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
		}
		
		currentCell.setCount(currentCell.getCount() + 1);
		
		
		if(currentCell.getColor() == Main.Color.BLACK && !Main.isSafe) {
			Main.isBlack = true;
			Main.blackCellCount++;
		}
		else {
			Main.isBlack = false;
			
			
			if(Main.degree == 0) {
				if(Main.getDistance() > 0.33) {
					
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
				}
				Main.sensorMotor.rotate(-90);
					
					
				if(Main.getDistance() > 0.33) {
					
				
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
					
				}
				Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
						
					
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
						
					}
					Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
						
					
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
						//ultrasonic sensoru 90 derece daha dondur basladigi yere
						
					}
					Main.sensorMotor.rotate(270);
					
				
				
						
				
			}
			else if(Main.degree == 90) {
				
				if(Main.getDistance() > 0.33) {
					
				
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
					
				}
				Main.sensorMotor.rotate(-90);
					
					
					
					if(Main.getDistance() > 0.33) {
						
					
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
						
					}
					Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
						
					
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
						//ultrasonic sensoru 90 derece daha dondur basladigi yere
						
					}
					Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
					
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
						
					}
					Main.sensorMotor.rotate(270);
					
					
				
				
				
			}
			else if(Main.degree == 180) {
				
			
				if(Main.getDistance() > 0.33) {
					
				
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
					
				}	
				Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
					
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
						//ultrasonic sensoru 90 derece daha dondur basladigi yere
						
					}
					Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
						
					
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
						
					}
					Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
						
					
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
						
					}
					Main.sensorMotor.rotate(270);
					
					
					
				
				
				
			}
			else if(Main.degree == 270) {
				
				if(Main.getDistance() > 0.33) {
					
				
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
					//ultrasonic sensoru 90 derece daha dondur basladigi yere
					
				}
				Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
						
					
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
						
					}
					Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
						
					

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
						
					}
					Main.sensorMotor.rotate(-90);
					
					
					if(Main.getDistance() > 0.33) {
						
				
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
						
					}
					Main.sensorMotor.rotate(270);
					
				
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
		
	    
	
		OutputStream outputStream = Main.client.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		
		
		
		BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(dataOutputStream));
		
			
		
		bf.write(currentCell.getX()+"");
		bf.newLine();
		bf.write(currentCell.getY()+"");
		bf.newLine();
		
		if(currentCell.getDistance()[0] <= .33) {
			bf.write(1+"");
		}else {
			bf.write(0+"");
		}	
		bf.newLine();
		if(currentCell.getDistance()[1] <= .33) {
			bf.write(1+"");
		}else {
			bf.write(0+"");
		}
		bf.newLine();
		if(currentCell.getDistance()[2] <= .33) {
			bf.write(1+"");
		}else {
			bf.write(0+"");
		}
		bf.newLine();
		if(currentCell.getDistance()[3] <= .33) {
			bf.write(1+"");
		}else {
			bf.write(0+"");
		}
		bf.newLine();
		bf.flush();
			
		
			if(Main.mapDone) {
				Main.robot.stop();
				try {
					dataOutputStream.close();
					Main.socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Sound.beep();
				Sound.beep();
				Sound.beep();
			}
		
		
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		/*if(Main.mapDone) {
			Main.robot.stop();
			Sound.beep();
			Sound.beep();
			Sound.beep();
		}*/
	
		
		Main.action = false;
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
	

}
