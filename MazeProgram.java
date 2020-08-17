import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.math.*;
public class MazeProgram extends JPanel implements KeyListener,MouseListener
{
	JFrame frame;
	//declare an array to store the maze - Store Wall(s) in the array
	int dir =0;
	Explorer explorer = new Explorer(0, 1);
	Wall[][] walls = null;
	boolean done = false;
	boolean p = false;
	int max =7;
	int hallway =0;
	int t = 0;
	int width = 50;
	int tracker = 0;
	int lim =0;
	int zubH;
	int zubV;
	int sLeft = 0;
	int sRight=0;
	int times =0;
	int randx =0;
	int randy =0;
	int repx=0;
	int repy=0;
	int keyused =0;
	int pressed =0;
	boolean available_2d=false;
	boolean started = false;
	boolean visible = false;
	boolean trap = false;
	boolean replenish = false;
	boolean displayTrap = false;
	boolean win = false;
	int health = 100;
	//Color t = new Color(0,0,0);
	//Color b = new Color(0,0,0);
	Color wallColor = new Color(0,0,0);
	ArrayList<Polygon> leftpolygons = new ArrayList<>();
	ArrayList<Polygon> rightpolygons = new ArrayList<>();
	ArrayList<Boolean> leftOpen = new ArrayList<>();
	ArrayList<Boolean> rightOpen = new ArrayList<>();
	ArrayList<Polygon> floor = new ArrayList<>();
	ArrayList<Polygon> ceiling = new ArrayList<>();
	public MazeProgram()
	{
		setBoard();
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,800);
		frame.setVisible(true);
		frame.addKeyListener(this);
		//this.addMouseListener(this); //in case you need mouse clicking
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//setBoard();
		setWalls();
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		//this will set the background color
		g2.fillRect(0,0,1000,800);  //since the screen size is 1000x800
									//it will fill the whole visible part
									//of the screen with a black rectangle

		//drawBoard here!

