package com.max.javagame.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{
	public boolean keys[] = new boolean[525];
	public boolean startListening1 = false;
	public boolean startListening2 = false;
	public char[] chars1 = new char[10];
	public char[] chars2 = new char[10];
	public int charIndex1 = 0;
	public int charIndex2 = 0;
	public KeyEvent keyTyped;
	
	public Keyboard() {
		for(int i = 0; i< chars1.length;i++) {
			chars1[i] = 0;
			chars2[i] = 0;
		}
	}
	
	public void update() {


	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}

	public void keyTyped(KeyEvent e) {
		
		if(startListening1) {
			if(((int)e.getKeyChar()) == 8) {
				if((charIndex1 > 0)) {
					if(charIndex1 < chars1.length) {
						chars1[charIndex1] = 0;
					}
					charIndex1--;
					chars1[charIndex1] = 95;
				}
			} else if(!(charIndex1 >= chars1.length)) {
				chars1[charIndex1] = e.getKeyChar();
				charIndex1++;
				if(!(charIndex1 >= chars1.length)) {
					chars1[charIndex1] = 95;
				}
				
			}
		}else if(startListening2) {

			if(((int)e.getKeyChar()) == 8) {
				if((charIndex2 > 0)) {
					if(charIndex2 < chars1.length) {
						chars1[charIndex2] = 0;
					}
					charIndex2--;
					chars2[charIndex2] = 95;
				}
			} else if(!(charIndex2 >= chars2.length)) {
				chars2[charIndex2] = e.getKeyChar();
				charIndex2++;
				if(!(charIndex2 >= chars2.length)) {
					chars2[charIndex2] = 95;
				}
			}
		}
	}
	public void resetTyped() {
		startListening1 = false;
		for(int i = 0; i< chars1.length;i++) {
			chars1[i] = 0;
		}
		charIndex1 = 0;
		startListening2 = false;
		for(int i = 0; i< chars2.length;i++) {
			chars2[i] = 0;
		}
		charIndex2 = 0;
	}

}
