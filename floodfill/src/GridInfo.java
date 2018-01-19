public interface GridInfo
{
	// Return true if SharedCar succesfully claimed the location
	public boolean claim(SharedCar car, Coord loc);
	// Return true if SharedCar  successfully loaded rider 
	public boolean riderLoaded(SharedCar car);
	// Return true if it is an Obstacle and destroy it
	public boolean isanddestroyObstacle(Coord loc);
}
