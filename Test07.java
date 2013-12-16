import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.callbacks.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

import fr.atis_lab.physicalworld.*;

/*
 * javac -cp ./lib/*:. TestXX.java
 *
 * java -cp ./lib/*:. TestXX
 */

public class Test07 {

	private MyGame myGame;
    public Test07()  {
    
	try {
		myGame = Serializer.loadFromFile("MyGame.bak");
	} catch(FileNotFoundException ex) {
		// Do nothing
	} catch(ClassNotFoundException ex) {
		ex.printStackTrace();
	}catch(IOException ex) {
		ex.printStackTrace();
	}
	if(myGame == null) {
		myGame = new MyGame();
	}
    }
    
    public void run() {
    	myGame.run();
    try {
     Serializer.saveToFile("MyGame.bak",myGame);
    	}catch(IOException ex) {
		ex.printStackTrace();
	}

    	
    }


    public static void main(String[] args) {
        new Test07().run();
    }
}
