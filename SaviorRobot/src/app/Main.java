package app;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;


public class Main {

	//color enumeration for grid color 
	public enum Color{
		GREEN,BLUE,BLACK,WHITE, RED	
	}
	
	//passive socket 
	public static ServerSocket socket;
	//activated socket to draw map at map sketcher
	public static Socket client;
	
	//list representing map grid topology
	public static ArrayList<Cell> cells = new ArrayList<>();
	
	//to remember and move back to the previous cell if current cell is black
	public static Cell previousCell = null;
	
	//mapping action controls
	public static boolean mapDone = false;
	public static boolean isSafe = false;
	
	//if current cell is a black cell or not
	public static boolean isBlack = false;
	
	//first mapping on execution
	public static boolean firstPart = true;
	
	//total black cell that is discovered
	public static int blackCellCount = 0;
	//x and y coordinates which is current position of the robot in the map
	public static int x,y = 0;
	//robot's current heading
	public static int degree = 0;
	
	//differential drive based robot
	public static DifferentialPilot robot;
	//color sensor
	public static EV3ColorSensor colorSensor;
	//ultrasonic sensor
	public static EV3UltrasonicSensor ultrasonicSensor;
	//sample providers(sensor modes) for sensors
	public static SampleProvider sp1,sp2;
	//sample values taken by sensors' sample provider
	public static float[] sample1,sample2;
	
	
	//ultrasonic sensor motor
	public static RegulatedMotor sensorMotor, armMotor;
	
	//robot body(brick)
	public static EV3 ev3 = (EV3)BrickFinder.getDefault();
	
