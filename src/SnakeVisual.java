 import java.awt.Color;
 import java.awt.Font;
 import java.awt.Graphics;
 import java.util.ArrayList;
 import java.util.Scanner;

 public class SnakeVisual {
	 
   public int height;
   
   public boolean play = true;
   
   Scanner sc;
   Graphics g;
   
   public int score = 0;
   public int live = 0;
   public int direction = 1;
   public int level = 1;
   
   public int nbApple = 1;
   public int nbObstacles = 0;
   public int blueApple = 3;
   
   public int CountBlue;
   public int CountRed;

   public Color darkGreen = new Color(29, 131, 72);
   public Color darkRed = new Color(176, 58, 46);
   public Color darkBlue = new Color(33, 97, 140);
   public Color snakeHead = new Color(28, 40, 51);
   public Color snakeTail = new Color(52, 73, 94);
   public Color windowColor = new Color(236, 240, 241);

   ArrayList < Square > body = new ArrayList < Square > ();
   ArrayList < Apple > apples = new ArrayList < Apple > ();
   ArrayList < Square > obstacles = new ArrayList < Square > ();
//   ArrayList < Square > border = new ArrayList < Square > ();
   
   public boolean win = false;

   public SnakeVisual(Graphics g, int height) {
     this.g = g;
     this.height = height;
     sc = new Scanner(System.in);
   }

   public void play() {
     createSnake();

     while (play == true) {
       g.setColor(windowColor);
       g.fillRect(0, 0, Window.height, Window.width);

       CreateApple();
       
       DesignApple();
       DesignSnake();
       DesignObstacles();
      
       
       ShowScore();
       ShowLive();
       ShowLevel();
       
       
//      CreateBorder();
//      DesignBorder();
       
       sleep(200);
 
       NextLevel();
       Collider();
       move();
     }
   }

   public void sleep(int time) { //Faire une pause de tout les (int time) dans le thread
     try {
       Thread.sleep(time);
     } catch (InterruptedException e) { 
       Thread.currentThread().interrupt();
     }
   }
   
   public void NextLevel() {
	   if(body.size() >= 4 && this.level == 1) {
    	   this.level++;
    	   this.live = 0;
    	   this.CountRed = 0;
    	   this.nbObstacles = 3;
    	   this.blueApple = 5;
    	   Respawn();
    	   CreateObstacles();
       } else if (body.size() >= 60 && this.level == 2) {
    	   this.level++;
    	   this.live = 0;
    	   this.CountRed = 0;
    	   this.nbObstacles = 5;
    	   this.blueApple = 10;
    	   obstacles.clear(); //vider le tableau des obstacles present sur le niveau 2
    	   Respawn();
    	   CreateObstacles();
       } else if (body.size() >= 100 && this.level == 3) {
    	   this.play = false;
    	   this.win = true;
       }
   }

   public void move() {
     int posX, posY;

     for (int i = body.size() - 1; i > 0; i--) {
         Square s;
         s = body.get(i - 1); //prendre la position de carré placé devant
         //System.out.println("s"+i+" pos ="+ s.posX);
         posX = s.posX;
         posY = s.posY;
         s = body.get(i); //initialiser la position du carré avec la position du precedent
         s.posX = posX;
         s.posY = posY;
     }
     
     Square sq = body.get(0);

     if (direction == 1) { //left
       sq.posX -= 10;
     }
     //		
     if (direction == 2) { //right
       sq.posX += 10;
     }
     //		
     if (direction == 3) { //up
       sq.posY -= 10;
     }

     if (direction == 4) { //down
       sq.posY += 10;
     }
    
   }

   public void Collider() {

     //MANGER UNE POMME
     for (int i = 0; i < apples.size(); i++) {
       Apple Apple = apples.get(i);
       Square SnakeHead = body.get(0);
       Square LastSquare = body.get(body.size() - 1);

       if (Apple.posX == SnakeHead.posX && Apple.posY == SnakeHead.posY) { //si la tete qui a la meme position que la pomme
         //System.out.println("COLLISION");
         body.add(new Square(((LastSquare.posX) + 10), 0, snakeTail));
         apples.remove(i);
         if (Apple.color == darkBlue) {
           this.score += 10;
         } else if (Apple.color == darkRed) {
           this.live++;
           this.score++;
         } else {
           this.score++;
         }
         //System.out.println(LastSquare.posX);
       }
     }
     

     //SE PRENDRE LA QUEUE DU SERPENT
     for (int i = 1; i < body.size(); i++) {
       Square BodySnake = body.get(i);
       Square SnakeHead = body.get(0);

       if (SnakeHead.posX == BodySnake.posX && SnakeHead.posY == BodySnake.posY) { //si la tete qui a la meme position qu'un carré du corps
         if (this.live > 0) {
           Respawn();
         } else {
           play = false;
         }
       }
     }
     
     
     //PRENDRE UN OBSTACLE
     for (int i = 0; i < obstacles.size(); i++) { 
       Square ob = obstacles.get(i);
       Square SnakeHead = body.get(0);

       if (ob.posX == SnakeHead.posX && ob.posY == SnakeHead.posY) { ///si la tete qui a la meme position qu'un obstacle
         //System.out.println("COLLISION");
    	   if(this.live > 0) {
               Respawn();
           } else {
               play = false;
           }
       }
     }

   //DEPASSEMENT DE LA FENETRE
     Square SnakeHead = body.get(0);
     if((SnakeHead.posX) < 0) { //bordure gauche
         if(this.live > 0) {
             Respawn();
         } else {
             play = false;
         }
//         play = false;
//         System.out.println("COLLISION");
     }
     if((SnakeHead.posX) > (Window.width-10)) { //bordure droite
         if(this.live > 0) {
             Respawn();
         } else {
             play = false;
         }
//         play = false;
//         System.out.println("COLLISION");
     }
     if((SnakeHead.posY) < 30) { //bordure haut
         if(this.live > 0) {
             Respawn();
         } else {
             play = false;
         }
//         play = false;
//         System.out.println("COLLISION");
     }
     if((SnakeHead.posY) > (Window.height-10)) { //bordure bas
         if(this.live > 0) {
             Respawn();
         } else {
             play = false;
         }
     }
//         play = false;
//         System.out.println("COLLISION");
     }

   public void Respawn() {
     body.clear();
     if(this.live >= 1) {
    	 this.live--;
     }
     this.direction = 1;
     createSnake();
   }
   
   public void ShowLive() {
     g.setColor(darkRed);
     g.setFont(new Font("Helvetica", Font.BOLD, 15));
     g.drawString(Integer.toString(this.live), Window.width-30, Window.height - 20);
   }
   
   public void ShowLevel() {
	 g.setColor(Color.black);
     g.setFont(new Font("Helvetica", Font.BOLD, 15));
     g.drawString("Level  " + Integer.toString(this.level), (Window.width/2-25), 60);
   }

   public void ShowScore() {
     g.setColor(darkGreen);
     g.setFont(new Font("Helvetica", Font.BOLD, 15));
     g.drawString(Integer.toString(this.score), 20, Window.height - 20);
   }

   public void createSnake() {
     for (int j = 0; j < this.height; j++) {
       int height;
       height = ((int) Window.height / 2) / 10;
       height *= 10;
       if (j == 0) {
         body.add(new Square((Window.height / 2), height, snakeHead));
       } else if (j > 0) {
         body.add(new Square(Window.height / 2 + ((j) * 10), height, snakeTail));
       }
     }
   }
   
   
   public void CreateApple() {
     int randX, randY;
     Boolean create = true;

     //System.out.println(this.CountBlue);

     while (apples.size() < nbApple) {
       create = true;
       int widthWindow = ((Window.width - 40) / 10);
       int heightWindow = ((Window.height - 40) / 10);

       randX = (int)((Math.random() * (widthWindow)) + 3);
       randY = (int)((Math.random() * (heightWindow)) + 3);

       randX = (randX * 10);
       randY = (randY * 10);

       for (int i = 0; i < body.size(); i++) {
         Square Exist = body.get(i);
         if (apples.size() > 0) { //verifier qu'une pomme n'existe pas sur cette position
           for (int j = 0; j < apples.size(); j++) {
             Apple ExistAp = apples.get(j);
             if(obstacles.size() > 0){
            	 for(int x = 0; x < obstacles.size(); x++) {	  
            		 Square obExist = obstacles.get(x);
            		 if (randX == Exist.posX && randY == Exist.posY || randX == ExistAp.posX && randY == ExistAp.posY || randX == obExist.posX && randY == obExist.posY) {
            			 create = false;
            		 }
            	 }
             } else {
            	 if (randX == Exist.posX && randY == Exist.posY || randX == ExistAp.posX && randY == ExistAp.posY) {
            		 create = false;
            	 }
             } 
           }
         } else {
        	 if(obstacles.size() > 0) {
        		 for(int x = 0; x < obstacles.size(); x++) {
        			 Square obExist = obstacles.get(x);
        			 if (randX == Exist.posX && randY == Exist.posY || randX == obExist.posX && randY == obExist.posY) {
        				 create = false;
        			 }
        		 }
        	 } else {
        		 if (randX == Exist.posX && randY == Exist.posY) { //verifier que le snake n'est pas sur cette position
        			 create = false;
        		 }
        	 }
         }
       }

       if (create) {
         int RandomInt = (int)(Math.random() * 20);
         int RandomIntR = (int)(Math.random() * 30);
//         System.out.println(RandomInt);
//         System.out.println(RandomIntR);
         if (RandomInt == 1 && this.CountBlue < this.blueApple) {
           apples.add(new Apple(randX, randY, darkBlue));
           this.CountBlue++;
         } else if (RandomIntR == 1 && this.CountRed < 2) {
           apples.add(new Apple(randX, randY, darkRed));
           this.CountRed++;
         } else {
           apples.add(new Apple(randX, randY, darkGreen));
         }
       }
     }
   }
   
   public void CreateObstacles() {
	     int randX, randY;
	     Boolean create = true;

	     //System.out.println(this.CountBlue);

	     while (obstacles.size() < this.nbObstacles) {
	       create = true;
	       int widthWindow = ((Window.width - 40) / 10);
	       int heightWindow = ((Window.height - 40) / 10);

	       randX = (int)((Math.random() * (widthWindow)) + 3);
	       randY = (int)((Math.random() * (heightWindow)) + 3);

	       randX = (randX * 10);
	       randY = (randY * 10);

	       for (int i = 0; i < body.size(); i++) {
	         Square Exist = body.get(i);
	         Square SnakeHead = body.get(0);
	         if(obstacles.size() > 0) {
	    		  for(int x = 0; x < obstacles.size(); x++) {	  
	    			  Square ExistOb = obstacles.get(x);
	    			  if (apples.size() > 0) { //verifier qu'une pomme n'existe pas sur cette position
	    		           for (int j = 0; j < apples.size(); j++) {
	    		             Apple ExistAp = apples.get(j);
	    		             if (randX == Exist.posX && randY == Exist.posY || randX == ExistAp.posX && randY == ExistAp.posY || randX == ExistOb.posX && randY == ExistOb.posY || randX == SnakeHead.posX-10 || randX == SnakeHead.posX-20) {
	    		               create = false;
	    		             }
	    		           }
	    		      } else {
	    		    	  if (randX == Exist.posX && randY == Exist.posY || randX == ExistOb.posX && randY == ExistOb.posY || randX == SnakeHead.posX-10 || randX == SnakeHead.posX-20) {
	    		               create = false;
	    		          }
	    		      }
	    		  }
	    	 } else {
	    		 if (apples.size() > 0) { //verifier qu'une pomme n'existe pas sur cette position
 		           for (int j = 0; j < apples.size(); j++) {
 		             Apple ExistAp = apples.get(j);
 		             if (randX == Exist.posX && randY == Exist.posY || randX == ExistAp.posX && randY == ExistAp.posY || randX == SnakeHead.posX-10 || randX == SnakeHead.posX-20) {
 		               create = false;
 		             }
 		           }
	    		 } else {
	    			 if (randX == Exist.posX && randY == Exist.posY) { //verifier que le snake n'est pas sur cette position
	    	             create = false;
	    	         }
	    		 }
	    	 }
	       }
	       

	       if (create) {
	    	   obstacles.add(new Square(randX, randY, Color.black));
	       }
	     }
   }
   
//   public void CreateBorder() {
//	   for(int i = 0; i < Window.width; i = i + 10) { //gauche
//		   border.add(new Square(0, i, Color.LIGHT_GRAY));
//	   }
//	   for(int i = 0; i < Window.width; i = i + 10) { //droite
//		   border.add(new Square(Window.width - 10, i, Color.LIGHT_GRAY));
//	   }
//	   for(int i = 0; i < Window.height; i = i + 10) { //haut
//		   border.add(new Square(i, 30, Color.LIGHT_GRAY));
//	   }
//	   for(int i = 0; i < Window.height; i = i + 10) { //bas
//		   border.add(new Square(i, Window.height-10, Color.LIGHT_GRAY));
//	   }
//   }
//   
//   public void DesignBorder() {
//	  for (int x = 0; x < border.size(); x++) {
//       Square b = border.get(x);
//
//       g.setColor(b.color);
//       g.fillRect(b.posX, b.posY, 10, 10);
//	  }
//   }

   public void DesignApple() {
     for (int x = 0; x < apples.size(); x++) {
       Apple a = apples.get(x);

       g.setColor(a.color);
       g.fillOval(a.posX, a.posY, 10, 10);
     }
   }

   public void DesignSnake() {
     for (int x = 0; x < body.size(); x++) {
       Square s = body.get(x);

       g.setColor(s.color);
       g.fillRect(s.posX, s.posY, 10, 10);
     }
   }
   
   public void DesignObstacles() {
	  for (int x = 0; x < obstacles.size(); x++) {
       Square o = obstacles.get(x);

       g.setColor(o.color);
       g.fillRect(o.posX, o.posY, 10, 10);
	  }
	}
   
 }