/**Grid
 * Grid.java is where the state of our simulation is kept.
 * @author Mu He
 */
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Grid implements GridInfo,CoordInfo, KeyListener{
	private ArrayList<SharedCar> cars;
	private ArrayList<Obstacle> obstacles;
	private ArrayList<bestPath> bestPaths;
	private Rider rider;
	private GridObject[][] internal; 
	public int row,col;
	private int roboscores, playerscores;
	
	/**Constructor
	 * @param dim the dimensions of its grid(row*col)
	 */
	public Grid(Coord dim){
		row = dim.row;
		col = dim.col;
		cars = new ArrayList<SharedCar>();
		obstacles = new ArrayList<Obstacle>();
		bestPaths = new ArrayList<bestPath>();
		rider = null;
	}
	
	/**The Grid must keep track of all the Riders.
	 * @param r-Rider to be added.
	 * @return Return true if added successfully, false if not.
	 */
	public boolean addRider(Rider r){
		if(r.getLocation().row < 0 || r.getLocation().col < 0 || r.getLocation().row >= row || r.getLocation().col >= col) return false;
		else{
			if(coordFree(r.getLocation())){
			rider = r;
			for(SharedCar c : cars)
			c.newRider(r.getLocation());
			return true;
			}
			else return false;
		}
	}

	public void setUpColor(int r, int c,  Color myColor){

		internal[r][c].setColor(myColor);
	}
	
	/**The Grid must keep track of all the Cars.
	 * @param c-Car to be added.
	 * @return Return true if added successfully, false if not.
	 */
	public boolean addCar(SharedCar c){
		if(c.getLocation().row < 0 || c.getLocation().col < 0 || c.getLocation().row >= row || c.getLocation().col >= col) return false;
		else{
			if(coordFree(c.getLocation())){
			cars.add(c);
			if(rider != null){
				c.newRider(rider.getLocation());
			}
			return true;
			}
			else return false;
		}
	}
	
	/**The Grid must keep track of all the Obstacles.
	 * @param o-Obstacle to be added.
	 * @return Return true if added successfully, false if not.
	 */
	public boolean addObstacle(Obstacle o){
		if(o.getLocation().row < 0 || o.getLocation().col < 0 || o.getLocation().row >= row || o.getLocation().col >= col) return false;
		else{
			if(coordFree(o.getLocation())){
			obstacles.add(o);
			return true;
			}
			else return false;
		}
	}

	public boolean addPath(bestPath o){
		if(o.getLocation().row < 0 || o.getLocation().col < 0 || o.getLocation().row >= row || o.getLocation().col >= col) return false;
		else{
			if(coordFree(o.getLocation())){
			bestPaths.add(o);
			return true;
			}
			else return false;
		}
	}

	/** Determine if a Coordinate is free
 	 * @param loc location to query
 	 * @return  Return true if loc is in bounds and available
 	 *          else false.  
 	 *          return false if loc is null
	*/
	@Override
	public boolean coordFree(Coord loc) {
		if(loc != null && loc.row >= 0 && loc.col >= 0 && loc.row < row && loc.col < col){
			for(int i = 0;  i < obstacles.size(); i++){
			if(loc.equals(obstacles.get(i).getLocation()))return false;
			}
			for(int j = 0;  j < cars.size(); j ++){
			if(loc.equals(cars.get(j).getLocation()))return false;
			}
			return true;
		}
		else{
			return false;
		}
	}

	/**Use claim to make sure that the car doesn't claim a space that is
	 * already taken by an obstacle. Make sure the car does not go out of bounds.
	 * @param loc location to query
	 * @return Return true if SharedCar succesfully claimed the location
	 */
	@Override
	public boolean claim(SharedCar car, Coord loc) {
		if(loc == null || car == null)return false;
//		if(this.coordFree(loc)){
		if(this.coordFree(loc) && car.getLocation().dist2(loc) <= 1){
			return true;
		}
		else return false;
	}
	
	/**Use riderLoaded when a particular car has successfully loaded a rider, the rider is picked up (look at Rider.java -- for the
	 * methods to tell the rider which car he/she is in). riderLoaded() needs to also tell all the other
	 * cars to go back to roam() mode. 
	 * @param car car to query
	 * @Return Return true if SharedCar  successfully loaded rider 
	 */
	@Override
	public boolean riderLoaded(SharedCar car) {
//			if(car.getLocation().equals(r.getLocation()) && r.waiting()){
			if(rider.waiting()){
				rider.pickUp(car);
				if(car.getColor() == Color.RED) playerscores++;
				else roboscores++;
				rider = null;
			for(SharedCar c: cars) c.roam();
			return true;
			}
			return false;
	}
	/**Check if there is any rider waiting
	 * @return true if there is any rider waiting, otherwise return false
	 */
	public boolean waiting(){
		if(rider.waiting())return true;
		return false;
	}
	
	/**In order to know how many riders player picks.
	 * @return number of riders player picks
	 */
	public int getplayerscores(){
		return playerscores;
	}
	
	/**In order to know how many robocars player picks.
	 * @return number of riders robocars picks
	 */
	public int getroboscores(){
		return roboscores;
	}
	
	/**Set all to default
	 */
	public void clear(){
		cars.clear();
		obstacles.clear();
		bestPaths.clear();
		rider = null;
		roboscores = 0;
		playerscores = 0;
	}
	/**Let all car drive
	 * @return Reture true if all car drive else false
	 */
	public boolean drive(){
		for(SharedCar c: cars){
		c.drive();
		}
		return true;
	}
		
	/**write our own toString() method. A toString() method that pretty prints what's on the grid.
	 * @return Return our own toString() String.
	 */
	@Override
	public synchronized String toString(){
		internal = new GridObject[row][col];
		for(SharedCar c : cars)internal[c.getLocation().row][c.getLocation().col] = c;
		for(Obstacle o : obstacles)internal[o.getLocation().row][o.getLocation().col] = o;
		for(bestPath o : bestPaths)internal[o.getLocation().row][o.getLocation().col] = o;
		if(rider != null && rider.waiting())internal[rider.getLocation().row][rider.getLocation().col] = rider;
		String temp = "";
		temp += "=";
		for(int k = 0;  k < col; k++){
			temp += "=";
		}
		temp += "=\n";
		{
			for(int i = 0; i < internal.length; i++){
				for(int j = 0; j < internal[i].length; j++){
					if(j == 0){
						if(internal[i][j] == null) temp += "| ";
						else temp += "|" + internal[i][j].getSymbol();
					}
					else if(j == internal[i].length - 1){
						if(internal[i][j] == null) temp += " |\n";
						else temp += internal[i][j].getSymbol() + "|\n";
					}
					else{
						if(internal[i][j] == null) temp += " ";
						else temp += internal[i][j].getSymbol();
					}
				}
			}
		}
		temp += "=";
		for(int l = 0;  l < col; l++){
			temp += "=";
		}
		temp += "=";
			return temp;
	}
	
	/**Update in order to make all cars drive once.
	 */
	public synchronized void update(){
		this.drive();
	}
	
	/**
	 * get internal rider
	 * @return internal rider
	 */
	public Rider getriders(){
		return rider;
	}
	
	@Override
	public boolean isanddestroyObstacle(Coord loc){
		for(Obstacle o : obstacles){
			if(loc.equals(o.getLocation())){ 
				o = null;
				return true;
			}
		}
		return false;
	}
	/**Write our own player method. 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
	if(e.getKeyCode() == KeyEvent.VK_SPACE){
		for(SharedCar c : cars){
			if(c.getColor() == Color.RED || c.getColor() == Color.GREEN) c.drive();
		}
	}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
