package Pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.nio.channels.ServerSocketChannel;
import java.util.Random;

import javax.activation.UnsupportedDataTypeException;

public class Ball {
	
	public int x, y, width = 25, height = 25 ;
	
	int motionX, motionY;
	
	public Random random;
	
	private Pong pong;
	
	private int amountOfHits;
	
	public Ball(Pong pong){
		this.pong = pong;
		random = new Random();
		spawn();
	}
	
	public void update(Paddle paddle1, Paddle paddle2){
		int speed = 2;
		this.x += motionX*speed;
		this.y += motionY*speed;
		
		if(this.y <= 0 || this.y + height >= pong.height){
			if(motionY<0){
				this.y = 0;
				motionY = random.nextInt(4) + 1;
			}else{
				this.y = pong.height-height;
				motionY = -random.nextInt(4) -1;
			}
		}
		
		else if(checkCollision(paddle1) == 1){
			this.motionX = 1 + (amountOfHits/5);
			this.motionY = -2 + random.nextInt(4);
			while(this.motionY == 0){
				this.motionY = -2 + random.nextInt(4);
			}
			amountOfHits ++;
		}
		
		else if(checkCollision(paddle2) == 1){
			this.motionX = -1 - (amountOfHits/5);
			this.motionY = -2 + random.nextInt(4);
			while(this.motionY == 0){
				this.motionY = -2 + random.nextInt(4);
			}
			amountOfHits ++;
		}
		
		else if(checkCollision(paddle1) == 2){
			paddle2.score++;
			spawn();
			pong.gameStatus = 1;
		}
		 
		else if(checkCollision(paddle2) == 2){
			paddle1.score++;
			spawn();
			pong.gameStatus = 1;
		}
	}
	
	public void spawn(){
		amountOfHits = 0;
		this.x = pong.width/2 -this.width/2;
		this.y = pong.height/2 - this.height/2;
		

		this.motionY = -2 + random.nextInt(4);
		
		if(random.nextBoolean()){
			motionX = 1;
		}else{
			motionX = -1;
		}
	}
	
	public int checkCollision(Paddle paddle){
		if(this.x < paddle.x + paddle.width && this.x+ width > paddle.x && this.y < paddle.y + paddle.height && this.y+height>paddle.y){
			return 1; // hit
		}
		
//		else if(paddle.x <  x && paddle.paddleNumber == 2 || paddle.x > x + width && paddle.paddleNumber == 1){
//			return 2; // score
//		}
		else if(x <= 0 && paddle.paddleNumber == 1 || x+width>=pong.width && paddle.paddleNumber == 2){
			return 2;
		}
 		return 0; //nothing
	}
	
	public void render(Graphics g){
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
	}
}
