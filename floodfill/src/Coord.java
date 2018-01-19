/**Coord Class (row, col) In java doc
 * | x | means that absolute value of x
 * x^2 means x * x
 * sign(x) means -1 if x < 0, 1 otherwise
 * iff means "if and only if"
 * Implementation notes:
 * compareTo -- compares the dist2(Origin) of the each Coord instance. a null instance is infinitely far
 * from the Origin
 * equals() compares row == row and col == col for the two instances
 * toString format is Coord:(row=%d,col=%d)
 * default contructor creates the origin (0,0) coordinate
 * @author Mu He
 *
 */
public class Coord extends java.lang.Object implements java.lang.Comparable<Coord>{
	public final int row;
	public final int col;


	public Coord(){
		this(0,0);
	}
	
	/**Constructors - row, column
	 * @param row1 - row part of coordinate
	 * @param col1 - columns part of coordinate
	 */
	public Coord(int row1, int col1){
		row = row1;
		col = col1;
	}
	
	/**"Copy" Constructor create a new Coord with identical row,col as the argument
	 * @param other - Coordate to make a deep copy,
	 */
	public Coord(Coord other){
		row = other.row;
		col = other.col;
	}
	
	/**Compute the "Manhattan" distance between two different coordinates
	 * @param b - coordinate against which to compute distance
	 * @return null, if b is null; else new Coord(|row-b.row|,|col-b.col|)
	 */
	public Coord dist(Coord b){
		if(b == null) return null;
		else return new Coord(Math.abs(row - b.row),Math.abs(col - b.col));
	}
	
	/**Compute the signed distance (difference)between two different coordinates
	 * @param b - coordinate against which to compute distance
	 * @return null, if b is null; else new Coord(row - b.row,col - a.col)
	 */
	public Coord diff(Coord b){
		if(b == null) return null;
		else return new Coord(row - b.row, col - b.col);
	}
	
	/**Compute the sum of distances squared between two Coords
	 * @param b - coordinate against which to compute distance
	 * @return Integer.MAX_VALUE if b is null, else (dist(b).row)^2 + (dist(b).col)^2
	 */
	public int dist2(Coord b){
		if(b == null) return Integer.MAX_VALUE;
		else return (dist(b).row)^2 + (dist(b).col)^2;
	}
	
	/**Compute the sign of the row, column component
	 * @return Coord(sign(row), sign(col))
	 */
	public Coord unit(){
		if(this.row >= 0){
			if(this.col >= 0) return new Coord(1,1);
			else return new Coord(1,-1);
		}
		else{
			if(this.col >= 0) return new Coord(-1,1);
			else return new Coord(-1,-1);
		}
	}
	
	/**Add coordinates together
	 * @param b - coordinate to add
	 * @return null, if b is null; else new Coord(row + b.row,col + a.col)
	 */
	public Coord add(Coord b){
		if(b == null) return null;
		else return new Coord(row + b.row,col + b.col);
	}
	
	/**compareTo -- compares the dist2(Origin) of the each Coord instance. a null instance is infinitely far
	 * from the Origin
	 * @param other
	 * @return
	 */
	public int compareTo(Coord other){
		return Integer.valueOf(this.dist2(new Coord())).compareTo(Integer.valueOf(other.dist2(new Coord())));
	}
	@Override
	/**
	 *Override our own equals(java.lang.Object other)s
	 */
	public boolean equals(java.lang.Object other){
		if(other instanceof Coord){
			if((this.row == ((Coord)other).row) && (this.col == ((Coord)other).col)) return true;
			else return false;
		}
		else return false;
	}
	@Override
	/**
	 * Override our own toString()
	 */
	public java.lang.String toString(){
		return "".format("Coord:(row=%d,col=%d)",row,col);
	}
}
