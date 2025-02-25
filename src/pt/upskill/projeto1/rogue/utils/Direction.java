package pt.upskill.projeto1.rogue.utils;

/**
 * @author POO2016
 *
 * Cardinal directions
 *
 */
public enum Direction {
	LEFT, UP, RIGHT, DOWN, UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT;

	public Vector2D asVector() {
		Vector2D v = null;
		if (this==Direction.UP){
			v= new Vector2D(0, -1);
		}
		if(this==Direction.DOWN){
			v = new Vector2D(0, 1);
		}
		if(this==Direction.LEFT){
			v = new Vector2D(-1, 0);
		}
		if(this==Direction.RIGHT){
			v = new Vector2D(1, 0);
		}
		if (this==Direction.UPRIGHT){
			v= new Vector2D(1, -1);
		}
		if(this==Direction.UPLEFT){
			v = new Vector2D(-1, -1);
		}
		if(this==Direction.DOWNRIGHT){
			v = new Vector2D(1, 1);
		}
		if(this==Direction.DOWNLEFT){
			v = new Vector2D(-1, 1);
		}
		return v;
	}
}
