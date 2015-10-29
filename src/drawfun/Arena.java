package drawfun;

import java.awt.Graphics;
import javax.swing.JPanel;

//260, 260 is center of arena
@SuppressWarnings("serial")
public class Arena extends JPanel{
	int playerScore = 0;
	int enemyScore = 0;
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
        //Scoreboard
        g.drawString("Player Score = " + playerScore, 100, 530);
        g.drawString("Enemy Score = " + enemyScore, 320, 530);
        //Half Field Line
    	g.drawLine(260, 10, 260, 510);
    	drawGoals(g);
        //Game Map
        g.drawRect(10, 10, 500, 500);
    }
	
	public void drawGoals(Graphics g){
		g.drawLine(400, 10, 510, 120);
		g.drawString("P2", 490, 25);
		g.drawLine(10, 400, 120, 510);
		g.drawString("P1", 15, 500);
		g.drawLine(10, 120, 120, 10);
		g.drawString("P1", 15, 25);
		g.drawLine(400, 510, 510, 400);
		g.drawString("P2", 490, 500);
	}
}
