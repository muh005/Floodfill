
public class NorthSouthController extends CarController {
    public NorthSouthController(CoordInfo oracle) {
		super(oracle);
	}
	@Override
	public void setDefaultDirection() {
		direction = NORTH;
		
	}
	
	public Coord reversedirection(){
		if(direction.equals(EAST)) return WEST;
		else if(direction.equals(WEST)) return EAST;
		else if(direction.equals(NORTH)) return SOUTH;
		else return NORTH;
	}
	@Override
	public Coord roam(Coord current) {
		if(oracle.coordFree(current.add(this.getDirection())))
			return direction;
			else{
			direction = reversedirection();
			return direction;
		}
	}
	@Override
	public Coord drive(Coord current, Coord goal) {
		Coord temp = goal.diff(current);
		if(temp.row != 0){
		if(temp.row < 0) direction = NORTH;
		else if(temp.row > 0)direction = SOUTH;
		}
		else{
		if(temp.col < 0) direction = WEST;
		else if(temp.col > 0) direction = EAST;
		}

		if(!oracle.coordFree(current.add(direction))){
			Coord east = goal.diff(current.add(EAST));
			Coord west = goal.diff(current.add(WEST));
			if((east.compareTo(west) < 0) && oracle.coordFree(current.add(EAST))) direction = EAST;
			else if((east.compareTo(west) >= 0) && oracle.coordFree(current.add(WEST))) direction = WEST;
			else direction = reversedirection();
		}
		return direction;
	}

}
