
public class EastWestController extends CarController{
	public EastWestController(CoordInfo oracle) {
		super(oracle);
	}
	@Override
	public void setDefaultDirection() {
		direction = EAST;
	}
	
	public Coord reversedirection(){
		if(direction.equals(EAST)) return WEST;
		else if(direction.equals(WEST)) return EAST;
		else if(direction.equals(NORTH)) return SOUTH;
		else return NORTH;
	}

	@Override
	public Coord roam(Coord current) {
		if(oracle.coordFree(current.add(direction)))
		return direction;
		else{
		direction = reversedirection();
		return direction;
		}
	}

	@Override
	public Coord drive(Coord current, Coord goal) {
		Coord temp = goal.diff(current);
		if(temp.col != 0){
		if(temp.col < 0) direction = WEST;
		else if(temp.col > 0)direction = EAST;
		}
		else{
		if(temp.row < 0) direction = NORTH;
		else if(temp.row > 0) direction = SOUTH;
		}
		
		if(!oracle.coordFree(current.add(direction))){
		Coord north = goal.diff(current.add(NORTH));
		Coord south = goal.diff(current.add(SOUTH));
		if((north.compareTo(south) < 0) && oracle.coordFree(current.add(NORTH)))direction = NORTH;
		else if((north.compareTo(south) >= 0) && oracle.coordFree(current.add(SOUTH))) direction = SOUTH;
		else direction = reversedirection();
		}
		return direction;
	}
}
