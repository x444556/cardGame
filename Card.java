package net.ddns.x444556;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.Font;
import javax.swing.JPanel;

public class Card extends JPanel {
	private static final long serialVersionUID = -4890117439504466739L;
	
	public Color bgColorStart, bgColorEnd;
	public String Name;
	public int Health, Damage;

	public Card original;
	public DuellPane Duell;
	
	public int roundsSurvived = 0;
	
	protected final int refX = 170, refY = 260;
	
	protected boolean hidebg = false;
	
	public Card(Color bgColorStart, Color bgColorEnd, String name, int health, int damage) {
		this.bgColorStart = bgColorStart;
		this.bgColorEnd = bgColorEnd;
		this.Name = name;
		this.Health = health;
		this.Damage = damage;
		
		this.setSize(refX, refY);
		this.setOpaque(false);
	}
	
	public static Card Common(String name, int health, int damage) {
		return new Card(new Color(150, 150, 150), new Color(80, 80, 80), name, health, damage);
	}
	public static Card Uncommon(String name, int health, int damage) {
		return new Card(new Color(0, 180, 0), new Color(0, 120, 0), name, health, damage);
	}
	public static Card Rare(String name, int health, int damage) {
		return new Card(new Color(180, 0, 0), new Color(100, 0, 0), name, health, damage);
	}
	public static Card Epic(String name, int health, int damage) {
		return new Card(new Color(0, 0, 190), new Color(0, 0, 120), name, health, damage);
	}
	public static Card Legendary(String name, int health, int damage) {
		return new Card(new Color(220, 220, 0), new Color(130, 130, 0), name, health, damage);
	}
	public static Card Unique(String name, int health, int damage) {
		return new Card(new Color(210, 0, 210), new Color(0, 130, 130), name, health, damage);
	}
	
	public boolean contains(int x, int y) {
		return x >= getX() && x < getX() + getWidth() && y >= getY() && y < getY() + getHeight();
	}

	public void Resize(int width) {
		this.setSize(width, (int)(1.0 * width / refX * refY));
	}
	
	public Card Clone() {
		Card c = new Card(bgColorStart, bgColorEnd, Name, Health, Damage);
		c.hidebg = hidebg;
		return c;
	}
	public Card Reset() {
		if(original != null) {
			Health = original.Health;
			Damage = original.Damage;
		}
		roundsSurvived = 0;
		return this;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if(!hidebg) {
			double cr = (bgColorStart.getRed() - bgColorEnd.getRed()) * 1.0 / getHeight();
			double cg = (bgColorStart.getGreen() - bgColorEnd.getGreen()) * 1.0 / getHeight();
			double cb = (bgColorStart.getBlue() - bgColorEnd.getBlue()) * 1.0 / getHeight();
			
			for(int y=0; y<getHeight(); y++) {
				g.setColor(new Color(bgColorStart.getRed() - (int)(y*cr), bgColorStart.getGreen() - (int)(y*cg), 
						bgColorStart.getBlue() - (int)(y*cb)));
				g.drawLine((int)(y < 12.0/refX*getWidth() ? 12.0/refX*getWidth() - y*y : (y > getHeight() - 12.0/refX*getWidth() ? 
						12.0/refX*getWidth() - (getHeight() - y)*(getHeight() - y) : 0)), y, 
						getWidth() - (int)(y < 12.0/refX*getWidth() ? 12.0/refX*getWidth() - y*y : (y > getHeight() - 12.0/refX*getWidth() ? 
						12.0/refX*getWidth() - (getHeight() - y)*(getHeight() - y) : 0)), y);
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
				g.drawLine((int)(y < 12.0/refX*getWidth() ? 12.0/refX*getWidth() - y*y : (y > getHeight() - 12.0/refX*getWidth() ? 
						12.0/refX*getWidth() - (getHeight() - y)*(getHeight() - y) : 0)), y, 
						getWidth() - (int)(y < 12.0/refX*getWidth() ? 12.0/refX*getWidth() - y*y : (y > getHeight() - 12.0/refX*getWidth() ? 
						12.0/refX*getWidth() - (getHeight() - y)*(getHeight() - y) : 0)), y);
			}
		}
	}
}
