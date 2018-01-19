import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *This is our simulation/game method. The main method is also here.
 * @author Mu He
 *
 */
public class Simulation extends JFrame implements ActionListener, ChangeListener,Runnable {
	public Grid grid;
	public GraphicsGrid game; 
	public GridSetup gs = null;
	private int row, col;
	private final int pixel = 10;
	private CarController ctrl;
	private ManualController man;
	private Random rand;
	private int ticks,currentTicks;
	private final int MAXTICKS = 100, MINTICKS = 10;
	private TimeTick tt;
	private Rider rider;
	JLabel top = new JLabel("Riders Loaded Player: 0 Robots: 0");
	JButton newgame;
	JButton pause;
	JSlider speed;
	JPanel buttom;
	JPanel topic;
	int xx[] = {0, 1, 0, -1};
	int yy[] = {1, 0, -1, 0};
	Stack<Integer> Qx, Qy;
	boolean  visit[][] = new boolean[100][100];
	int  dist[][] = new int[100][100];
	int  parentX[][] = new int[100][100];
	int  parentY[][] = new int[100][100];
	int  bestRouteX[][] = new int[100][100];
	int  bestRouteY[][] = new int[100][100];

	private int CENTERX1, CENTERY1, CENTERX2, CENTERY2, CENTERX3, CENTERY3, CENTERX4, CENTERY4;

