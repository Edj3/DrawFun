package drawfun;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

@SuppressWarnings("serial")
public class DrawFunMain extends JApplet{
	PlayerSquare p1;
	EnemySquare e1;
	GameBall ball;
	Arena arena;
	LinkedHashSet<Integer> listOfKeys;
	boolean touchHeld = false;
	long lastProcessed = 0;
	boolean movingLeft = false;
	boolean movingRight = false;
	boolean movingUp = false;
	boolean movingDown = false;
	boolean playerScored = false;
	boolean enemyScored = false;
	
	public class Game extends JPanel{
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        arena.paintComponent(g);
	        ball.paintComponent(g);
	        p1.paintComponent(g);
	        e1.paintComponent(g);   
	    }
	}
	
	public void init(){
		p1 = new PlayerSquare();
		e1 = new EnemySquare();
		arena = new Arena();
		listOfKeys = new LinkedHashSet<Integer>();
		GUIManager gui = new GUIManager();
		try {
			ball = new GameBall();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Initting...");
		gui.displayTitleScreen();
		startGame();
		kickOff();
	}
	
	public void kickOff(){
		movingLeft = false;
		movingRight = false;
		movingUp = false;
		movingDown = false;
		final double randDirectionX = Math.random();
		final double randDirectionY = Math.random();
		Timer kickOffTimer = new Timer();
		kickOffTimer.schedule(new TimerTask(){
			boolean notMovingX = false;
			boolean notMovingY = false;
			@Override
			public void run(){
				//Set Boolean for when boundary is hit
				if(ball.x <= 11){
					ball.moveRight();
					movingRight = true;
					movingLeft = false;
				}
				if(ball.x >= 489){
					ball.moveLeft();
					movingLeft = true;
					movingRight = false;
				}
				if(ball.y <= 11){
					ball.moveDown();
					movingDown = true;
					movingUp = false;
				}
				if(ball.y >= 489){
					ball.moveUp();
					movingUp = true;
					movingDown = false;
				}
				//Handle mid wall bounce by sending ball in random direction
				if(ball.x == 250 && ball.y <= 15){
					ball.moveDown();
					movingDown = true;
					movingUp = false;
					if(Math.random() > 0.5){
						ball.moveRight();
						movingRight = true;
						movingLeft = false;
					}else{
						ball.moveLeft();
						movingRight = false;
						movingLeft = true;
					}
				}
				if(ball.x == 250 && ball.y >= 485){
					ball.moveUp();
					movingDown = false;
					movingUp = true;
					if(Math.random() > 0.5){
						ball.moveRight();
						movingRight = true;
						movingLeft = false;
					}else{
						ball.moveLeft();
						movingRight = false;
						movingLeft = true;
					}
				}
				if(ball.x <= 15 && ball.y == 250){
					ball.moveRight();
					movingRight = true;
					movingLeft = false;
					if(Math.random() > 0.5){
						ball.moveUp();
						movingUp = true;
						movingDown = false;
					}else{
						ball.moveDown();
						movingUp = false;
						movingDown = true;
					}
				}
				if(ball.x >= 485 && ball.y == 250){
					ball.moveLeft();
					movingLeft = true;
					movingRight = false;
					if(Math.random() > 0.5){
						ball.moveUp();
						movingUp = true;
						movingDown = false;
					}else{
						ball.moveDown();
						movingUp = false;
						movingDown = true;
					}
				}
				
				//move in the directions set by the boolean flow above
				if(movingLeft || movingRight || movingUp || movingDown){
					if(movingLeft){
						ball.moveLeft();
					}
					if(movingRight){
						ball.moveRight();
					}
					if(movingUp){
						ball.moveUp();
					}
					if(movingDown){
						ball.moveDown();
					}
				}else{
					//Random Direction for x position
					if(randDirectionX < 0.33){
						ball.moveLeft();
						movingLeft = true;
						movingRight = false;
						notMovingX = false;
					}else if(0.33 < randDirectionX && randDirectionX < 0.67){
						ball.moveRight();
						movingRight = true;
						movingLeft = false;
						notMovingX = false;
					}else{
						notMovingX = true;
					}
					//Random Direction for y position
					if(randDirectionY < 0.33){
						ball.moveUp();
						movingUp = true;
						movingDown = false;
						notMovingY = false;
					}else if(0.33 < randDirectionY && randDirectionY < 0.67){
						ball.moveDown();
						movingDown = true;
						movingUp = false;
						notMovingY = false;
					}else{
						notMovingY = true;
					}
					
					//If the ball is not moving, x & y showdown. Higher probability to 50%
					if(notMovingX && notMovingY){
						//Random Direction for x position
						if(randDirectionX < 0.5){
							ball.moveRight();
							movingRight = true;
							notMovingX = false;
						}else{
							ball.moveLeft();
							movingLeft = true;
							notMovingX = false;
						}
						//Random Direction for y position
						if(randDirectionY < 0.5){
							ball.moveDown();
							movingDown = true;
							notMovingY = false;
						}else{
							ball.moveUp();
							movingUp = true;
							notMovingY = false;
						}
					}
				}
				playerCollision();
				enemyCollision();
				//attackerAI();
				defenderAI();
				playerScore();
				enemyScore();
				repaint();
			}
		}, 0, 30);
	}
	
	public void playerScore(){
		if((ball.x > 440 && ball.y > 400) || (ball.x > 440 && ball.y < 60)){
			if(!playerScored){
				arena.playerScore++;
				playerScored = true;
				if(ball.ballSpeed < 10){
					ball.ballSpeed++;
				}else{
					ball.ballSpeed = 10;
				}
			}
		}else{
			playerScored = false;
		}
	}
	
	public void enemyScore(){
		if((ball.x < 60 && ball.y > 440) || (ball.x < 60 && ball.y < 60)){
			if(!enemyScored){
				arena.enemyScore++;
				enemyScored = true;
				if(ball.ballSpeed < 10){
					ball.ballSpeed++;
				}else{
					ball.ballSpeed = 10;
				}
			}
		}else{
			enemyScored = false;
		}
	}
	
	public void playerCollision(){
		Rectangle playerHitBox = p1.getHitBox();
		Rectangle ballHitBox = ball.getHitBox();
		//Rectangle enemyHitBox = e1.getHitBox();
		if(playerHitBox.intersects(ballHitBox) || playerHitBox.contains(ballHitBox)){
			Point pPoint = playerHitBox.getLocation();
			Point bPoint = ballHitBox.getLocation();
			double xDiff = bPoint.getX() - pPoint.getX();
			double yDiff = bPoint.getY() - pPoint.getY();
			System.out.println("The X Intersection happens at: " + xDiff);
			System.out.println("The Y Intersection happens at: " + yDiff);
			//move in the directions set by the boolean flow above
			if(xDiff < 0){
				ball.moveLeft();
				movingLeft = true;
				movingRight = false;
			}
			
			if(yDiff < 0){
				ball.moveUp();
				movingUp = true;
				movingDown = false;
			}
			
			if(yDiff >= 10){
				ball.moveDown();
				movingUp = false;
				movingDown = true;
			}
			
			if(xDiff >= 10){
				ball.moveRight();
				movingRight = true;
				movingLeft = false;
			}
		}
		
		/*if(playerHitBox.intersects(enemyHitBox) || playerHitBox.contains(enemyHitBox)){
			Point pPoint = playerHitBox.getLocation();
			Point ePoint = enemyHitBox.getLocation();
			double xDiff = ePoint.getX() - pPoint.getX();
			double yDiff = ePoint.getY() - pPoint.getY();
			System.out.println("Player Collides with enemy X: " + xDiff);
			System.out.println("Player Collides with enemy Y: " + yDiff);
			//move in the directions set by the boolean flow above
			if(xDiff == 0 && yDiff == 0){
				if(movingLeft){
					p1.moveLeft();
				}
				if(movingRight){
					p1.moveRight();
				}
				if(movingUp){
					p1.moveUp();
				}
				if(movingDown){
					p1.moveDown();
				}
			}
		}*/
	}
	
	public void enemyCollision(){
		Rectangle enemyHitBox = e1.getHitBox();
		Rectangle ballHitBox = ball.getHitBox();
		//Rectangle playerHitBox = p1.getHitBox();
		if(enemyHitBox.intersects(ballHitBox) || enemyHitBox.contains(ballHitBox)){
			Point ePoint = enemyHitBox.getLocation();
			Point bPoint = ballHitBox.getLocation();
			double xDiff = bPoint.getX() - ePoint.getX();
			double yDiff = bPoint.getY() - ePoint.getY();
			System.out.println("The X Intersection happens at: " + xDiff);
			System.out.println("The Y Intersection happens at: " + yDiff);
			//move in the directions set by the boolean flow above
			if(xDiff < 0){
				ball.moveLeft();
				movingLeft = true;
				movingRight = false;
			}
			
			if(yDiff < 0){
				ball.moveUp();
				movingUp = true;
				movingDown = false;
			}
			
			if(yDiff >= 10){
				ball.moveDown();
				movingUp = false;
				movingDown = true;
			}
			
			if(xDiff >= 10){
				ball.moveRight();
				movingRight = true;
				movingLeft = false;
			}
		}
		
		/*if(enemyHitBox.intersects(playerHitBox) || enemyHitBox.contains(playerHitBox)){
			Point pPoint = playerHitBox.getLocation();
			Point ePoint = enemyHitBox.getLocation();
			double xDiff = ePoint.getX() - pPoint.getX();
			double yDiff = ePoint.getY() - pPoint.getY();
			System.out.println("Enemy Collides with player X: " + xDiff);
			System.out.println("Enemy Collides with player Y: " + yDiff);
			//move in the directions set by the boolean flow above
			if(xDiff == 0 && yDiff == 0){
				if(movingLeft){
					e1.moveLeft();
				}
				if(movingRight){
					e1.moveRight();
				}
				if(movingUp){
					e1.moveUp();
				}
				if(movingDown){
					e1.moveDown();
				}
			}
		}*/
	}
	
	public void attackerAI(){
		if(ball.x > e1.x){
			e1.moveRight();
		}else if(ball.y < e1.y){
			e1.moveUp();
		}
		
		if(ball.y > e1.y){
			e1.moveDown();
		}else if(ball.x < e1.x){
			e1.moveLeft();
		}
	}
	
	public void defenderAI(){
		if(ball.x > 265 && ball.x < e1.x){
			e1.moveLeft();
		}
		
		if(ball.x < 265 && e1.x < 420){
			e1.moveRight();
		}
		
		if(ball.y < e1.y && ball.x < 400){
			e1.moveUp();
		}
		
		if(ball.y > e1.y && ball.x < 400){
			e1.moveDown();
		}
	}

	public void startGame(){
		setSize(520,540);
		setVisible(true);
		Game game = new Game();
		game.setBackground(Color.WHITE);
		game.setSize(450,450);
		game.setVisible(true);
		game.setFocusable(true);
		this.add(game);
		game.addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e) {
				
			}

			@Override
			/** Handle the key pressed event from the text field. */
		    public synchronized void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
				listOfKeys.add(e.getKeyCode());
				if(System.currentTimeMillis() - lastProcessed > .00000000000000001) {
		            //Do your work here...
		            lastProcessed = System.currentTimeMillis();
		            if(listOfKeys.size() > 0){
		            	for(Integer key: listOfKeys){
			            	switch(key){
								case KeyEvent.VK_LEFT:
									p1.moveTo(-1, 0);
									System.out.println("Moving Left: " + p1.getX());
									break;
								case KeyEvent.VK_RIGHT:
									p1.moveTo(1, 0);
									System.out.println("Moving Right " + p1.getX());
									break;
								case KeyEvent.VK_UP:
									p1.moveTo(0, -1);
									System.out.println("Moving Up In The World " + p1.getY());
									break;
								case KeyEvent.VK_DOWN:
									p1.moveTo(0, 1);
									System.out.println("Moving Down " + p1.getY());
									break;
								default:
									break;
			            	}
			            }
		            }
					repaint();
				}
			}

			@Override
			public synchronized void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				touchHeld = false;
				listOfKeys.remove(e.getKeyCode());
			}
		});
		//Linked List & Binary Tree Logic for fun
		LinkedList dll = new LinkedList();
        dll.add(10);
        dll.add(34);
        dll.add(56);
        dll.add(364);
        System.out.println(dll.get(3));
        
        BinarySearchTree searchTree = new BinarySearchTree();
        searchTree.insertNode(5);
        searchTree.insertNode(3);
        searchTree.insertNode(7);
        searchTree.insertNode(2);
        searchTree.insertNode(6);
        //searchTree.insertNode(3);
        //searchTree.deleteNode(3);
        Node myRoot = searchTree.getRoot();
        System.out.println("--------------------------------------");
        System.out.println("All Nodes in Tree:");
        searchTree.printItems(myRoot);
        //System.out.println(searchTree.countItems(myRoot));
        System.out.println("\n--------------------------------------");
        searchTree.printTree(myRoot, 0);
	}
	
	/*  
	}*/
	
	public class LinkedList
	{
		// reference to the head node.
		private Node head;
		private int listCount;
		
		// LinkedList constructor
		public LinkedList()
		{
			// this is an empty list, so the reference to the head node
			// is set to a new node with no data
			head = new Node(null);
			listCount = 0;
		}
		
		public void add(Object data)
		// post: appends the specified element to the end of this list.
		{
			Node temp = new Node(data);
			Node current = head;
			// starting at the head node, crawl to the end of the list
			while(current.getNext() != null)
			{
				current = current.getNext();
			}
			// the last node's "next" reference set to our new node
			current.setNext(temp);
			listCount++;// increment the number of elements variable
		}
		
		public void add(Object data, int index)
		// post: inserts the specified element at the specified position in this list.
		{
			Node temp = new Node(data);
			Node current = head;
			// crawl to the requested index or the last element in the list,
			// whichever comes first
			for(int i = 1; i < index && current.getNext() != null; i++)
			{
				current = current.getNext();
			}
			// set the new node's next-node reference to this node's next-node reference
			temp.setNext(current.getNext());
			// now set this node's next-node reference to the new node
			current.setNext(temp);
			listCount++;// increment the number of elements variable
		}
		
		public Object get(int index)
		// post: returns the element at the specified position in this list.
		{
			// index must be 1 or higher
			if(index <= 0)
				return null;
			
			Node current = head.getNext();
			for(int i = 1; i < index; i++)
			{
				if(current.getNext() == null)
					return null;
				
				current = current.getNext();
			}
			return current.getData();
		}
		
		public boolean remove(int index)
		// post: removes the element at the specified position in this list.
		{
			// if the index is out of range, exit
			if(index < 1 || index > size())
				return false;
			
			Node current = head;
			for(int i = 1; i < index; i++)
			{
				if(current.getNext() == null)
					return false;
				
				current = current.getNext();
			}
			current.setNext(current.getNext().getNext());
			listCount--; // decrement the number of elements variable
			return true;
		}
		
		public int size()
		// post: returns the number of elements in this list.
		{
			return listCount;
		}
		
		public String toString()
		{
			Node current = head.getNext();
			String output = "";
			while(current != null)
			{
				output += "[" + current.getData().toString() + "]";
				current = current.getNext();
			}
			return output;
		}
		
		private class Node
		{
			// reference to the next node in the chain,
			// or null if there isn't one.
			Node next;
			// data carried by this node.
			// could be of any type you need.
			Object data;
			

			// Node constructor
			public Node(Object _data)
			{
				next = null;
				data = _data;
			}
			
			// another Node constructor if we want to
			// specify the node to point to.
			@SuppressWarnings("unused")
			public Node(Object _data, Node _next)
			{
				next = _next;
				data = _data;
			}
			
			// these methods should be self-explanatory
			public Object getData()
			{
				return data;
			}
			
			@SuppressWarnings("unused")
			public void setData(Object _data)
			{
				data = _data;
			}
			
			public Node getNext()
			{
				return next;
			}
			
			public void setNext(Node _next)
			{
				next = _next;
			}
		}
	}
	
	public class BinarySearchTree{
		Node root;
		int level = 0;
		public void insertNode(int value){
			if(root == null){
				root = new Node(value);
				System.out.println("Root Node " + root.cargo + " has been created.");
				level = 1;
				return;
			}
			Node current;
			current = root;
			while(true){
				if(value < current.cargo){
					if(current.left == null){
						current.left = new Node(value);
						System.out.println("Left Node " + current.left.cargo + " has been created.");
						return;
					}else{
						current = current.left;
					}
				}else if(value == current.cargo){
					System.out.println("Unable to create new Node. The value already exists in the tree.");
					return;
				}else{
					if(current.right == null){
						current.right = new Node(value);
						System.out.println("Right Node " + current.right.cargo + " has been created.");
						return;
					}else{
						current = current.right;
					}
				}
			}
		}
		
		public void deleteNode(int value){
			if(root == null){
				System.out.println("The tree is empty fool!");
				return;
			}
			Node current;
			current = root;
			while(true){
				if(value < current.cargo){
					if(current.left != null && value == current.left.cargo){
						System.out.println("Left Node " + current.left.cargo + " has been deleted.");
						current.left = current.left.left;
						return;
					}else{
						current = current.left;
					}
				}else if(value == current.cargo && current == root){
					System.out.println("Root Node " + current.cargo + " has been deleted.");
					root = root.right;
					return;
				}else if(value > current.cargo){
					if(current.right != null && value == current.right.cargo){
						System.out.println("Right Node " + current.right.cargo + " has been deleted.");
						current.right = current.right.right;
						return;
					}else{
						current = current.right;
					}
				}else{
					System.out.println("The value " + value + " does not exist in the tree.");
				}
			}
		}
		
		//in order traversal
		public void printItems(Node root){
			if(root != null){
				printItems(root.left);
				System.out.print(root.cargo + " ");
				printItems(root.right);
			}
		}
		
		//this function prints out just one level of nodes in the tree
		public void printTreeAtLevel(Node root, int level){
			if(root == null){
				return;
			}else{
				if(level == 0){
					System.out.print(root.cargo + " ");
				}else{
					printTreeAtLevel(root.left, level - 1);
					printTreeAtLevel(root.right, level - 1);
				}
			}
			level++;
			System.out.println("The count of levels = " + level);
		}
		
		//This function prints out the entire tree in a tree type format
		void printTree(Node node, int spaces)
		{
		  int i;
		 
		  if(node != null){
			  printTree(node.right, spaces + 3);
			  for(i = 0; i < spaces; i++){
				  System.out.print(" ");
			  }    
			  System.out.println(node.cargo);
			  printTree(node.left, spaces + 3);
		  }
		}
		
		public int countItems(Node root){
			if(root == null){
				return 0;
			}else{
				int count = 1;
				count += countItems(root.left);
				count += countItems(root.right);
				return count;
			}
		}
		
		public Node getRoot(){
			return root;
		}
	}
	
	//Node class
	public class Node{
		Node left;
		Node right;
		int cargo;
		
		public Node(int value){
			cargo = value;
			left = null;
			right = null;
		}
	}
}
