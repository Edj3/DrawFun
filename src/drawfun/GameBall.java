package drawfun;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class GameBall extends GameObject{
	protected Image gameBall;
	public int ballSize;
	public int ballSpeed = 5;

	GameBall() throws IOException{
		File ballFile = new File("soccergameball.png");
		gameBall = ImageIO.read(ballFile);
		gameBall.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
		setX(250);
		setY(250);
		setBallSize(20);
	}
	
	//Override moveTo specifically for game ball due to size difference
	public void moveTo(int x, int y){
		this.x = this.x + (x * ballSpeed);
		this.y = this.y + (y * ballSpeed);
		
		//prevent square from moving beyond the bounds of the arena
		if(this.x < 11){
			this.x = 11;
		}
		if(this.y < 11){
			this.y = 11;
		}
		if(this.y > 490){
			this.y = 490;
		}
		if(this.x > 490){
			this.x = 490;
		}
	}
	
	public void moveLeft(){
		this.x = this.x - (1 * ballSpeed);
	}
	
	public void moveRight(){
		this.x = this.x + (1 * ballSpeed);
	}

	public void moveUp(){
		this.y = this.y - (1 * ballSpeed);
	}

	public void moveDown(){
		this.y = this.y + (1 * ballSpeed);
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
      //Draw Player Square Left Side
        g.drawImage(gameBall, getX(), getY(), getBallSize(), getBallSize(), null);
    }
	
	public Rectangle getHitBox(){
		return new Rectangle(this.x, this.y, this.ballSize, this.ballSize);
	}
	
	public Image getGameBall() {
		return gameBall;
	}
	
	public void setGameBall(Image gameBall) {
		this.gameBall = gameBall;
	}
	
	public int getBallSize() {
		return ballSize;
	}

	public void setBallSize(int ballSize) {
		this.ballSize = ballSize;
	}

	public int getBallSpeed(){
		return ballSpeed;
	}
	
	public void setBallSpeed(int ballSpeed){
			this.ballSpeed = ballSpeed;
	}
}
