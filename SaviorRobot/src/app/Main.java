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
import java.util.ListIterator;
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
import localization.KillGiant;
import localization.Localize;
import localization.SavePrince;
import localization.TakeWeapon;
import mapping.MoveTo;
import mapping.NeighborCheck;

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
	public static boolean action = true;
	public static boolean mapDone = false;
	public static boolean isSafe = false;
	
	//if current cell is a black cell or not
	public static boolean isBlack = false;
	
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
	
	//arbitrators for mapping and localization
	public static Arbitrator mappingArbitrator, localizationArbitrator;
	
	
	//localization behavior controls
	public static boolean action2, weapon, giant, prince = false;
	
	//ultrasonic sensor motor
	public static RegulatedMotor sensorMotor;
	
	//robot body(brick)
	public static EV3 ev3 = (EV3)BrickFinder.getDefault();
	
	//robot screen
	public static GraphicsLCD graphicsLCD = ev3.getGraphicsLCD();
	
	
	public static void main(String[] args) throws Exception {
		
		//differential robot initiation
		RegulatedMotor leftMotor;
		RegulatedMotor rightMotor;
		sensorMotor = new EV3MediumRegulatedMotor(MotorPort.D);
		
		
    	
    	float leftDiam = 5.55f;
    	float rightDiam = 5.55f;
    	float trackWidth = 12.5f;
    	
    	
    	leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
    	rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
    	boolean reverse = false;
    	
    	
    	robot = new DifferentialPilot(leftDiam,rightDiam,trackWidth,leftMotor,rightMotor,reverse);
		
    	
    	robot.setLinearSpeed(10);
    	robot.setAngularSpeed(40);
    	
    	
    	//sensors' initiation
    	
    	colorSensor = new EV3ColorSensor(SensorPort.S1);
		sp1 = colorSensor.getColorIDMode();
		sample1 = new float[sp1.sampleSize()];
		
		ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S4);
	    sp2 = ultrasonicSensor.getDistanceMode();
	    sample2 = new float[sp2.sampleSize()];
	    
		
	    //server socket initiation
	    socket = new ServerSocket(1235);
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
				NeighborCheck nCheck = new NeighborCheck();
				MoveTo moveTo = new MoveTo();
				
				Behavior[] behaviorList = {moveTo,nCheck};
				mappingArbitrator = new Arbitrator(behaviorList);
				mappingArbitrator.go();
				
				Main.serializeMap();
			}
			//localization task
			else if(button == Button.ID_DOWN) {
				Localize localize = new Localize();
				TakeWeapon takeWeapon = new TakeWeapon();
				KillGiant killGiant = new KillGiant();
				SavePrince savePrince = new SavePrince();
				
				Behavior[] behaviorList = {savePrince,killGiant,takeWeapon,localize};
				localizationArbitrator = new Arbitrator(behaviorList);
				localizationArbitrator.go();
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
					action2 = false;
					weapon = false;
					giant = false;
					prince = false;
					
					Localize localize = new Localize();
					TakeWeapon takeWeapon = new TakeWeapon();
					KillGiant killGiant = new KillGiant();
					SavePrince savePrince = new SavePrince();
					
					Behavior[] behaviorList = {savePrince,killGiant,takeWeapon,localize};
					Arbitrator arbitrator = new Arbitrator(behaviorList);
					arbitrator.go();
				}else {
					//reset mapping
					cells.clear();
					previousCell = null;
					action = true;
					mapDone = false;
					isBlack = false;
					blackCellCount = 0;
					x = 0;
					y = 0;
					degree = 0;
					
					NeighborCheck nCheck = new NeighborCheck();
					MoveTo moveTo = new MoveTo();
					
					Behavior[] behaviorList = {moveTo,nCheck};
					Arbitrator arbitrator = new Arbitrator(behaviorList);
					arbitrator.go();
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
		
		start.addAll(start.size(), end);
		
		float temp[] = new float[4];
		for(int i=0;i<array.length;i++) {
			temp[i] = start.get(i);
		}
		
		
		return temp;
	}
	
	//checks whether two float array are almost equal(every element in arrays differs at most by .13) or not
	public static boolean equals(float[] arr1, float[] arr2) {
		
		for(int i=0;i<4;i++) {
			if(Math.abs(arr1[i]-arr2[i]) >= 0.13) {
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
		currentDist[0] = Main.getDistance();
		for(int i=1;i<4;i++) {
			Main.sensorMotor.rotate(-90);
			currentDist[i] = Main.getDistance();
			if(currentDist[i] < 0.21) currentWallCount++;
		}
		Main.sensorMotor.rotate(270);
		
		//locate yourself(find a unique cell to locate your coordinate) 
		for(Cell cell:Main.cells) {
			int cellWallCount = cell.getWallCount();
			if(cellWallCount == currentWallCount ) {
				float dist0[] = cell.getDistance();
				float dist1[] = Main.shift(dist0, 1);
				float dist2[] = Main.shift(dist1, 1);
				float dist3[] = Main.shift(dist2, 1);

				if(Main.equals(currentDist, dist0)) {
					degreeList.add(0);
					list.add(cell);
				}
				else if(Main.equals(currentDist, dist1)) {
					degreeList.add(90);
					list.add(cell);
				}
				else if(Main.equals(currentDist, dist2)) {
					degreeList.add(180);
					list.add(cell);
				}
				else if(Main.equals(currentDist, dist3)) {
					degreeList.add(270);
					list.add(cell);
				}
			}
		}

		return list;
	}
	
	//move by one cell
	public static void moveToCell(Cell cell) {

		
		if(Main.x != cell.getX()) {
			
			if(Main.x > cell.getUp().getX()) {
				Main.robot.rotate(90-Main.degree);
				Main.robot.travel(33);
				
				Main.degree = 90;
				Main.x--;
			}else {
				Main.robot.rotate(270-Main.degree);
				Main.robot.travel(33);
				
				Main.degree = 270;
				Main.x++;
			}
			
		}else {
			
			if(Main.y > cell.getY()) {
				Main.robot.rotate(180-Main.degree);
				Main.robot.travel(33);
				
				Main.degree = 180;
				Main.y--;
			}else {
				Main.robot.rotate(360-Main.degree);
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
		while(!stack.isEmpty()) {
			Cell cur = stack.pop();
			Main.moveToCell(Main.getCell(cur.x, cur.y));
		}
		
		
	}
	
	
	public static void serializeMap() {
		try {
	         FileOutputStream fileOut = new FileOutputStream("map.ser");
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
			
			File f = new File("map.ser");
			if(f.exists()) {
				FileInputStream fileIn = new FileInputStream("map.ser");
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
