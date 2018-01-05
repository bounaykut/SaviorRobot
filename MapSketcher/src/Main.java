

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main extends JFrame {
	
	private static final long serialVersionUID = 2280874288993963333L;
	
	static int curX=250;
	static int curY=250;
	static int unitCellLength = 50;
	static InputStream inputStream;
	static DataInputStream dataInputStream;
	
	static int upWall;
	static int bottomWall;
	static int leftWall;
	static int rightWall;
	
	static ArrayList<Line> lines = new ArrayList<>();
			
	public Main() {
		super("Localization Monitor for CmpE 434");
		setSize( 700, 500 );
		setFocusable(true);
		setVisible( true );
		
		
	}
	
	public static void main(String[] args) throws Exception	{
		
		Main monitor = new Main();
		
		monitor.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		String ip = "10.0.1.1";
		
		@SuppressWarnings("resource")
		Socket socket = new Socket(ip, 1235);
		System.out.println("Connected!");
		
		inputStream = socket.getInputStream();
		dataInputStream = new DataInputStream(inputStream);
		BufferedReader bf = new BufferedReader(new InputStreamReader(dataInputStream));
		
		//
		OutputStream outputStream = socket.getOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		Scanner scanner = new Scanner(System.in);
		//
		
		while( true ){
			curX = 250 + 50*Integer.parseInt(bf.readLine());
			curY = 250 - 50*Integer.parseInt(bf.readLine());
			
			upWall = Integer.parseInt(bf.readLine());
			leftWall = Integer.parseInt(bf.readLine());
			bottomWall = Integer.parseInt(bf.readLine());
			rightWall = Integer.parseInt(bf.readLine());
			System.out.println(curX + " " +curY + " " +upWall + " " + leftWall + " " + bottomWall + " " + rightWall);
			System.out.println("sa ciktim");
			
			monitor.repaint();
		}
	}

	
	public void paint( Graphics g ) {
		super.paint( g );
		displayMap(g);	
	}

	public void displayMap(Graphics g ){
		Graphics2D g2 = ( Graphics2D ) g;
		
		g2.setStroke( new BasicStroke( 5.0f ));
		
		g2.setPaint(Color.RED);
		for(int i=0;i<lines.size();i++) {
			g2.drawLine(lines.get(i).fromX,lines.get(i).fromY,lines.get(i).toX,lines.get(i).toY);
		}

		if(upWall == 1) {
			g2.setPaint(Color.RED);
			//upper line
			g2.drawLine(curX-25, curY-25, curX+25, curY-25);
			
			lines.add(new Line(curX-25, curY-25, curX+25, curY-25));
		}
		
		if(leftWall == 1) {
			g2.setPaint(Color.RED);
			//left line
			g2.drawLine(curX-25, curY-25, curX-25, curY+25);
			
			lines.add(new Line(curX-25, curY-25, curX-25, curY+25));
		}
		
		if(bottomWall == 1) {
			g2.setPaint(Color.RED);
			//bottom line
			g2.drawLine(curX-25, curY+25, curX+25, curY+25);
			
			lines.add(new Line(curX-25, curY+25, curX+25, curY+25));
		}
		
		if(rightWall == 1) {
			g2.setPaint(Color.RED);
			//right line
			g2.drawLine(curX+25, curY-25, curX+25, curY+25);
			
			lines.add(new Line(curX+25, curY-25, curX+25, curY+25));
		}
	
		
	}
	
	
}


