package com.max.javagame.graphics;

import java.awt.Color;
import java.awt.Graphics;

import com.max.javagame.input.Mouse;

public class Button{
	private int x, y;
	private int width, height;
	private Color color;
	private boolean isClicked = false;
	private boolean isDestroyed = false;
	
	public Button(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		isClicked = false;
		isDestroyed = false;
	}
	
	public void update() {
		isClicked = false;
		if(!isDestroyed) {
			if(Mouse.getB()==1 && Mouse.getX() > x && Mouse.getX() < x + width  &&  Mouse.getY() > y && Mouse.getY() < y + height) {
				isClicked = true;
			}else {
				isClicked = false;
			}
		}else {
			isClicked = false;
		}
	}
	public void render(Graphics g) {
		if(!isDestroyed) {
			Color oldColor = g.getColor();
			g.setColor(color);
			g.fillRect(x, y, width, height);
			g.setColor(oldColor);
		}
		
	}
	public void setDestroyed(boolean isDestroyed) {
		if(this.isDestroyed != isDestroyed) {
			this.isDestroyed = isDestroyed;
		}
	}
	public boolean isClicked() {
		return isClicked;
	}



}