	/**
	 * This is our constructor which use to add all non-graphical things inside
	 * @param args args to see which situation it is in
	 */
	public Simulation(String[] args){
		if(args.length == 0){

			row = 16;
			col = 16;

			grid = new Grid(new Coord(row,col));
			game = new GraphicsGrid(row,col,pixel);
		}
		else{
			if(args.length == 1) gs = new GridSetup(args[0]);
			if(args.length == 2) gs = null;

			if(gs == null){
				row = Integer.parseInt(args[0]);
				col = Integer.parseInt(args[1]);
				grid = new Grid(new Coord(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
				game = new GraphicsGrid(Integer.parseInt(args[0]), Integer.parseInt(args[1]),10);
			}
			else{
				if(gs.getDimension() == null) {
					row = 30;
					col = 30;
					grid = new Grid(new Coord(row,col));
					game = new GraphicsGrid(row,col,pixel);
				}
				else{
					row = gs.getDimension().row;
					col = gs.getDimension().col;
					grid = new Grid(gs.getDimension());
					game = new GraphicsGrid(row, col, pixel);
				}
			}	
		}
		speed = new JSlider(MINTICKS, MAXTICKS, MINTICKS);
		speed.addChangeListener(this);
		currentTicks = MINTICKS + MAXTICKS - speed.getValue();
		tt = new TimeTick(currentTicks, grid, this);
	}


	void updatePosition(int r1, int c1, int r2, int c2){

		if(r1+1 == r2){
			
			// Down
			man.setDirection(4);
		}
		else if(r2+1 == r1){

			// Up
			man.setDirection(1);

		}
		else if(c1+1 == c2){

			// Right
			man.setDirection(2);
		}
		else{

			// Down
			man.setDirection(3);
		}
	}

	boolean isCentre(int x, int y){

		if(x == CENTERX1 && y == CENTERY1)
			return true;		
		if(x == CENTERX2 && y == CENTERY2)
			return true;		
		if(x == CENTERX3 && y == CENTERY3)
			return true;		
		if(x == CENTERX4 && y == CENTERY4)
			return true;

		return false;
	}



	boolean floodfill(){

		if(Qx.isEmpty())
			return false;



		int x = Qx.peek(), y = Qy.peek();

		System.out.println("X = " + x + ", Y = " + y);

		int parentx = parentX[x][y];
		int parenty = parentY[x][y];

		if(isCentre(x, y)){
			dist[x][y] = 0; visit[x][y] = true; 

			updatePosition(x, y, parentX[x][y], parentY[x][y]);

			Qx.pop();
			Qy.pop();
			return true;
		}

		visit[x][y] = true;
		if(parentx != -1 && parenty != -1){
			
			if(dist[parentx][parenty] != -1){
				bestRouteX[x][y] = parentx;
				bestRouteY[x][y] =  parenty;
				dist[x][y] = dist[parentx][parenty]+1;
			}
		}


		for(int i = 0 ; i < 4 ; i++){

			int nxtX = x+xx[i];
			int nxtY = y+yy[i];

			if(!grid.coordFree(new Coord(nxtX, nxtY))) 
				continue;

			if(visit[nxtX][nxtY] == false){

				Qx.push(nxtX);
				Qy.push(nxtY);
				parentX[nxtX][nxtY] = x;
				parentY[nxtX][nxtY] = y;

				System.out.println("nxtX = " + nxtX + ", nxtY = " + nxtY);


				updatePosition(x, y, nxtX, nxtY);

				return true;
			}

			// Even if we have visited our neighbour, it could be the case that he has the shortest path!
			if(dist[nxtX][nxtY] != -1){ // Can our neighbour reach center?
				if(dist[x][y] == -1){ // Can we reach center?

					bestRouteX[x][y] = nxtX;
					bestRouteY[x][y] = nxtY;
					dist[x][y] = dist[nxtX][nxtY]+1;
				}
				else{
					if(dist[nxtX][nxtY]+1 < dist[x][y]){ // Can our neighbour reach the center faster?

						bestRouteX[x][y] = nxtX;
						bestRouteY[x][y] = nxtY;
						dist[x][y] = dist[nxtX][nxtY]+1;
					}
				}
			}	
		}

		Qx.pop();
		Qy.pop();

		updatePosition(x, y, parentx, parenty);	
		return true;
	}


	public void initMe() {

		for(int i = 0; i < 100 ; i++){
			for(int j = 0; j < 100 ; j++){

				visit[i][j] = false;
				dist[i][j] = -1;
				parentX[i][j] = -1;
				parentY[i][j] = -1;
				bestRouteX[i][j] = -1;
				bestRouteY[i][j] = -1;
			}
		}

		Qx = new Stack<Integer>();
		Qy = new Stack<Integer>();

		Qx.push(0);
		Qy.push(0);

		int hr = row/2, cr = col/2;

		CENTERX1 = hr;
		CENTERY1 = cr;
		CENTERX2 = hr;
		CENTERY2 = cr-1;
		CENTERX3 = hr-1;
		CENTERY3 = cr;
		CENTERX4 = hr-1;
		CENTERY4 = cr-1;
	}



	/**Make newGame, Set all parameter to default
	*/
	public void newgame(){
		tt.stop();
		grid.clear();
		game.clearObjects();

		initMe();

		ticks = 100;
        top.setText("".format("Riders Loaded Player: %d Robots: %d", grid.getplayerscores(), grid.getroboscores()));
		if(gs != null){
			
			for(int j = 0 ; j < gs.getObstacles().length; j++){
				Obstacle ob = new Obstacle();
				ob.setLocation(gs.getObstacles()[j]);
				game.addGridObject(ob);
				grid.addObstacle(ob);
			} 

			SharedCar player = new SharedCar(man, grid);
			player.setLocation(new Coord(0, 0));
			player.setColor(Color.RED);
			grid.addCar(player);
			game.addGridObject(player);
		}
		else {
			SharedCar player = new SharedCar(man, grid);
			player.setLocation(new Coord(0, 0));
			player.setColor(Color.RED);
			grid.addCar(player);
			game.addGridObject(player);
		}
		pause.setText("Pause");
		this.setFocusable(true);
		this.requestFocus();
		tt = new TimeTick(currentTicks, grid, this);
		(new Thread(tt)).start();
	}

	/**This should update the parameters of the game including cars, obstacles and riders
	 * Beautifully paint all the things in the Graphicsgrid
	 */
	public void update() {
		System.out.println(grid.toString());

		if(!floodfill()){
			//STOP


			int bestX = 0;
			int bestY = 0;


			System.out.println("Best dist = " + dist[0][0]);


			while(!isCentre(bestX, bestY)){




			//	grid.setUpColor(bestX, bestY, Color.YELLOW);
				int nxtX, nxtY;
				nxtX = bestRouteX[bestX][bestY];
				nxtY = bestRouteY[bestX][bestY];

				bestX = nxtX;
				bestY = nxtY;
				System.out.println("BestX = " + bestX + ", BestY = " + bestY);

				bestPath bp = new bestPath();
				bp.setLocation(new Coord(bestX, bestY));
				grid.addPath(bp);
				game.addGridObject(bp);
			}

			//System.out.println("BestX = " + bestX + ", BestY = " + bestY);


			tt.changeState();
			System.out.println(grid.toString());

		}



		

		game.repaint();
	}	

	/**Return the controller the car use
	 * @param command The String that is "east" stands for EastWestController
	 * "north" stands for NorthSouthController, "random" stands for RandomController, "manul" stands for ManulController.
	 * @return Controller that car should use
	 */
	public CarController control(String command){
		switch(command){
			case "east": return new EastWestController(grid);
			case "north": return new NorthSouthController(grid);
			case "random": return new RandomController(grid);
			default: return new EastWestController(grid);
		}
	}
	
	/**
	 * Decide which carcontroller to add
	 * @param i a number to see the situation
	 * @return a new carcontroller.
	 */
	public CarController random(int i){
		switch(i%3){
			case 0: return new EastWestController(grid);
			case 1: return new NorthSouthController(grid);
			case 2: return new RandomController(grid);
			default: return new EastWestController(grid);
		}
	}

	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args){
		Simulation game = new Simulation(args);
		SwingUtilities.invokeLater(game);
	}

	@Override
		/**ActionPerformed method to see which action should be used
		 * @param e the ActionEvent passed to the method
		 */
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == newgame) newgame();
			if(e.getSource() == pause){
				tt.changeState();
				if(pause.getText().equals("Pause"))pause.setText("Resume");
				else pause.setText("Pause");
			}
			this.setFocusable(true);
			this.requestFocus();
		}

	@Override
		/**StateChanged method to see which state is changed
		 * @param e the ChangeEvent passed to the method
		 */
		public void stateChanged(ChangeEvent e){
			if(ticks >= MINTICKS+MAXTICKS-speed.getValue()){
				currentTicks = MINTICKS + MAXTICKS - speed.getValue();
				tt.setTicks(currentTicks);
			}
			this.setFocusable(true);
			this.requestFocus();
		}
	/**
	 * run method paint all graphical parts in the graphicsgrid
	 */
	@Override
		public void run() {
		man = new ManualController(grid);
		SharedCar player = new SharedCar(man, grid);
		player.setLocation(new Coord(row/2, col/2));
		player.setColor(Color.RED);
		grid.addCar(player);
		game.addGridObject(player);
		
			if(gs != null){

				for(int j = 0 ; j < gs.getObstacles().length; j++){
					Obstacle ob = new Obstacle();
					ob.setLocation(gs.getObstacles()[j]);
					game.addGridObject(ob);
					grid.addObstacle(ob);
				} 
			}
			topic = new JPanel();
			topic.setLayout(new FlowLayout());
			topic.add(top);

			newgame = new JButton("New Game");
			pause = new JButton("Pause");
			speed = new JSlider(MINTICKS, MAXTICKS, MINTICKS);
			buttom = new JPanel();

			newgame.addActionListener(this);
			pause.addActionListener(this);
			speed.addChangeListener(this);

			buttom.add(newgame);
			buttom.add(pause);
			buttom.add(speed);

			this.setTitle("Rydr");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Container panel = this.getContentPane();
			panel.setLayout(new BorderLayout());

			panel.add(topic, BorderLayout.NORTH);
			panel.add(game, BorderLayout.CENTER);
			panel.add(buttom, BorderLayout.SOUTH);

			this.setFocusable(true);
			this.requestFocus();
			this.validate();
			this.pack();
			this.setVisible(true);


		}

}





