# **Game Pong**

<img width="1057" height="783" alt="image" src="https://github.com/user-attachments/assets/05587719-90fb-4649-b3fb-d8abf35e157e" />
<img width="1397" height="1451" alt="image" src="https://github.com/user-attachments/assets/5fecd93b-8ca9-40bc-850c-b8845828e3b9" />


Pada latihan ini, dibuat sebuah permainan **Pong** sederhana menggunakan bahasa pemrograman **Java** dan pustaka GUI **Swing**. Pong adalah game klasik dua dimensi yang meniru permainan tenis meja.

Game terdiri dari empat komponen utama:

### **1. Renderer.java**

Bertanggung jawab melakukan *rendering* (menggambar) semua elemen game menggunakan `Graphics2D`. Komponen ini dipanggil secara terus-menerus oleh Swing untuk memperbarui tampilan game.

### **2. Pong.java**

Merupakan kelas utama yang menangani:

* Logika permainan
* Input keyboard
* Update posisi bola & paddle
* Pembuatan jendela game
* Pemilihan mode permainan (player vs player atau player vs bot)

Kelas ini juga memuat *main loop* menggunakan `Timer` untuk pembaruan game setiap 20 ms.

### **3. Paddle.java**

Mewakili paddle pemain:

* Dapat bergerak ke atas/bawah
* Dapat digerakkan oleh pemain atau oleh bot

### **4. Ball.java**

Mewakili bola, termasuk:

* Gerakan bola
* Deteksi tabrakan
* Sistem skor
* Reset bola setelah mencetak skor

---

# **Source Code**

---

## **Renderer.java**

```java
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Renderer extends JPanel {

	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Pong.pong.render((Graphics2D) g);
	}
}
```

---

## **Pong.java**

```java
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener {
	public static Pong pong;
	public int width = 700, height = 700;
	public Renderer renderer;
	public Paddle player1;
	public Paddle player2;
	public Ball ball;
	public boolean bot = false, selectingDifficulty;
	public boolean w, s, up, down;
	public int gameStatus = 0, scoreLimit = 7, playerWon; 
	public int botDifficulty, botMoves, botCooldown = 0;
	public Random random;
	public JFrame jframe;

	public Pong() {
		Timer timer = new Timer(20, this);
		random = new Random();
		jframe = new JFrame("Pong");
		renderer = new Renderer();
		jframe.setSize(width + 15, height + 35);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(renderer);
		jframe.addKeyListener(this);
		timer.start();
	}

	public void start() {
		gameStatus = 2;
		player1 = new Paddle(this, 1);
		player2 = new Paddle(this, 2);
		ball = new Ball(this);
	}

	public void update() {
		if (player1.score >= scoreLimit) {
			playerWon = 1;
			gameStatus = 3;
		}

		if (player2.score >= scoreLimit) {
			playerWon = 2;
			gameStatus = 3;
		}

		if (w) player1.move(true);
		if (s) player1.move(false);

		if (!bot) {
			if (up) player2.move(true);
			if (down) player2.move(false);
		} else {
			handleBotMovement();
		}

		ball.update(player1, player2);
	}

	private void handleBotMovement() {
		if (botCooldown > 0) {
			botCooldown--;
			if (botCooldown == 0) botMoves = 0;
		}

		if (botMoves < 10) {
			if (player2.y + player2.height / 2 < ball.y) {
				player2.move(false);
				botMoves++;
			}

			if (player2.y + player2.height / 2 > ball.y) {
				player2.move(true);
				botMoves++;
			}

			if (botDifficulty == 0) botCooldown = 20;
			if (botDifficulty == 1) botCooldown = 15;
			if (botDifficulty == 2) botCooldown = 10;
		}
	}

	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (gameStatus == 0) renderMenu(g);
		if (selectingDifficulty) renderDifficultyMenu(g);
		if (gameStatus == 1) renderPause(g);
		if (gameStatus == 1 || gameStatus == 2) renderGame(g);
		if (gameStatus == 3) renderGameOver(g);
	}

	private void renderMenu(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 50));
		g.drawString("PONG", width / 2 - 75, 50);

		if (!selectingDifficulty) {
			g.setFont(new Font("Arial", 1, 30));
			g.drawString("Press Space to Play", width / 2 - 150, height / 2 - 25);
			g.drawString("Press Shift to Play with Bot", width / 2 - 200, height / 2 + 25);
			g.drawString("<< Score Limit: " + scoreLimit + " >>", width / 2 - 150, height / 2 + 75);
		}
	}

	private void renderDifficultyMenu(Graphics2D g) {
		String diff = botDifficulty == 0 ? "Easy" : (botDifficulty == 1 ? "Medium" : "Hard");
		g.setFont(new Font("Arial", 1, 30));

		g.drawString("<< Bot Difficulty: " + diff + " >>", width / 2 - 180, height / 2 - 25);
		g.drawString("Press Space to Play", width / 2 - 150, height / 2 + 25);
	}

	private void renderPause(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 50));
		g.drawString("PAUSED", width / 2 - 103, height / 2 - 25);
	}

	private void renderGame(Graphics2D g) {
		g.setColor(Color.WHITE);

		g.setStroke(new BasicStroke(5f));
		g.drawLine(width / 2, 0, width / 2, height);

		g.setStroke(new BasicStroke(2f));
		g.drawOval(width / 2 - 150, height / 2 - 150, 300, 300);

		g.setFont(new Font("Arial", 1, 50));
		g.drawString(String.valueOf(player1.score), width / 2 - 90, 50);
		g.drawString(String.valueOf(player2.score), width / 2 + 65, 50);

		player1.render(g);
		player2.render(g);
		ball.render(g);
	}

	private void renderGameOver(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 1, 50));
		g.drawString("PONG", width / 2 - 75, 50);

		if (bot && playerWon == 2) {
			g.drawString("The Bot Wins!", width / 2 - 170, 200);
		} else {
			g.drawString("Player " + playerWon + " Wins!", width / 2 - 165, 200);
		}

		g.setFont(new Font("Arial", 1, 30));
		g.drawString("Press Space to Play Again", width / 2 - 185, height / 2 - 25);
		g.drawString("Press ESC for Menu", width / 2 - 140, height / 2 + 25);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameStatus == 2) update();
		renderer.repaint();
	}

	public static void main(String[] args) {
		pong = new Pong();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int id = e.getKeyCode();

		if (id == KeyEvent.VK_W) w = true;
		else if (id == KeyEvent.VK_S) s = true;
		else if (id == KeyEvent.VK_UP) up = true;
		else if (id == KeyEvent.VK_DOWN) down = true;
		else if (id == KeyEvent.VK_RIGHT) {
			if (selectingDifficulty) botDifficulty = (botDifficulty + 1) % 3;
			else if (gameStatus == 0) scoreLimit++;
		} else if (id == KeyEvent.VK_LEFT) {
			if (selectingDifficulty) botDifficulty = (botDifficulty + 2) % 3;
			else if (gameStatus == 0 && scoreLimit > 1) scoreLimit--;
		} else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3)) {
			gameStatus = 0;
		} else if (id == KeyEvent.VK_SHIFT && gameStatus == 0) {
			bot = true;
			selectingDifficulty = true;
		} else if (id == KeyEvent.VK_SPACE) {
			if (gameStatus == 0 || gameStatus == 3) {
				if (!selectingDifficulty) bot = false;
				else selectingDifficulty = false;
				start();
			} else if (gameStatus == 1) gameStatus = 2;
			else if (gameStatus == 2) gameStatus = 1;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int id = e.getKeyCode();

		if (id == KeyEvent.VK_W) w = false;
		else if (id == KeyEvent.VK_S) s = false;
		else if (id == KeyEvent.VK_UP) up = false;
		else if (id == KeyEvent.VK_DOWN) down = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
```

