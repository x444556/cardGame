package net.ddns.x444556;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.event.MouseInputListener;

public class DuellPane extends JLayeredPane implements MouseInputListener {
	private static final long serialVersionUID = 630987873375576417L;
	
	public int cardWidth = 170, cardSpace = 10, cardOffsetLeft = 128;
	public int enemyCardsY = 10, playerCardsY = 280;
	public int handCardsX = 218, handCardsY = 570;
	
	public boolean allowSacrifice = true;
	
	public Card[] enemyCards = new Card[6];
	public Card[] playerCards = new Card[6];
	public Card[] playerHandCards = new Card[5];
	public ArrayList<Card> cardInventory = new ArrayList<Card>();
	public ArrayList<Card> enemyInventory = new ArrayList<Card>();
	public Card PlayerCard, EnemyCard;
	
	private Card dragged = null;
	public boolean myTurn = true;
	public boolean isOnline = false;
	
	public int playerLives = 1;
	public int enemyLives = 1;
	
	private Program game;

	public DuellPane(Program game) {
		this.game = game;
	}
	
	public void StartDuell(boolean myTurn, boolean online, Card myCard, Card enemyCard, ArrayList<Card> cards, ArrayList<Card> enemyCards) {
		this.myTurn = myTurn;
		this.isOnline = online;
		this.cardInventory = cards;
		this.enemyInventory = enemyCards;
		
		if(cards.contains(myCard)) cards.remove(myCard);
		
		PlayerCard = myCard;
		EnemyCard = enemyCard;
		
		EnemyCard.setLocation((int)(10.0 / 1200.0 * getWidth()), (int)(enemyCardsY / 840.0 * getHeight()));
		EnemyCard.Resize((int)(80.0 / 1200.0 * getWidth()));
		add(EnemyCard);
		enemyLives = EnemyCard.Health + EnemyCard.Damage;
		
		PlayerCard.setLocation((int)(10.0 / 1200.0 * getWidth()), (int)(playerCardsY / 840.0 * getHeight()));
		PlayerCard.Resize((int)(80.0 / 1200.0 * getWidth()));
		add(PlayerCard);
		playerLives = PlayerCard.Health + PlayerCard.Damage;
	}
	
	public static int index(Card card, Card[] cards) {
		for(int i=0; i<cards.length; i++) if(cards[i] == card) return i;
		return -1;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		int c0 = 130;
		int c1 = 80;
		int w = (int) (100.0 / 1200.0 * getWidth());
		double cs = (double)(c0 - c1) / getHeight();
		for(int y=0; y<getHeight(); y++) {
			g.setColor(new Color(c0 - (int)(cs*y), c0 - (int)(cs*y), c0 - (int)(cs*y)));
			g.drawLine(0, y, w, y);
		}
		
		if(EnemyCard != null && PlayerCard != null) {
			g.setColor(Color.black);
			g.fillRect((int)(20.0 / 1200.0 * getWidth()), (int)((enemyCardsY + 10.0) / 840.0 * getHeight()) + EnemyCard.getHeight(), 
					(int)(60.0 / 1200.0 * getWidth()), (int)(60.0 / 840.0 * getHeight()));
			g.setFont(new Font("SansSerif", Font.BOLD, (int)(18.0/1200.0*getWidth())));
			g.setColor(new Color(230, 230, 230));
			g.drawString(Integer.toString(enemyLives), (int)(50.0 / 1200.0 * getWidth()) - 
					(int) (g.getFont().getStringBounds(Integer.toString(enemyLives), 
					new FontRenderContext(null, true, true)).getWidth() / 2), 
					(int)((enemyCardsY + 68.0) / 840.0 * getHeight()) + EnemyCard.getHeight());
			
			g.setColor(Color.black);
			g.fillRect((int)(20.0 / 1200.0 * getWidth()), (int)((playerCardsY + 10.0) / 840.0 * getHeight()) + PlayerCard.getHeight(), 
					(int)(60.0 / 1200.0 * getWidth()), (int)(60.0 / 840.0 * getHeight()));
			g.setFont(new Font("SansSerif", Font.BOLD, (int)(18.0/1200.0*getWidth())));
			g.setColor(new Color(230, 230, 230));
			g.drawString(Integer.toString(playerLives), (int)(50.0 / 1200.0 * getWidth()) - 
					(int) (g.getFont().getStringBounds(Integer.toString(playerLives), 
					new FontRenderContext(null, true, true)).getWidth() / 2), 
					(int)((playerCardsY + 68.0) / 840.0 * getHeight()) + PlayerCard.getHeight());
		}
	}
	
