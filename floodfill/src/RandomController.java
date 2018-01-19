import java.util.Random;

public class RandomController extends CarController {
	private Random rand;
	public RandomController(CoordInfo oracle) {
		super(oracle);
	}
	
	@Override
	public void setDefaultDirection() {
		return;
	}
	
	public Coord reversedirection(){
		if(direction.equals(EAST)) return WEST;
		else if(direction.equals(WEST)) return EAST;
		else if(direction.equals(NORTH)) return SOUTH;
		else return NORTH;
	}
	@Override
	public Coord roam(Coord current) {
		int temp;
		rand = new Random();
		temp = rand.nextInt(3);
		switch(temp){
		case 0: direction = NORTH;
		break;
		case 1: direction = SOUTH;
		break;
		case 2: direction = EAST;
		break;
		case 3: direction = WEST;
		break;
		}
		return direction;
	}
	
	public Coord northsouth(Coord temp){
		if(temp.row > 0) return SOUTH;
		else return NORTH;
	}
	public Coord eastwest(Coord temp){
		if(temp.col > 0) return EAST;
		else return WEST;
	}
	
	@Override
	public Coord drive(Coord current, Coord goal) {
		Coord temp = goal.diff(current);
		if(Math.abs(temp.row) < Math.abs(temp.col)) direction = eastwest(temp);
		else direction = northsouth(temp);
		
		if(!oracle.coordFree(current.add(direction))){
		Coord[] compare = new Coord[4];
		compare[0] = goal.diff(current.add(EAST));
		compare[1] = goal.diff(current.add(NORTH));
		compare[2] = goal.diff(current.add(SOUTH));
		compare[3] = goal.diff(current.add(WEST));
		Coord min = goal.diff(current.add(EAST));
		int minmin = 0;
		for(int i = 0;  i < compare.length; i++){
			if(min.compareTo(compare[i]) > 0){
				min = compare[i];
				minmin = i;
			}
		}
		if(minmin == 0 && oracle.coordFree(current.add(EAST))) direction = EAST;
		else if(minmin == 1 && oracle.coordFree(current.add(NORTH))) direction = NORTH;
		else if(minmin == 2 && oracle.coordFree(current.add(SOUTH))) direction = SOUTH;
		else if(minmin == 3 && oracle.coordFree(current.add(WEST))) direction = WEST;
		else direction = reversedirection();
		}
		return direction;
	}

}