---

## **Paddle.java**

```java
import java.awt.Color;
import java.awt.Graphics;

public class Paddle {
	public int paddleNumber;
	public int x, y, width = 50, height = 250;
	public int score;

	public Paddle(Pong pong, int paddleNumber) {
		this.paddleNumber = paddleNumber;
		if (paddleNumber == 1) this.x = 0;
		if (paddleNumber == 2) this.x = pong.width - width;
		this.y = pong.height / 2 - this.height / 2;
	}

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}

	public void move(boolean up) {
		int speed = 15;

		if (up) {
			if (y - speed > 0) y -= speed;
			else y = 0;
		} else {
			if (y + height + speed < Pong.pong.height) y += speed;
			else y = Pong.pong.height - height;
		}
	}
}
```

---

## **Ball.java**

```java
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball {

	public int x, y, width = 25, height = 25;
	public int motionX, motionY;
	public Random random;
	private Pong pong;
	public int amountOfHits;

	public Ball(Pong pong) {
		this.pong = pong;
		this.random = new Random();
		spawn();
	}

	public void update(Paddle paddle1, Paddle paddle2) {
		int speed = 5;

		this.x += motionX * speed;
		this.y += motionY * speed;

		// Bounce off top/bottom
		if (this.y + height - motionY > pong.height || this.y + motionY < 0) {
			if (this.motionY < 0) {
				this.y = 0;
				this.motionY = random.nextInt(4);
				if (motionY == 0) motionY = 1;
			} else {
				this.motionY = -random.nextInt(4);
				this.y = pong.height - height;
				if (motionY == 0) motionY = -1;
			}
		}

		// Paddle collision
		if (checkCollision(paddle1) == 1) {
			this.motionX = 1 + (amountOfHits / 5);
			this.motionY = -2 + random.nextInt(4);
			if (motionY == 0) motionY = 1;
			amountOfHits++;
		} else if (checkCollision(paddle2) == 1) {
			this.motionX = -1 - (amountOfHits / 5);
			this.motionY = -2 + random.nextInt(4);
			if (motionY == 0) motionY = 1;
			amountOfHits++;
		}

		// Scoring
		if (checkCollision(paddle1) == 2) {
			paddle2.score++;
			spawn();
		} else if (checkCollision(paddle2) == 2) {
			paddle1.score++;
			spawn();
		}
	}

	public void spawn() {
		this.amountOfHits = 0;
		this.x = pong.width / 2 - this.width / 2;
		this.y = pong.height / 2 - this.height / 2;

		this.motionY = -2 + random.nextInt(4);
		if (motionY == 0) motionY = 1;

		if (random.nextBoolean()) motionX = 1;
		else motionX = -1;
	}

	public int checkCollision(Paddle paddle) {
		if (this.x < paddle.x + paddle.width && this.x + width > paddle.x &&
			this.y < paddle.y + paddle.height && this.y + height > paddle.y) {
			return 1; 
		}
		else if ((paddle.x > x && paddle.paddleNumber == 1) ||
				 (paddle.x < x - width && paddle.paddleNumber == 2)) {
			return 2; 
		}
		return 0; 
	}

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
	}
}
```

Cukup beri tahu — saya bisa buatkan!
