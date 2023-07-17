package net.ddns.x444556;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ComponentListener;

public class Program extends JFrame implements Runnable, KeyListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	private Thread thread;
	public int windowX = 1200, windowY = 840;
	
	public Program() {
		thread = new Thread(this);
		
		setSize(windowX, windowY);
		setResizable(true);
		setTitle("Card Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setPreferredSize(new Dimension(windowX, windowY));
		getContentPane().setBackground(Color.lightGray);
		setLocationRelativeTo(null);
		setLayout(null);
		
		pack();
		setVisible(true);
		
		addKeyListener(this);
		addComponentListener(this);
	}
	private synchronized void start() {
		thread.start();

		DuellPane duellPane = new DuellPane(this);
		duellPane.setSize(getContentPane().getWidth(), getContentPane().getHeight());
		getContentPane().add(duellPane);
		
		Card card0 = Card.Common(		"Card Common", 		10, 12);
		card0.Resize(80);
		duellPane.AddInv(card0);
		duellPane.AddInv(Card.Uncommon	("Card Uncommon", 	10, 12));
		duellPane.AddInv(Card.Rare		("Card Rare", 		10, 12));
		duellPane.AddInv(Card.Epic		("Card Epic", 		10, 12));
		duellPane.AddInv(Card.Legendary	("Card Legendary", 	10, 12));
		duellPane.AddInv(Card.Unique	("Card Unique", 	10, 12));
		duellPane.AddInv(Card.Uncommon	("Hand #0", 		 3,	 4));
		duellPane.AddInv(Card.Common	("Hand #1", 		 2,	 1));
		duellPane.AddInv(Card.Rare		("Hand #2", 		 6,	 5));
		duellPane.AddInv(Card.Legendary	("Hand #3", 		18, 11));
		duellPane.AddInv(Card.Uncommon	("Hand #4", 		 6,	 3));
		duellPane.AddInv(Card.Uncommon	("Hand #5", 		 7,	 2));
		duellPane.AddInv(new SpecialCard("Special", 80, 24));
		duellPane.AddInv(new SpecialCard2("Special #2", 80, 24));
		duellPane.FillHand();
		
		for(Card c : duellPane.cardInventory) {
			duellPane.AddEnemyInv(c.Clone());
		}
		
		StartDuell(duellPane, Card.Epic("Test #0", 18, 12), Card.Rare("Test #1", 17, 14));
	}
	public synchronized void stop() {
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static int randomInt(int min, int max) {
		return (int)(Math.random() * (max - min) + min);
	}
	
	public static void main(String [] args) {
		Program game = new Program();		
		game.start();
	}
	
	public void StartDuell(DuellPane duellPane, Card playerCard, Card enemyCard) {
		addMouseListener(duellPane);
		addMouseMotionListener(duellPane);
		add(duellPane);
		duellPane.StartDuell(true, false, playerCard, enemyCard, duellPane.cardInventory, duellPane.enemyInventory);
	}
	public void OnDuellEnded(DuellPane duellPane, boolean won) {
		removeMouseListener(duellPane);
		removeMouseMotionListener(duellPane);
		remove(duellPane);
		Thread.currentThread().interrupt();
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			
		}

		else if(e.getKeyCode() == KeyEvent.VK_UP) {

		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {

		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	@Override
	public void run() {
		
	}
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}
