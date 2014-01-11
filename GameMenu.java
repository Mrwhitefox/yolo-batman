
import javax.swing.* ;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GameMenu implements ActionListener{

	JPanel menu;
	JButton newGame;
	JButton loadGame;
	
	public GameMenu() {
		JFrame frame = new JFrame("Menu");
		frame.setPreferredSize(new Dimension(200,150));
		frame.setMinimumSize(new Dimension(200,150));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
		frame.setLayout(new GridLayout(2,1));
		
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

			
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(centerPanel, BorderLayout.CENTER);
		
			
			

		frame.setVisible(true);
		
		
	
	
		
	}
	
	public void actionPerformed(ActionEvent e){
		
		

		switch(e.getActionCommand()){
			case "loadgame":
			
				break;
		
			case "newgame":
			
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
