package drawfun;

import java.awt.Graphics;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class PlayerSquare extends GameObject{
	int length;
	
	PlayerSquare(){
		this.x = 10;
		this.y = 245;
		this.length = 30;
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draw Player Square Left Side
        g.drawRect(this.getX(), this.getY(), this.getLength(), this.getLength());
    }
	
	public Rectangle getHitBox(){
		return new Rectangle(this.x, this.y, this.length, this.length);
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