		Color c = new Color(211, 211, 211);
		Color f = new Color(0, 255, 0);
		Color wallc = new Color(0,0,0);
		try{
			for(int x=0;x<Math.min(max, ceiling.size());x++)
			{
					Color finalc = new Color(c.getRed()-x*25, c.getGreen()-x*25, c.getBlue()-x*25);
					Color finalf = new Color(0, c.getGreen()-x*25, 0);
					g2.setColor(Color.BLACK);
					g2.drawPolygon(ceiling.get(x));
					g2.drawPolygon(floor.get(x));
					GradientPaint gp_floor = new GradientPaint(floor.get(x).xpoints[0], floor.get(x).ypoints[3], f, floor.get(x).xpoints[0]+1, floor.get(x).ypoints[3], finalf);
					GradientPaint gp_ceiling = new GradientPaint(ceiling.get(x).xpoints[0], ceiling.get(x).ypoints[3], c, ceiling.get(x).xpoints[0]+1, ceiling.get(x).ypoints[3], finalc);
					g2.setPaint(gp_ceiling);
					g2.fillPolygon(ceiling.get(x));
					g2.setPaint(gp_floor);
					g2.fillPolygon(floor.get(x));
					if(!leftOpen.get(x))
					{
						g2.setColor(Color.BLACK);
						int[] asx = {50*x, 50+50*x, 50*x};
						int[] asy = {ceiling.get(x).ypoints[0], ceiling.get(x).ypoints[2],ceiling.get(x).ypoints[2]};
						g2.drawPolygon(asx, asy, 3);
						g2.setPaint(finalc);
						g2.fillPolygon(asx, asy, 3);
						g2.setColor(Color.BLACK);
						int[] ary = {floor.get(x).ypoints[0], floor.get(x).ypoints[2],floor.get(x).ypoints[2]};
						g2.drawPolygon(asx, ary, 3);
						g2.setPaint(finalf);
						g2.fillPolygon(asx, ary, 3);
					}
					if(!rightOpen.get(x))
					{
						g2.setColor(Color.BLACK);
						int[] asx = {1000-50*x, 950-50*x, 1000-50*x};
						int[] asy = {ceiling.get(x).ypoints[0], ceiling.get(x).ypoints[2],ceiling.get(x).ypoints[2]};
						g2.drawPolygon(asx, asy, 3);
						g2.setPaint(finalc);
						g2.fillPolygon(asx, asy, 3);
						g2.setColor(Color.BLACK);
						int[] ary = {floor.get(x).ypoints[0], floor.get(x).ypoints[2],floor.get(x).ypoints[2]};
						g2.drawPolygon(asx, ary, 3);
						g2.setPaint(finalf);
						g2.fillPolygon(asx, ary, 3);
					}
					
				}
		}
		catch(Exception exception)
		{

		}
		Color rightleft = new Color(255, 0, 0);
			for(int x=0;x<Math.min(max, rightpolygons.size());x++)
			{
				Color finalr = new Color(rightleft.getRed()-x*20, 0, 0);
				g2.setColor(Color.BLACK);
				g2.drawPolygon(rightpolygons.get(x));			
				GradientPaint gp = new GradientPaint(rightpolygons.get(x).xpoints[0], rightpolygons.get(x).ypoints[0], rightleft, rightpolygons.get(x).xpoints[3]+1, rightpolygons.get(x).ypoints[0], finalr);
				g2.setPaint(gp);
				g2.fillPolygon(rightpolygons.get(x));
				wallc = finalr;
			}
			for(int x=0;x<Math.min(max, leftpolygons.size());x++)
			{
				Color finall = new Color(rightleft.getRed()-x*20, 0, 0);
				g2.setColor(Color.BLACK);
				g2.drawPolygon(leftpolygons.get(x));
				GradientPaint gp = new GradientPaint(leftpolygons.get(x).xpoints[3], leftpolygons.get(x).ypoints[0], rightleft, leftpolygons.get(x).xpoints[3]+1, leftpolygons.get(x).ypoints[0], finall);
				g2.setPaint(gp);
				g2.fillPolygon(leftpolygons.get(x));
				if(leftpolygons.size()>rightpolygons.size())
					wallc = finall;
			}
			try{
				int ksi = Math.min(max, hallway);
				int[] wallx = {floor.get(ksi-1).xpoints[3], floor.get(ksi-1).xpoints[2],floor.get(ksi-1).xpoints[2],floor.get(ksi-1).xpoints[3]};
				int[] wally= {floor.get(ksi-1).ypoints[3], floor.get(ksi-1).ypoints[3], ceiling.get(ksi-1).ypoints[2],ceiling.get(ksi-1).ypoints[2]};
		
				g2.setColor(Color.BLACK);
				g2.drawPolygon(wallx, wally, 4);
				if(ksi==1)
					g2.setColor(Color.RED);
				else g2.setColor(wallc);
				g2.fillPolygon(wallx, wally, 4);
					for(int x=0;x<ksi;x++)
					{
						if(!leftOpen.get(x))
						{
							g2.setColor(Color.BLACK);
							int[] ax = {50*x, 50+50*x, 50+50*x, 50*x};
							int[] ay = {floor.get(x).ypoints[3],floor.get(x).ypoints[3], ceiling.get(x).ypoints[3], ceiling.get(x).ypoints[3]};
							g2.drawPolygon(ax, ay, 4);
							if(ksi == 1)
								g2.setColor(Color.RED);
							else g2.setColor(wallc);
							g2.fillPolygon(ax, ay, 4);
						}
						if(!rightOpen.get(x))
						{
							g2.setColor(Color.BLACK);
							int[] ax = {1000-50*x, 950-50*x, 950-50*x, 1000-50*x};
							int[] ay = {floor.get(x).ypoints[3],floor.get(x).ypoints[3], ceiling.get(x).ypoints[3], ceiling.get(x).ypoints[3]};
							g2.drawPolygon(ax, ay, 4);
							if(ksi==1)
								g2.setColor(Color.RED);
							else g2.setColor(wallc);
							g2.fillPolygon(ax, ay, 4);
						}
					}
				}
				catch(Exception exception)
				{

				}	
		//System.out.println(leftpolygons.size()+"  "+rightpolygons.size());
	/*	g2.setColor(Color.CYAN);
			for(int x=0;x<walls.length;x++)
			{
				for(int y=0;y<walls[0].length;y++)
				{
					if(walls[x][y].getOccupant() == 0)
					{
						g2.fillRect(walls[x][y].getY()*10, walls[x][y].getX()*10, 10, 10);
						
					}
				}
			}
			g2.setColor(Color.CYAN);
			g2.drawRect(explorer.getX()*10, explorer.getY()*10,10,10);*/
			g2.setColor(Color.ORANGE);
			g2.setFont(new Font("Times New Roman",Font.PLAIN,36));
		
