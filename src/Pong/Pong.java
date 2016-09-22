package Pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.omg.CORBA.BAD_OPERATION;


public class Pong implements ActionListener, KeyListener{
	
	public static Pong pong;
	
	public int width = 700, height = 700;
	
	public Renderer renderer;
	
	public Paddle player1;
	
	public Paddle player2;
	
	public Ball ball;
	
	public boolean bot = false, selectingDifficulty = false;
	
	public boolean w,s,up,down;
	
	public int gameStatus = 0; // 0 = Stopped, 1 = Paused, 2 = Playing
	
	public Random random;
	
	public int botMoves=0, cooldown = 0, botDifficulty = 1;
	
	public int scoreLimit = 7;
	
	public String winner = "";
	
	public Pong() {
		
		random = new Random();
		Timer timer = new Timer(20, this);
		JFrame jframe = new JFrame("Pong");
		
		renderer = new Renderer();
		
		jframe.setSize(width + 15,  height + 35);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(renderer);
		jframe.addKeyListener(this);
		
		
		timer.start();
	}
	
	public void start(){
		gameStatus = 2;
		player1 = new Paddle(this, 1);
		player2 = new Paddle(this, 2);
		ball = new Ball(this);
	}
	
	private void update() {
		// TODO Auto-generated method stub
		
		if(player1.score == scoreLimit){
			gameStatus = 3;
		}
		
		if(player2.score == scoreLimit){
			gameStatus = 3;
		}
		
		if(w){
			player1.moveUp(true);
		}
		if(s){
			player1.moveUp(false);
		}
		
		if(!bot){
			if(up){
				player2.moveUp(true);
			}
			if(down){
				player2.moveUp(false);
			}
		}else{
			if(cooldown > 0){
				cooldown --;
				if(cooldown == 0){
					botMoves = 0;
				}
			}
			
			if(botMoves < 10)
			{
				if(player2.y + player2.height/2 < ball.y)
				{
					player2.moveUp(false);
				}
				else if(player2.y + player2.height/2 > ball.y)
				{
					player2.moveUp(true);
				}	
				
				if(botDifficulty == 0){
					cooldown = 30;
				}else if(botDifficulty == 1){
					cooldown = 20;
				}else if(botDifficulty == 2){
					cooldown = 10;
				}
			}
			
		}
		
		
		
		ball.update(player1, player2);
	}
	
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(gameStatus == 0){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("PONG", width/2 - 75, 50);
			
			if(!selectingDifficulty){
				
				g.setFont(new Font("Arial", 1, 30));
				
				g.drawString("Press Space to Play", width/2 - 150, height/2 - 25);
				g.drawString("Press Shift to Play with Bot", width/2 - 200, height/2 + 25);
				g.drawString("<< Score Limit: " + scoreLimit + " >>", width/2 - 150, height/2 + 75);
					
			}
		}
		
		if(selectingDifficulty)
		{
			String difficulty = botDifficulty == 0 ? "Easy": botDifficulty == 1 ? "Medium" : "Hard";
			g.setFont(new Font("Arial", 1, 30));
			g.drawString("Bot Difficulty : " + difficulty, width/2 - 175, height/2 - 25);
			g.drawString("Press Space to Play", width/2 - 150, height/2 + 25);
		}
		
		if(gameStatus == 2 || gameStatus == 1){
			g.setColor(Color.WHITE);
			g.setStroke(new BasicStroke((float)2.5));
			g.drawLine(width/2, 0, width/2, height);
			
			g.setStroke(new BasicStroke(5f));
			g.drawOval(height/2 - 100, height/2 - 100, 200, 200);
			
			
			g.setFont(new Font("Arial", 1, 50));
			g.drawString(String.valueOf(player1.score), width/2 - 95, 50);
			g.drawString(String.valueOf(player2.score), width/2 + 65, 50);
			
			player1.render(g);
			player2.render(g);
			ball.render(g);
		}
		
		
		if(gameStatus == 1){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("PAUSED", width/2 - 103, height/2 - 105);
		}
		
		if(gameStatus == 3){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("PONG", width/2 - 75, 50);
			
				
			g.setFont(new Font("Arial", 1, 30));
			
			g.drawString("Press Space to Play Again", width/2 - 180, height/2 - 25);
			g.drawString("Press ESC for menu", width/2 - 135, height/2 + 25);
		
		}
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(gameStatus == 2){
			update();
		}
		
		renderer.repaint();
	}



	public static void main(String[] args) {
		pong = new Pong();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int id = e.getKeyCode();
		
		if(id == KeyEvent.VK_W){
			w = true;
		}
		else if(id == KeyEvent.VK_S){
			s = true;
		}
		else if(id == KeyEvent.VK_UP){
			up = true;
		}
		else if(id == KeyEvent.VK_DOWN){
			down = true;
		}
		else if(id == KeyEvent.VK_RIGHT){
			if(selectingDifficulty){
				botDifficulty = ++botDifficulty%3;
			}else if(gameStatus == 0 && scoreLimit < 50){
				scoreLimit ++;
			}
		}
		else if(id == KeyEvent.VK_LEFT ){
			if(selectingDifficulty){
				botDifficulty = (--botDifficulty+3)%3;
			}
			else if(gameStatus == 0 && scoreLimit > 1){
				scoreLimit --;
			}
		}
		else if(id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3)){
			gameStatus = 0;
			selectingDifficulty =false;
			bot = false;
		}
		else if( id== KeyEvent.VK_SHIFT && gameStatus == 0){
			bot = true;
			selectingDifficulty = true;
		}
		else if(id == KeyEvent.VK_SPACE){
			if(gameStatus == 0 || gameStatus == 3){
				start();
			}else if(gameStatus == 1){
				gameStatus = 2;
			}else if(gameStatus == 2){
				gameStatus = 1;
			}
		}
	}
	

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int id = e.getKeyCode();
		if(id == KeyEvent.VK_W){
			w = false;
		}
		if(id == KeyEvent.VK_S){
			s = false;
		}
		if(id == KeyEvent.VK_UP){
			up = false;
		}
		if(id == KeyEvent.VK_DOWN){
			down = false;
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	
}