	public void AddInv(Card card) {
		card.original = card.Clone();
		card.Duell = this;
		cardInventory.add(card);
	}
	public void AddEnemyInv(Card card) {
		card.original = card.Clone();
		card.Duell = this;
		enemyInventory.add(card);
	}
	public void FillHand() {
		for(int i=0; i<playerHandCards.length && cardInventory.size() > 0; i++) {
			if(playerHandCards[i] == null) {
				playerHandCards[i] = cardInventory.get(Program.randomInt(0, cardInventory.size()));
				cardInventory.remove(playerHandCards[i]);
				
				playerHandCards[i].setLocation(handCardsX + i*cardWidth + (i-1)*cardSpace, handCardsY);
				add(playerHandCards[i]);
			}
		}
		repaint();
	}
	public boolean PlayCard(Card card) {
		if(index(card, playerHandCards) == -1) return false;
		
		for(int i=0; i<playerCards.length; i++) {
			if(playerCards[i] == null) {
				card.setLocation(cardOffsetLeft + cardWidth*i + cardSpace*(i-1), playerCardsY);
				playerCards[i] = card;
				playerHandCards[index(card, playerHandCards)] = null;
				//FillHand();
				//add(card);
				repaint();
				return true;
			}
		}
		
		if(allowSacrifice) {
			playerHandCards[index(card, playerHandCards)] = null;
			remove(card);
			//FillHand();
			cardInventory.add(card);
			int dpc = card.Damage / playerCards.length;
			int hpc = card.Health / playerCards.length;
			for(int i=0; i<playerCards.length; i++) {
				// distribute attack and defense points evenly
				if(playerCards[i] != null) {
					playerCards[i].Damage += dpc;
					playerCards[i].Health += hpc;
				}
			}
			repaint();
			return true;
		}
		
		return false;
	}
	public void DestroyCard(Card card) {
		for(int i=0; i<playerCards.length; i++) {
			if(playerCards[i] == card) {
				playerCards[i] = null;
				remove(card);
				cardInventory.add(card.Reset());
			}
		}
		for(int i=0; i<enemyCards.length; i++) {
			if(enemyCards[i] == card) {
				enemyCards[i] = null;
				enemyInventory.add(card.Reset());
				remove(card);
			}
		}
		repaint();
	}
	public boolean EnemyPlayCard(Card card) {
		for(int i=0; i<enemyCards.length; i++) {
			if(enemyCards[i] == null) {
				card.setLocation(cardOffsetLeft + cardWidth*i + cardSpace*(i-1), enemyCardsY);
				enemyCards[i] = card;
				enemyInventory.remove(card);
				add(card);
				i = enemyCards.length;
				repaint();
				return true;
			}
		}
		
		if(allowSacrifice) {
			int dpc = card.Damage / enemyCards.length;
			int hpc = card.Health / enemyCards.length;
			for(int i=0; i<enemyCards.length; i++) {
				// distribute attack and defense points evenly
				if(enemyCards[i] != null) {
					enemyCards[i].Damage += dpc;
					enemyCards[i].Health += hpc;
				}
			}
			repaint();
			return true;
		}
		
		return false;
	}
	