		g2.setColor(Color.ORANGE);
		if(available_2d && !visible && keyused ==0 && walls[explorer.getY()][explorer.getX()].getOccupant()==5)
		{
			g2.drawString("You now have access to the map!", 200, 400);
			visible = true;
		}
		if(available_2d && visible)
		{
			g2.setColor(Color.CYAN);
			for(int x=0;x<walls.length;x++)
			{
				for(int y=0;y<walls[0].length;y++)
				{
					if(walls[x][y].getOccupant() == 0)
					{
						g2.fillRect(walls[x][y].getY()*25+200, walls[x][y].getX()*25+300, 25, 25);
						
					}
				}
			}
			g2.setColor(Color.CYAN);
			g2.drawRect(explorer.getX()*25+200, explorer.getY()*25+300,25,25);
			g2.setColor(Color.MAGENTA);
			g2.fillRect(explorer.getX()*25+200, explorer.getY()*25+300,25,25);
			keyused++;
		}	
				//x & y would be used to located your
									//playable character
									//values would be set below
									//call the x & y values from your Explorer class
									//explorer.getX() and explorer.getY()
		//other commands that might come in handy
		
		g2.setColor(Color.ORANGE);
		//g2.drawString(dir+"", 500, 750);
		g2.drawString("Health: "+health+"", 750, 50);
		if(health<=0)
		{
			done = true;
		}
		if(!available_2d)
		{
			g2.setColor(Color.ORANGE);
			g2.drawString("Map at: ("+randx+", "+randy+")", 10, 100);
		}
		g2.setColor(Color.ORANGE);
		switch(dir)
		{
			case 0:
			g2.drawString("Direction: East", 750, 100);
			break;
			case 1:
			g2.drawString("Direction: South", 750, 100);
			break;
			case 2:
			g2.drawString("Direction: West", 750, 100);
			break;
			case 3:
			g2.drawString("Direction: North", 750, 100);
			break;
		}
			g.setColor(Color.BLUE);
			g.fillOval(700,450,200,200);
			g.setColor(Color.RED);
			g.setFont(new Font("Helvetica",Font.PLAIN,20));
			g.setColor(Color.BLACK);
			g.drawString("N",795,470);
			g.drawString("S",795,645);
			g.drawString("W",705,560);
			g.drawString("E",880,560);
			g.setColor(Color.BLACK);
			g.fillOval(790,540,20,20);
			if(dir==3){
				g.setColor(Color.RED);
				g.fillOval(795,470,10,90);
			}
			else if(dir==0){
				g.setColor(Color.RED);
				g.fillOval(790,545,90,10);
			}
			else if(dir==1){
				g.setColor(Color.RED);
				g.fillOval(795,540,10,90);
			}
			else if(dir==2){
				g.setColor(Color.RED);
				g.fillOval(720,545,90,10);
			}
				//you can also use Font.BOLD, Font.ITALIC, Font.BOLD|Font.Italic
		//g.drawOval(x,y,10,10);
		//g.fillRect(x,y,100,100);
		//g.fillOval(x,y,10,10);
		g2.setFont(new Font("Times New Roman",Font.PLAIN,36));
		g2.setColor(Color.ORANGE);
		g2.drawString("You are at: ("+explorer.getX()+", "+explorer.getY()+")", 10, 50);
		g2.setColor(Color.BLACK);
		g2.drawString("Moves: "+pressed, 400, 750);
		if(walls[explorer.getY()][explorer.getX()].getOccupant()==7)
		{
			trap=true;
			displayTrap=true;
			g2.setColor(Color.BLACK);
			g2.fillRect(0,0,1000,800);
			g2.setColor(Color.WHITE);
			g2.drawString("TRAPPED!", 200, 400);
			walls[explorer.getY()][explorer.getX()].setOccupant(1);
		}
		if(displayTrap)
		{
			g2.setColor(Color.BLACK);
			g2.drawString("Restore Health at: ("+repx+", "+repy+")", 350, 50);
		}
		if(walls[explorer.getY()][explorer.getX()].getOccupant()==8 && trap)
		{
			replenish = true;
			displayTrap=false;
			g2.setColor(Color.BLACK);
			g2.fillRect(0,0,1000,800);
			g2.setColor(Color.WHITE);
			health = 100;
			g2.drawString("HEALTH RESTORED!", 200, 400);
			walls[explorer.getY()][explorer.getX()].setOccupant(1);
		}	
		if(walls[explorer.getY()][explorer.getX()].getOccupant()==2 && !done)
			win = true;				
		if(done && !win)
		{
			g2.setColor(Color.BLACK);
			g2.fillRect(0,0,1000,800);
			g2.setColor(Color.WHITE);
			g2.drawString("Game Over!", 300, 500);
		}
		if(win && !done)
		{
			g2.setColor(Color.BLACK);
			g2.fillRect(0,0,1000,800);
			g2.setColor(Color.WHITE);
			g2.drawString("You Win!", 300, 500);
		}
	}
	public void setBoard()
	{
		//choose your maze design

		//pre-fill maze array here

		File name = new File("maze1.txt");
        int r=0;
		int mazeLength = 0;
		ArrayList<String> tempStr = new ArrayList<>();
		try
		{
			BufferedReader input = new BufferedReader(new FileReader("mazeone.txt"));
			String text;
			while( (text=input.readLine())!= null)
			{
                r++;
                String[] lengths = text.split("");
                mazeLength = lengths.length;
                tempStr.add(text);
				//your code goes in here to chop up the maze design
				//fill maze array with actual maze stored in text file
			}
			walls = new Wall[r][mazeLength];
			for(int x=0;x<tempStr.size();x++)
			{
				for(int y=0;y<tempStr.get(0).length();y++)
				{
					if(tempStr.get(x).charAt(y)== '#')
					{
						walls[x][y] = new Wall(x,y,0);
					}
					else if(tempStr.get(x).charAt(y)== ' ')
					{
						walls[x][y] = new Wall(x,y,1);
					}
					else if(tempStr.get(x).charAt(y)== 'e')
					{
						walls[x][y] = new Wall(x,y,2);
					}
					else if(tempStr.get(x).charAt(y)== 'E')
					{
						walls[x][y] = new Wall(x,y,3);
					}
				}
			}
			randx = (int)(Math.random()*19);
			randy = (int)(Math.random()*9);
			while(walls[randy][randx].getOccupant()==0)
			{
				randx = (int)(Math.random()*19);
				randy = (int)(Math.random()*9);
			}
			//System.out.println(randx+" "+randy);
			walls[randy][randx].setOccupant(5);
			int tx = (int)(Math.random()*19);
			int ty = (int)(Math.random()*9);
			while(walls[ty][tx].getOccupant()==0)
			{
				tx = (int)(Math.random()*19);
				ty = (int)(Math.random()*9);
			}
			//System.out.println(tx+" "+ty);
			walls[ty][tx].setOccupant(7);
			repx = (int)(Math.random()*19);
			repy = (int)(Math.random()*9);
			while(walls[repy][repx].getOccupant()==0 || walls[repy][repx].getOccupant()==7 )
			{
				repx = (int)(Math.random()*19);
				repy = (int)(Math.random()*9);
			}
			//System.out.println(repx+" "+repy);
			walls[repy][repx].setOccupant(8);
		}
		catch (IOException io)
		{
			System.err.println("File error");
		}
		
	}

	public void setWalls()
	{
		//when you're ready for the 3D part
		//int[] c1X={150,550,450,250};
		//int[] c1Y={50,50,100,100};
		//Wall ceiling1=new Wall(c1X,c1Y,4);  //needs to be set as a global!
						//parameters - x coordinates, y coordinates, # of points
		int r = explorer.getX();
		int c = explorer.getY();
		leftpolygons.clear();
		rightpolygons.clear();
		ceiling.clear();
		floor.clear();
		leftOpen.clear();
		rightOpen.clear();
		hallway = 0;
		lim = 0;
		int l =0;
		try{
			switch(dir)
			{
				case 0: 
				lim = explorer.getX();
				while(walls[c][r].getOccupant()!=0) 
				{
					r++;
					hallway++;
				}
				break;
				case 1:
				lim = explorer.getY();
				while(walls[c][r].getOccupant()!=0)
				{
					c++;
					hallway++;
				}
				break;
				case 2:
				while(walls[c][r].getOccupant()!=0)
				{
					r--;
					hallway++;
				}
				break;
				case 3:
				while(walls[c][r].getOccupant()!=0)
				{
					c--;
					hallway++;
				}
				break;
			}
		}
		catch(Exception exception)
		{

		}
		c = explorer.getY();
		r = explorer.getX();
		if(tracker == 0){
			t=hallway;
			zubH=r;
			zubV=c;
		}
		switch(dir)
		{
			case 0:
			for(lim = r; lim<zubH+t;lim++)
			{
				l = lim-explorer.getX();
				//System.out.println("I HADS");
				int[] leftwallsx={50*(l),50+50*l,50+50*l,50*l};
				int[] leftwallsy={50*(l),50+50*(l),750-50*(l),800-50*(l)};
				int[] rightwallsx={950-50*(l),1000-50*l,1000-50*l,950-50*l};
				int[] rightwallsy={750-50*(l),800-50*(l),50*(l),50+50*(l)};
				leftOpen.add(walls[c-1][lim].getOccupant()==0);
				rightOpen.add(walls[c+1][lim].getOccupant()==0);
				if(walls[c-1][lim].getOccupant()==0)
				{
					leftpolygons.add(new Polygon(leftwallsx,leftwallsy,4));
				}
				if(walls[c+1][lim].getOccupant()==0)
				{
					rightpolygons.add(new Polygon(rightwallsx,rightwallsy,4));
				} 
			}
			break;
			case 1:
			for(lim = c; lim<t+zubV;lim++)
			{
				l = lim-explorer.getY();
				int[] leftwallsx={50*(l),50+50*l,50+50*l,50*l};
				int[] leftwallsy={50*(l),50+50*(l),750-50*(l),800-50*(l)};
				int[] rightwallsx={950-50*(l),1000-50*l,1000-50*l,950-50*l};
				int[] rightwallsy={750-50*(l),800-50*(l),50*(l),50+50*(l)};
				try{
					leftOpen.add(walls[lim][r+1].getOccupant()==0);
					rightOpen.add(walls[lim][r-1].getOccupant()==0);
					if(walls[lim][r+1].getOccupant()==0)
					{
						leftpolygons.add(new Polygon(leftwallsx,leftwallsy,4));
					}
					if(walls[lim][r-1].getOccupant()==0)
					{
						rightpolygons.add(new Polygon(rightwallsx,rightwallsy,4));
					} 
				}
				catch(Exception exception)
				{

				}
			}
			break;
			case 2:
			int k=0;
			int w = 0;
			for(int x=r;x>=0;x--)
			{
				if(walls[c][x].getOccupant()==0)
				{
					w=x;
					break;
				}
			}
			for(lim = r;lim>w;lim--)
			{
				l = k;
				int[] leftwallsx={50*(l),50+50*l,50+50*l,50*l};
				int[] leftwallsy={50*(l),50+50*(l),750-50*(l),800-50*(l)};
				int[] rightwallsx={950-50*(l),1000-50*l,1000-50*l,950-50*l};
				int[] rightwallsy={750-50*(l),800-50*(l),50*(l),50+50*(l)};
				leftOpen.add(walls[c+1][lim].getOccupant()==0);
				rightOpen.add(walls[c-1][lim].getOccupant()==0);
				if(walls[c+1][lim].getOccupant()==0)
				{
					leftpolygons.add(new Polygon(leftwallsx,leftwallsy,4));
				}
				if(walls[c-1][lim].getOccupant()==0)
				{
					rightpolygons.add(new Polygon(rightwallsx,rightwallsy,4));
				} 
				k++;
			}
			break;
			case 3:
			int j=0;
			int m= 0;
			for(int x=c;x>=0;x--)
			{
				if(walls[x][r].getOccupant()==0)
				{
					m=x;
					break;
				}
			}
			for(lim = c;lim>m;lim--)
			{
				l = j;
				int[] leftwallsx={50*(l),50+50*l,50+50*l,50*l};
				int[] leftwallsy={50*(l),50+50*(l),750-50*(l),800-50*(l)};
				int[] rightwallsx={950-50*(l),1000-50*l,1000-50*l,950-50*l};
				int[] rightwallsy={750-50*(l),800-50*(l),50*(l),50+50*(l)};
				
				try{
					leftOpen.add(walls[lim][r-1].getOccupant()==0);
				rightOpen.add(walls[lim][r+1].getOccupant()==0);
					if(walls[lim][r-1].getOccupant()==0)
					{
						//System.out.println("Im HER");
						leftpolygons.add(new Polygon(leftwallsx,leftwallsy,4));
					}
					if(walls[lim][r+1].getOccupant()==0)
					{
						//System.out.println("Im HER");
						rightpolygons.add(new Polygon(rightwallsx,rightwallsy,4));
					} 
				}
				catch(Exception exception)
				{

				}
				j++;
			}
			break;
		}
		for(int x=0;x<hallway;x++)
		{
			int[] floorx = {50*x, 1000-50*x, 950-50*x, 50+50*x};
			int[] floory = {800-50*x, 800-50*x, 750-50*x,750-50*x};
			int[] ceilingx = {50*x, 1000-50*x, 950-50*x, 50+50*x};
			int[] ceilingy = {50*x, 50*x, 50+50*x, 50+50*x};
			floor.add(new Polygon(floorx, floory, 4));
			ceiling.add(new Polygon(ceilingx, ceilingy, 4));
		}
		tracker++;
	}
	public int changeDirPositive(int d)
	{
		tracker =0;
		d++;
		if(d>3)
			d=0;
		return d;
	}
	public int changeDirNegative(int d)
	{
		tracker = 0;
		d--;
		if(d<0)
			d=3;
		return d;
	}
	public void keyPressed(KeyEvent e)
	{
		int movex=0;
		int movey=0;
		boolean movable = true;
		started = true;
		if(e.getKeyCode()==37)
			dir = changeDirNegative(dir);
		if(e.getKeyCode()==39)
			dir = changeDirPositive(dir);
		//System.out.println(explorer.getX()+" "+explorer.getY());
		if(walls[explorer.getY()][explorer.getX()].getOccupant()==5)
		{
			available_2d=true;
		}
		if(available_2d)
		{
			if(e.getKeyChar() == 'm')
				times++;
			if(times%2 == 1)
				visible = true;
			else visible = false;
		}
		switch(dir)
		{
			case 0:movex=1;
				   movey=0;
				   if(explorer.getX()+movex>19)
				   	movable=false;
				   break;
			case 1:movey=1;
					movex=0;
					break;
			case 2:movex=-1;
					movey=0;
					if(explorer.getX()+movex<0)
						movable = false;
					break;
			case 3:movey=-1;
					movex=0;
					break;
			default:break;
		}
		if(done || !movable)
		{
			movex=0;
			movey = 0;
		}
		if(walls[explorer.getY()+movey][explorer.getX()+movex].getOccupant()!=0)
		{
			if(e.getKeyCode()==38)
			{
				//wallColor = new Color(wallColor.getRed()+35, 0,0);
				explorer.move(movex, movey);
				if(trap && !replenish)
				{
					health-=5;
				}
				pressed++;
				try{
						leftpolygons.remove(0);
						rightpolygons.remove(0);
						ceiling.remove(0);
						floor.remove(0);
						leftOpen.remove(0);
						rightOpen.remove(0);
				}
				catch(Exception exception)
				{

				}
				
			}
			if(walls[explorer.getY()][explorer.getX()].getOccupant()==2)
				done = true;
			
		}
		repaint();
	}
	public void keyReleased(KeyEvent e)
	{
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public void mouseClicked(MouseEvent e)
	{
	}
	public void mousePressed(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}
	public static void main(String args[])
	{
		MazeProgram app=new MazeProgram();
	}
}