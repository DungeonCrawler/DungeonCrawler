package src.main.java.roguelike.ui.console;

public class Color {
	java.awt.Color color;
	
	public Color(int r, int g, int b) {
		color = new java.awt.Color(r, g, b);
	}
	
	private Color(java.awt.Color color) {
		this.color = color;
	}
	
	public Color darker() {
		return new Color(color.darker());
	}
	
	public Color brighter() {
		return new Color(color.brighter());
	}
	
	public final static Color black = new Color(java.awt.Color.black);
	public final static Color white = new Color(java.awt.Color.white);
	
	public final static Color green = new Color(java.awt.Color.green);
	public final static Color blue = new Color(java.awt.Color.blue);
	public final static Color cyan = new Color(java.awt.Color.cyan);
	public final static Color darkGray = new Color(java.awt.Color.darkGray);
	public final static Color gray = new Color(java.awt.Color.gray);
	public final static Color lightGray = new Color(java.awt.Color.lightGray);
	public final static Color magenta = new Color(java.awt.Color.magenta);
	public final static Color orange = new Color(java.awt.Color.orange);
	public final static Color pink = new Color(java.awt.Color.pink);
	public final static Color red = new Color(java.awt.Color.red);
	public final static Color yellow = new Color(java.awt.Color.yellow);
    public final static Color brown = new Color(150, 75, 0);
}