	public void Round(boolean myTurn) {
		Card[] cards = (myTurn ? playerCards : enemyCards);
		Card[] enemy = (myTurn ? enemyCards : playerCards);
		
		for(int i=0; i<cards.length; i++) {
			if(cards[i] != null) {
				if(cards[i].roundsSurvived > 0) {
					setLayer(cards[i], 1);
					cards[i].setLocation(cards[i].getX(), cards[i].getY() + (myTurn ? -1 : 1) * (int)(60.0 / 840.0 * getHeight()));
					repaint();
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					RoundAbilities(cards[i], true);
					if(enemy[i] == null) {
						if(myTurn) enemyLives -= cards[i].Damage;
						else playerLives -= cards[i].Damage;
					}
					else if(enemy[i].Health < cards[i].Damage) {
						if(myTurn) enemyLives -= cards[i].Damage - enemy[i].Health;
						else playerLives -= cards[i].Damage - enemy[i].Health;
						DestroyCard(enemy[i]);
					}
					else if(enemy[i].Health == cards[i].Damage) {
						DestroyCard(enemy[i]);
					}
					else {
						enemy[i].Health -= cards[i].Damage;
					}
					RoundAbilities(cards[i], false);
					repaint();
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cards[i].setLocation(cards[i].getX(), cards[i].getY() - (myTurn ? -1 : 1) * (int)(60.0 / 840.0 * getHeight()));
					setLayer(cards[i], 0);
					repaint();
					if(enemyLives <= 0) game.OnDuellEnded(this, myTurn);
					else if(playerLives <= 0) game.OnDuellEnded(this, !myTurn);
				}
			}
		}
		for(int i=0; i<cards.length; i++) {
			if(cards[i] != null) cards[i].roundsSurvived++;
		}
		repaint();
	}
	private void RoundAbilities(Card card, boolean isBeforeAttack) {
		
	}
	private Card Bot() {
		Card card = null;
		switch(Program.randomInt(0, 2)) {
			case(0):
				for(int i=0; i<enemyInventory.size(); i++) {
					if(enemyInventory.get(i) != null) {
						if(card == null || card.Health + card.Damage < enemyInventory.get(i).Health + enemyInventory.get(i).Damage)
							card = enemyInventory.get(i);
					}
				}
			case(1):
				card = enemyInventory.get(Program.randomInt(0, enemyInventory.size()));
		}
		return card;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getX() >= 0 && e.getY() >= 0 && e.getX() < getWidth() && e.getY() < getHeight()) {
			for(Card c : playerHandCards) {
				if(c != null && c.contains(e.getX(), e.getY())) {
					dragged = c;
					setLayer(dragged, 1);
					break;
				}
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(dragged != null) {
			setLayer(dragged, 0);
			
			if(myTurn && dragged.getY() <= handCardsY - dragged.getHeight() / 2) {
				Card dragged2 = dragged;
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						if(PlayCard(dragged2)) {
							myTurn = false;
							repaint();
							// Card has been played
							Round(true);
							repaint();
							if(!isOnline) {
								// Bot
								try {
									Thread.sleep(500);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								EnemyPlayCard(Bot());//enemyInventory.get(Program.randomInt(0, enemyInventory.size())));
								try {
									Thread.sleep(500);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
					            Round(false);
					            myTurn = true;
							}
							else {
								// Online
							}
							FillHand();
						}
						else {
							// Card couldn't be played!
							int i = DuellPane.index(dragged2, playerHandCards);
							dragged.setLocation(handCardsX + i*cardWidth + (i-1)*cardSpace, handCardsY);
						}
					}
					
				});
				t.start();
			}
			else {
				int i = DuellPane.index(dragged, playerHandCards);
				dragged.setLocation(handCardsX + i*cardWidth + (i-1)*cardSpace, handCardsY);
			}

			dragged = null;
		}
	}
	int lastMouseX, lastMouseY;
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		if(dragged != null) dragged.setLocation(dragged.getX() +  e.getX() - lastMouseX, dragged.getY() + e.getY() - lastMouseY);
		
		lastMouseX = e.getX();
		lastMouseY = e.getY();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
