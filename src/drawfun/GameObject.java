package drawfun;

import javax.swing.JPanel;

//abstract class used to handle location and movement of all game objects
@SuppressWarnings("serial")
public abstract class GameObject extends JPanel{
	int x;
	int y;
	int speed = 8;
	
	public void moveTo(int x, int y){
		this.x = this.x + (x * speed);
		this.y = this.y + (y * speed);
		
		//prevent square from moving beyond the bounds of the arena
		if(this.x < 10){
			this.x = 10;
		}
		if(this.y < 10){
			this.y = 10;
		}
		if(this.y > 480){
			this.y = 480;
		}
		if(this.x > 480){
			this.x = 480;
		}
	}
	
	public void moveLeft(){
		this.x = this.x - (1 * speed);
	}
	
	public void moveRight(){
		this.x = this.x + (1 * speed);
	}

	public void moveUp(){
		this.y = this.y - (1 * speed);
	}

	public void moveDown(){
		this.y = this.y + (1 * speed);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
