public class Rectangle {
	private int a;
	private int b;
	
	public Rectangle(int a, int b) {
		this.a = a;
		this.b = b;
	}
	
	public int surface() {
		return a * b;
	}
	
	public static int surface(Rectangle rectangle) {
		return rectangle.a * rectangle.b;
	}
}