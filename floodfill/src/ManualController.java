import java.awt.event.*;

public class ManualController extends CarController {
	public ManualController(CoordInfo oracle) {
		super(oracle);
	}

	public void setDirection(int x){

		if(x == 1)
			direction = NORTH;
		if(x == 2)
			direction = EAST;
		if(x == 3)
			direction = WEST;
		if(x == 4)
			direction = SOUTH;
	}

	public  void setDefaultDirection(){

		direction = NORTH;
	}


	@Override
	public Coord roam(Coord current) {
		return direction;
	}
	@Override
	public Coord drive(Coord current, Coord goal) {
		return direction;
	}

}