	//robot screen
	public static GraphicsLCD graphicsLCD = ev3.getGraphicsLCD();
	
	
	public static void main(String[] args) throws Exception {
		
		//differential robot initiation
		RegulatedMotor leftMotor;
		RegulatedMotor rightMotor;
		sensorMotor = new EV3MediumRegulatedMotor(MotorPort.A);
		
		armMotor = new EV3MediumRegulatedMotor(MotorPort.D);
		
		
    	float leftDiam = 5.55f;
    	float rightDiam = 5.55f;
    	float trackWidth = 12.5f;
    	
    	
    	leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
    	rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
    	boolean reverse = false;
    	
    	
    	robot = new DifferentialPilot(leftDiam,rightDiam,trackWidth,leftMotor,rightMotor,reverse);
		
    	
    	robot.setLinearSpeed(5);
    	robot.setAngularSpeed(40);
    	//82
    	//robot.travel(33);
    	//armMotor.rotate(60);
    	//sensorMotor.rotate(-90);
    	
    	
    	
    	//sensors' initiation
        colorSensor = new EV3ColorSensor(SensorPort.S4);
		sp1 = colorSensor.getColorIDMode();
		sample1 = new float[sp1.sampleSize()];
		
		ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S1);
	    sp2 = ultrasonicSensor.getDistanceMode();
	    sample2 = new float[sp2.sampleSize()];
	    
	    
	    
		
	    //server socket initiation
	    socket = new ServerSocket(1234);
	    Main.graphicsLCD.clear();
	    Main.graphicsLCD.drawString("WhereAMI", Main.graphicsLCD.getWidth()/2, 0, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
	    Main.graphicsLCD.drawString("Waiting", Main.graphicsLCD.getWidth()/2, 20, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
	    Main.graphicsLCD.refresh();
		
		client = socket.accept();
		
		Main.graphicsLCD.clear();
		Main.graphicsLCD.drawString("WhereAMI", Main.graphicsLCD.getWidth()/2, 0, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
		Main.graphicsLCD.drawString("Connected", Main.graphicsLCD.getWidth()/2, 20, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
		Main.graphicsLCD.refresh();
		
		
		//button interface to run different task with buttons
		if(Main.deserializeMap()) {
			Main.graphicsLCD.drawString("Successful deserialization", Main.graphicsLCD.getWidth()/2, 0, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
		}else {
			Main.graphicsLCD.drawString("Unsuccessful deserialization", Main.graphicsLCD.getWidth()/2, 0, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
		}
		while(true) {
			int button = 0;
			button = Button.waitForAnyPress();
			
			//mapping task
			if(button == Button.ID_UP) {
				mapping.Util.neighborCheck();
				
			}
			//localization task
			else if(button == Button.ID_DOWN) {
				localization.Util.localize();
				//localization.Util.takeWeapon();
			}
			else if(button == Button.ID_RIGHT) {
				//localization.Util.localize();
				localization.Util.takeWeapon();
			}
			//idle task
			else if(button == Button.ID_ENTER) {
				robot.stop();
			}
			//reset current task
			else if(button == Button.ID_ESCAPE) {
				if(mapDone) {
					//reset localization
					x = 0;
					y = 0;
					degree = 0;
					isBlack = false;
					previousCell = null;
					
					localization.Util.localize();
				}else {
					//reset mapping
					cells.clear();
					previousCell = null;
					mapDone = false;
					isBlack = false;
					blackCellCount = 0;
					x = 0;
					y = 0;
					degree = 0;
					isSafe = false;
					
					mapping.Util.neighborCheck();
				}
			}
		}
			
	}
	
	//get the current cell by reference
	public static Cell getCell(int x,int y) {
		
		for(Cell c: cells) {
			if(c.getX() == x && c.getY() == y) {
				return c;
			}
		}
		
		return null;
	}
	
	//get current cell color
	public static Main.Color getCellColor(){
		
		sp1.fetchSample(sample1, 0);
		
		if(sample1[0] == 0) {
			return Main.Color.RED;
		}else if(sample1[0] == 1) {
			return Main.Color.GREEN;
		}else if(sample1[0] == 2) {
			return Main.Color.BLUE;
		}else if(sample1[0] == 6) {
			return Main.Color.WHITE;
		}else if(sample1[0] == 7) {
			return Main.Color.BLACK;
		}
		
		return null;
	}
	
	//get distance to ultrasonic sensor
	public static float getDistance() {
		
		Delay.msDelay(1000);
		
		sp2.fetchSample(sample2, 0);
		//graphicsLCD.clear();
		//graphicsLCD.drawString(""+sample2[0], graphicsLCD.getWidth()/2, graphicsLCD.getHeight()/2, GraphicsLCD.HCENTER|GraphicsLCD.VCENTER);
		
		return sample2[0];
		
	}
	
	//shift float array to right by the number specified
	public static float[] shift(float[] array, int shifBy) {
		
		ArrayList<Float> start = new ArrayList<>();
		ArrayList<Float> end = new ArrayList<>();
		
		for(int i=0;i<array.length;i++) {
			if(i < array.length - shifBy) {
				start.add(array[i]);
			}else {
				end.add(array[i]);
			}
		}
		
		end.addAll(end.size(), start);
		
		float temp[] = new float[4];
		for(int i=0;i<array.length;i++) {
			temp[i] = end.get(i);
		}
		
		
		return temp;
	}
	
	//checks whether two float array are almost equal(every element in arrays differs at most by .13) or not
	public static boolean equals(float[] arr1, float[] arr2) {
		
		for(int i=0;i<4;i++) {
			if(Math.abs(arr1[i]-arr2[i]) >= 0.25) {
				return false;
			}
		}
		
		return true;
	}
	
	
	public static ArrayList<Integer> degreeList = new ArrayList<>();
	//try to locate current position of the robot by checking distance array and map cells' distance arrays
	public static ArrayList<Cell> locate() {
		
		degreeList.clear();
		
		ArrayList<Cell> list = new ArrayList<>();
		int currentWallCount = 0;
		float currentDist[] = new float[4];
		Delay.msDelay(1000);
		currentDist[0] = Main.getDistance();
		if(currentDist[0] < 0.27) currentWallCount++;
		for(int i=1;i<4;i++) {
			Main.sensorMotor.rotate(90);
			currentDist[i] = Main.getDistance();
			if(currentDist[i] < 0.27) currentWallCount++;
		}
		Main.sensorMotor.rotate(-270);
		
		//locate yourself(find a unique cell to locate your coordinate) 
		for(Cell cell:Main.cells) {
			int cellWallCount = cell.getWallCount();
			
			
			if(cellWallCount == currentWallCount ) {
				Main.graphicsLCD.clear();
				Main.graphicsLCD.drawString(currentWallCount+ " currentwall ", Main.graphicsLCD.getWidth()/2, 0, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
				Main.graphicsLCD.refresh();
				Delay.msDelay(2000);
				float dist0[] = cell.getDistance();
				float dist1[] = Main.shift(dist0, 1);
				float dist2[] = Main.shift(dist1, 1);
				float dist3[] = Main.shift(dist2, 1);

				if(Main.equals(currentDist, dist0)) {
					degreeList.add(0);
					list.add(cell);
					
					
				}
				else if(Main.equals(currentDist, dist1)) {
					degreeList.add(240);
					list.add(cell);
					
					
				}
				else if(Main.equals(currentDist, dist2)) {
					degreeList.add(160);
					list.add(cell);
					
					
				}
				else if(Main.equals(currentDist, dist3)) {
					degreeList.add(80);
					list.add(cell);
					
					
				}
				
			}
			
		}
		
		Main.graphicsLCD.clear();
		Main.graphicsLCD.drawString("candidate size "+list.size(), Main.graphicsLCD.getWidth()/2, 0, GraphicsLCD.VCENTER|GraphicsLCD.HCENTER);
		Main.graphicsLCD.refresh();
		Delay.msDelay(2000);

		return list;
	}
	
	//move by one cell
	public static void moveToCell(Cell cell) {

		
		if(Main.x != cell.getX()) {
			
			if(Main.x > cell.getX()) {
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
			
			if(Main.y > cell.getY()) {
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
	
	//move to specified cell through the shortest path
	public static void moveShortest(Cell goal) {
			
			Cell destination = null;
			
			ArrayList<Cell> openList = new ArrayList<>();
			ArrayList<Cell> closeList = new ArrayList<>();
			
			Cell currentCell = Main.getCell(Main.x, Main.y).clone();
			
			openList.add(currentCell);
			
			while(!openList.isEmpty()) {
				
				Cell q = null;
				
				//step a=> find the node with the least f on the open list, call it "q"
				int min = 100000;
				for(Cell c:openList) {
					if(c.g+c.h < min) {
						q = c;
						min = c.g+c.h;
						//System.out.println(q.x + " and "+q.y + " score:"+q.g+q.h);
					}
				}
				
				//step b=> pop q off the open list
				openList.remove(q);
				
				//step c=> generate q's 4(at most) successors and set their parents to q
				Cell s1 = q.getUp();
				if(s1 != null) {
					s1 = q.getUp().clone();
					s1.parent = q;
					//step d.i=>
					s1.g = q.g + 1;
					s1.h = Math.abs(q.getX() - goal.getX()) + Math.abs(q.getY() - goal.getY());
					if(s1.x == goal.x && s1.y == goal.y) {
						destination = s1;
						break;
					}
					//step d.ii=>
					boolean check = false;
					if(openList.contains(s1)) {
						for(Cell c:openList) {
							if(c.x == s1.x && c.y == s1.y) {
								if(c.g+c.h < s1.g+s1.h) {
									check = true;
									break;
								}
							};
						}
					}
					//step d.iii=>
					if(!check) {
						for(Cell c:closeList) {
							if(c.x == s1.x && c.y == s1.y) {
								if(c.g+c.h < s1.g+s1.h) {
									check = true;
									break;
								}
							};
						}
						if(!check) {
							openList.add(s1);
						}
					}
				}
				
				Cell s2 = q.getLeft();
				if(s2 != null) {
					s2 = q.getLeft().clone();
					s2.parent = q;
					//step d.i=>
					s2.g = q.g + 1;
					s2.h = Math.abs(q.getX() - goal.getX()) + Math.abs(q.getY() - goal.getY());
					if(s2.x == goal.x && s2.y == goal.y) {
						destination = s2;
						break;
					}
					//step d.ii=>
					boolean check = false;
					if(openList.contains(s2)) {
						for(Cell c:openList) {
							if(c.x == s2.x && c.y == s2.y) {
								if(c.g+c.h < s2.g+s2.h) {
									check = true;
									break;
								}
							};
						}
					}
					//step d.iii=>
					if(!check) {
						for(Cell c:closeList) {
							if(c.x == s2.x && c.y == s2.y) {
								if(c.g+c.h < s2.g+s2.h) {
									check = true;
									break;
								}
							};
						}
						if(!check) {
							openList.add(s2);
						}
					}
				}
				
				Cell s3 = q.getBottom();
				if(s3 != null) {
					s3 = q.getBottom().clone();
					s3.parent = q;
					//step d.i=>
					s3.g = q.g + 1;
					s3.h = Math.abs(q.getX() - goal.getX()) + Math.abs(q.getY() - goal.getY());
					if(s3.x == goal.x && s3.y == goal.y) {
						destination = s3;
						break;
					}
					//step d.ii=>
					boolean check = false;
					if(openList.contains(s3)) {
						for(Cell c:openList) {
							if(c.x == s3.x && c.y == s3.y) {
								if(c.g+c.h < s3.g+s3.h) {
									check = true;
									break;
								}
							};
						}
					}
					//step d.iii=>
					if(!check) {
						for(Cell c:closeList) {
							if(c.x == s3.x && c.y == s3.y) {
								if(c.g+c.h < s3.g+s3.h) {
									check = true;
									break;
								}
							};
						}
						if(!check) {
							openList.add(s3);
						}
					}
				}
				
				Cell s4 = q.getRight();
				if(s4 != null) {
					s4 = q.getRight().clone();
					s4.parent = q;
					//step d.i=>
					s4.g = q.g + 1;
					s4.h = Math.abs(q.getX() - goal.getX()) + Math.abs(q.getY() - goal.getY());
					if(s4.x == goal.x && s4.y == goal.y) {
						destination = s4;
						break;
					}
					//step d.ii=>
					boolean check = false;
					if(openList.contains(s4)) {
						for(Cell c:openList) {
							if(c.x == s4.x && c.y == s4.y) {
								if(c.g+c.h < s4.g+s4.h) {
									check = true;
									break;
								}
							};
						}
					}
					//step d.iii=>
					if(!check) {
						for(Cell c:closeList) {
							if(c.x == s4.x && c.y == s4.y) {
								if(c.g+c.h < s4.g+s4.h) {
									check = true;
									break;
								}
							};
						}
						if(!check) {
							openList.add(s4);
						}
					}
				}
				
				//step e=> push q on the closed list
				closeList.add(q);
				System.out.println("end");
				
			}
		
		
		//add the path from destination to source
		Stack<Cell> stack = new Stack<>();
		Cell dest = destination.clone();
		dest.parent = destination.parent;
		stack.push(dest);
		while(!(Main.x == destination.x && Main.y == destination.y)) {
			destination = destination.parent;
			Cell copyCell = destination.clone();
			copyCell.parent = destination.parent;
			stack.push(copyCell);
		}
		
		//once we find the shortest path, we can use moveToCell(Cell cell) function along the path
		stack.pop();
		while(!stack.isEmpty()) {
			Cell cur = stack.pop();
			Main.moveToCell(Main.getCell(cur.x, cur.y));
			System.out.println(cur.getX() + " and "+ cur.getY()+  " and " + Main.degree);
		}
		
		
	}
	
	
	public static void serializeMap() {
		try {
	         FileOutputStream fileOut = new FileOutputStream("map1.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(Main.cells);
	         out.close();
	         fileOut.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
	}
	
	public static boolean deserializeMap() {
		try {
			
			File f = new File("map1.ser");
			if(f.exists()) {
				FileInputStream fileIn = new FileInputStream("map1.ser");
		        ObjectInputStream in = new ObjectInputStream(fileIn);
		        Main.cells = (ArrayList<Cell>) in.readObject();
		        in.close();
		        fileIn.close();
		        
		        return true;
			}
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      } 
		
		return false;
	}
	
	
	

}
