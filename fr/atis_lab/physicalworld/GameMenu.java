public class GameMenu implements ActionListener {

import javax.swing.* ;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GameMenu implements ActionListener{

	JPanel menu;
	JButton newGame;
	JButton loadGame;
	
	public BlackJackGUI() {
		JFrame frame = new JFrame("Menu");
		frame.setPreferredSize(new Dimension(100,200));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
		frame.setLayout(new BorderLayout());
		
			JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			newGame = new JButton("New game");
			newGame.setActionCommand("newgame");
			newGame.addActionListener(this);
			
			loadGame = new JButton("Load game");
			loadGame.setActionCommand("loadgame");
			loadGame.addActionListener(this);
			
			topPanel.add(newGame);
			topPanel.add(loadGame);

			
		frame.add(topPanel, BorderLayout.NORTH);
		
			JPanel centerPanel = new JPanel(new GridLayout(2,1));
			playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			bankPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			playerPanel.setBorder(BorderFactory.createTitledBorder("Your hand"));
			bankPanel.setBorder(BorderFactory.createTitledBorder("The bank"));
			
		centerPanel.add(bankPanel);
		centerPanel.add(playerPanel);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.setVisible(true);
		
		
		
		bjc = new BlackJackConsole();
		this.updatePanels();
		
	}
	
	public void actionPerformed(ActionEvent e){
		
		

		switch(e.getActionCommand()){
			case "loadgame":
			
				break;
		
			case "newgame":
			
				break;

		}

	
		
		this.updatePanels();/*
		this.anotherCardButton.setEnabled(! bjc.isGameFinished());
		this.noMoreCardsButton.setEnabled(! bjc.isGameFinished());*/
			
		
	}
	
	public static void main(String[] args) {
		//new BlackJackGUI();
	}
