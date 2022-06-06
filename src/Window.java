import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window extends JFrame implements KeyListener {

  public static int height = 400;
  public static int width = 400;

  JPanel WindowContainer = new JPanel();
  JLabel gameOver = new JLabel();
  JLabel EndGame = new JLabel();

  SnakeVisual snake;

  public Window() {
    this.setTitle("Snake");
    this.setSize(height, width);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    //		WindowContainer.setBackground(Color.LIGHT_GRAY);
    this.setContentPane(WindowContainer);
    this.addKeyListener(this);
    this.setVisible(true);

    gameOver.setBounds((Window.height / 2) - 40, 0, 100, 20);
    EndGame.setBounds((Window.width / 2) - 30, 20, 100, 20);

    snake = new SnakeVisual(this.getGraphics(), 3);
  }

  public void GameOver() {
    gameOver.setText("Game Over !");
    WindowContainer.add(gameOver);
    WindowContainer.repaint();
  }
  
  public void EndGame() {
	gameOver.setText("Fin du jeu !");
	EndGame.setText("Score : "+Integer.toString(snake.score));
	WindowContainer.add(gameOver);
    WindowContainer.add(EndGame);
    WindowContainer.repaint();
  }

  public void startGame() {
    snake.play();
    if(snake.win) {
    	EndGame();
    } else { 
       GameOver();
    }
  }

  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
    if (e.getKeyCode() == KeyEvent.VK_LEFT && snake.direction != 2) {
      //			System.out.println(snake.direction);
      snake.direction = 1;
    }
    if (e.getKeyCode() == KeyEvent.VK_RIGHT && snake.direction != 1) {
      //			System.out.println(snake.direction);
      snake.direction = 2;
    }
    if (e.getKeyCode() == KeyEvent.VK_UP && snake.direction != 4) {
      //			System.out.println(snake.direction);
      snake.direction = 3;
    }
    if (e.getKeyCode() == KeyEvent.VK_DOWN && snake.direction != 3) {
      //			System.out.println(snake.direction);
      snake.direction = 4;
    }
  }

  public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub

  }

}