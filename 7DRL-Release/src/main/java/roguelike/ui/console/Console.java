package src.main.java.roguelike.ui.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Console {
	private JFrame consoleFrame;
    private JPanel consolePanel;
    private Color[][] foregroundColors;
    private Color[][] backgroundColors;
    private char[][] characters;
	private int width, height;
	private int cursorX = 0, cursorY = 0;
	private boolean cursorVisible = true;
	private Color foreground = Color.white, background = Color.black;
	private boolean bold = false;
	
	private Font fontPlain, fontBold;
	
	private BlockingQueue<KeyEvent> keyPresses = new LinkedBlockingQueue<KeyEvent>();
	
	public Console(int w, int h) {
		super();
		System.setProperty("awt.useSystemAAFontSettings", "on");
		
		this.width = w;
		this.height = h;
		
		fontPlain = new Font(Font.MONOSPACED, Font.PLAIN, 14);
		fontBold = new Font(Font.MONOSPACED, Font.BOLD, 14);

        foregroundColors = new Color[width][height];
        backgroundColors = new Color[width][height];
        characters = new char[width][height];
        clear();

        consolePanel = new JPanel(false) {
            @Override
            public synchronized void paintComponent(Graphics g) {
                g.setFont(fontPlain);
                FontMetrics metrics = g.getFontMetrics();
                int ascent = metrics.getMaxAscent();
                int w = metrics.charWidth('W');
                int h = metrics.getHeight();
                
                for (int x = 0; x < width; ++x)
                for (int y = 0; y < height; ++y) {
                    g.setColor(backgroundColors[x][y].color);
                    g.fillRect(x * w, y * h, w, h);
                    g.setColor(foregroundColors[x][y].color);
                    g.drawString("" + characters[x][y], x * w, y * h + ascent);
                }
            }
        };

        final int charWidth = 8;
        final int charHeight = 18;
		
		consoleFrame = new JFrame("Console");
		consoleFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		consoleFrame.setLayout(new GridLayout(0, width, 0, 0));
        consoleFrame.setLayout(new BorderLayout(0, 0));
        //consoleFrame.getContentPane().setBackground(java.awt.Color.BLACK);
		consoleFrame.setMinimumSize(new Dimension(width*charWidth + 2, height*charHeight + 2));
        //consoleFrame.add(consolePanel);
        consoleFrame.setContentPane(consolePanel);
		consoleFrame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyPresses.add(e);
			}
		});

        consoleFrame.pack();
        
		consoleFrame.setVisible(true);
	}
	
	public void setTitle(String title) {
		consoleFrame.setTitle(title);
	}

    public void refresh() {
        
    }
	
	public void close() {
		consoleFrame.setVisible(false);
		consoleFrame.dispose();
		consoleFrame = null;
	}
	
	public boolean isOpen() {
		return consoleFrame != null && consoleFrame.isVisible();
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getCursorX() {
		return cursorX;
	}
	
	public int getCursorY() {
		return cursorY;
	}
	
	public void setCursor(int x, int y) {
		while (x < 0)
			x += width;
		while (y < 0)
			y += height;
		x = x % width;
		y = y % height;
		cursorX = x;
		cursorY = y;
	}

	public boolean isCursorVisible() {
		return cursorVisible;
	}

	public void setCursorVisible(boolean cursorVisible) {
		this.cursorVisible = cursorVisible;
	}

	public void clear() {
		for (int x = 0; x < width; ++x)
			for (int y = 0; y < height; ++y)
			{
				setBackground(x, y, background);
				setForeground(x, y, foreground);
				setCharacter(x, y, ' ');
			}
	}
	
	public void setCharacter(int x, int y, char character) {
        characters[x][y] = character;
	}
	
	public void setBackground(int x, int y, Color background) {
        backgroundColors[x][y] = background;
	}
	
	public void setForeground(int x, int y, Color foreground) {
        foregroundColors[x][y] = foreground;
	}
	
	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}
	
	public void setBold(int x, int y, boolean bold) {
        // TODO Not implemented
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public void print(char character) {
		if (character == '\b') {
			if (getCursorX() - 1 >= 0) {
				setCursor(getCursorX() - 1, getCursorY());
				setForeground(getCursorX(), getCursorY(), foreground);
				setBackground(getCursorX(), getCursorY(), background);
				setBold(getCursorX(), getCursorY(), bold);
				setCharacter(getCursorX(), getCursorY(), ' ');
			}
		}
		else if (character == '\n') {
			setCursor(0, getCursorY() + 1);
		}
		else {
			setForeground(getCursorX(), getCursorY(), foreground);
			setBackground(getCursorX(), getCursorY(), background);
			setBold(getCursorX(), getCursorY(), bold);
			setCharacter(getCursorX(), getCursorY(), character);
			if (getCursorX() + 1 >= width) {
				setCursor(0, getCursorY() + 1);
			}
			else {
				setCursor(getCursorX() + 1, getCursorY());
			}
		}
	}
	
	public void print(String str) {
		for (char c : str.toCharArray()) {
			print(c);
		}
	}
	
	public void println(String str) {
		print(str + "\n");
	}
	
	public void println() {
		println("");
	}
	
	public char readCharacter() throws InterruptedException {
		Key k;
		do {
			k = readKey();
		} while (!k.isCharacter());
		return k.getCharacter();
	}
	
	public String readLine(boolean echo) throws InterruptedException {
		StringBuilder str = new StringBuilder();
		while (true) {
			char c = readCharacter();
			if (c == '\b') {
				if (str.length() > 0) {
					if (echo)
						print(c);
					str.deleteCharAt(str.length() - 1);
				}
			}
			else {
				if (echo)
					print(c);
				if (c == '\n')
					return str.toString();
				else
					str.append(c);
			}
		}
	}
	
	public String readLine() throws InterruptedException {
		return readLine(true);
	}
	
	public Key readKey() throws InterruptedException {
        consolePanel.repaint();
		KeyEvent e;
		do {
			e = keyPresses.take();
		} while (e.getKeyCode() == KeyEvent.VK_SHIFT || 
				e.getKeyCode() == KeyEvent.VK_ALT || 
				e.getKeyCode() == KeyEvent.VK_CONTROL);
		return new Key(e.getKeyCode(), e.getKeyChar(), e.getModifiersEx());
	}
}
