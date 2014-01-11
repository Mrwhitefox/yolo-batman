
import javax.swing.* ;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GameMenu implements ActionListener{

	private JPanel menu;
	private JButton newGame;
	private JButton loadGame;
	private JFrame menuFrame;
	
	public GameMenu() {
		menuFrame = new JFrame("Menu");
		menuFrame.setPreferredSize(new Dimension(200,150));
		menuFrame.setMinimumSize(new Dimension(200,150));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuFrame.pack();
		
		menuFrame.setLayout(new GridLayout(2,1));
		
			JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			
			newGame = new JButton("New game");
			newGame.setActionCommand("newgame");
			newGame.addActionListener(this);
			
			loadGame = new JButton("Load game");
			loadGame.setActionCommand("loadgame");
			loadGame.addActionListener(this);
			
			topPanel.add(newGame);
			centerPanel.add(loadGame);

			
		menuFrame.add(topPanel, BorderLayout.NORTH);
		menuFrame.add(centerPanel, BorderLayout.CENTER);
		
			
			

		menuFrame.setVisible(true);
		
		
	
	
		
	}
	
	public void actionPerformed(ActionEvent e){
		
		

		switch(e.getActionCommand()){
			case "loadgame":
				break;
		
			case "newgame":
				menuFrame.setVisible(false);
				MyGame game = new MyGame();				
				game.run();
				break;

		}

	
		
		/*
		this.anotherCardButton.setEnabled(! bjc.isGameFinished());
		this.noMoreCardsButton.setEnabled(! bjc.isGameFinished());*/
			
		
	}
	
	public static void main(String[] args) {
		new GameMenu();
	}
	
}
