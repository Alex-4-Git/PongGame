package Pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.nio.channels.ServerSocketChannel;
import java.util.Random;

import javax.activation.UnsupportedDataTypeException;

public class Ball {
	
	public int x, y, width = 25, height = 25;
	
	int motionX, motingY;
	
	public Random random;
	
	public Ball(Pong pong){
		random = new Random();
		this.x = pong.width/2 -this.width/2;
		this.y = pong.height/2 - this.height/2;
	}
	
	public void update(Paddle paddle1, Paddle paddle2){
		if(checkCollision(paddle1) == 1){
			this.motionX = 1;
			this.motingY = -2 + random.nextInt(4);
		}
		
		if(checkCollision(paddle2) == 1){
			this.motionX = -1;
			this.motingY = -2 + random.nextInt(4);
		}
		
		if(checkCollision(paddle1) == 2){
			paddle2.score++;
		}
		
		if(checkCollision(paddle2) == 2){
			paddle1.score++;
		}
	}
	
	public int checkCollision(Paddle paddle){
		if(paddle.x > x || paddle.x < x + width){
			if(paddle.y > y + height || paddle.y + height < y){
				return 1;  //hit
			}else{
				return 2;  //wall
			}
		}
 		return 0; //nothing
	}
	
	public void render(Graphics g){
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
	}
}
