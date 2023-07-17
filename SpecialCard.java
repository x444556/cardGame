package net.ddns.x444556;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.util.Timer;
import java.util.TimerTask;


public class SpecialCard extends Card{
	private static final long serialVersionUID = 777930635735238405L;
	
	private int minAlpha = 100, maxAlpha = 200, alphaStep = 4, alpha = 100;
	
	public SpecialCard(String name, int health, int damage) {
		super(null, null, name, health, damage);
		this.hidebg = true;
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(alpha <= minAlpha) {
					alpha = minAlpha;
					alphaStep *= -1;
				}
				else if(alpha >= maxAlpha) {
					alpha = maxAlpha;
					alphaStep *= -1;
				}
				repaint();
				alpha += alphaStep;
			}
		}, 0, 50);
	}
	public SpecialCard(Color bgColorStart, Color bgColorEnd, String name, int health, int damage) {
		super(bgColorStart, bgColorEnd, name, health, damage);
		this.hidebg = true;
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(alpha <= minAlpha) {
					alpha = minAlpha;
					alphaStep *= -1;
				}
				else if(alpha >= maxAlpha) {
					alpha = maxAlpha;
					alphaStep *= -1;
				}
				repaint();
				alpha += alphaStep;
			}
		}, 0, 50);
	}

	@Override
	public void paintComponent(Graphics g) {

		for(int y=0; y<getHeight(); y++) {
			for(int x=0; x<getWidth(); x++) {
				g.setColor(new Color((int)(255.0 * distNorm(0, 0, x, y)), (int)(255.0 * distNorm(getWidth(), 0, x, y)), 
						(int)(255.0 * distNorm(0, getHeight(), x, y)), alpha));
				g.drawLine(x, y, x, y);
			}
		}
		
		// Name
		g.setFont(new Font("SansSerif", Font.BOLD, (int)(20.0/refX*getWidth())));
		g.setColor(new Color(230, 230, 230));
		g.drawString(Name, (int)(6.0/refX*getWidth()), (int)(28.0/refX*getWidth()));
		
		// Image
		g.setColor(Color.black);
		g.fillRect((int)(6.0/refX*getWidth()), (int)(35.0/refX*getWidth()), getWidth() - (int)(12.0/refX*getWidth()), getWidth() - 
				(int)(12.0/refX*getWidth()));
		
		// Attack
		g.setColor(Color.black);
		g.fillRect((int)(6.0/refX*getWidth()), (int)(42.0/refX*getWidth()) + getWidth() - (int)(12.0/refX*getWidth()), 
				(getWidth() - (int)(12.0/refX*getWidth())) / 3, (getWidth() - (int)(12.0/refX*getWidth())) / 3);
		g.setFont(new Font("SansSerif", Font.BOLD, (int)(16.0/refX*getWidth())));
		g.setColor(new Color(230, 230, 230));
		g.drawString(Integer.toString(Damage), (int)(6.0/refX*getWidth()) + (getWidth() - (int)(12.0/refX*getWidth())) / 6 - 
				(int) (g.getFont().getStringBounds(Integer.toString(Damage), 
				new FontRenderContext(null, true, true)).getWidth() / 2), (int)(40.0/refX*getWidth()) + getWidth() - 
				(int)(12.0/refX*getWidth()) + (getWidth() - (int)(12.0/refX*getWidth())) / 3);
		
		// Defense
		g.setColor(Color.black);
		g.fillRect((int)(13.0/refX*getWidth()) + (getWidth() - (int)(12.0/refX*getWidth())) / 3, (int)(42.0/refX*getWidth()) + getWidth() - 
				(int)(12.0/refX*getWidth()) , (getWidth() - (int)(12.0/refX*getWidth())) / 3, (getWidth() - (int)(12.0/refX*getWidth())) / 3);
		g.setFont(new Font("SansSerif", Font.BOLD, (int)(16.0/refX*getWidth())));
		g.setColor(new Color(230, 230, 230));
		g.drawString(Integer.toString(Health), (int)(13.0/refX*getWidth()) + (getWidth() - (int)(12.0/refX*getWidth())) / 3 + 
				(getWidth() - (int)(12.0/refX*getWidth())) / 6 - 
				(int) (g.getFont().getStringBounds(Integer.toString(Health), 
				new FontRenderContext(null, true, true)).getWidth() / 2), (int)(40.0/refX*getWidth()) + getWidth() - 
				(int)(12.0/refX*getWidth()) + (getWidth() - (int)(12.0/refX*getWidth())) / 3);
		
		// Abilities
		for(int i=0; i<6; i++) {
			g.setColor(Color.black);
			g.fillRect((int)(20.0/refX*getWidth()) + (getWidth() - (int)(12.0/refX*getWidth())) / 3 * 2 + (((getWidth() - 
					(int)(12.0/refX*getWidth())) / 9 + (int)(3.0/refX*getWidth())) * (i / 3)), 
					(int)(42.0/refX*getWidth()) + getWidth() - (int)(12.0/refX*getWidth()) + (((getWidth() - (int)(12.0/refX*getWidth())) / 
					9 + (int)(2.0/refX*getWidth())) * (i % 3)), 
					(getWidth() - (int)(12.0/refX*getWidth())) / 9, (getWidth() - (int)(12.0/refX*getWidth())) / 9);
		}
		
		if(Duell != null && ((!Duell.myTurn && DuellPane.index(this, Duell.playerHandCards) != -1) || 
				((DuellPane.index(this, Duell.playerCards) != -1 || DuellPane.index(this, Duell.enemyCards) != -1) && roundsSurvived == 0))) {
			g.setColor(new Color(0, 0, 0, 90));
			for(int y=0; y<getHeight(); y++) {
				g.drawLine(0, y, getWidth(), y);
			}
		}
	}
	
	private double distance(int x0, int y0, int x1, int y1) {
		return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
	}
	private double distNorm(int x0, int y0, int x1, int y1) {
		return distance(x0, y0, x1, y1) / distance(0, 0, getWidth(), getHeight());
	}
}
