public class CircleCalc {
    public static final double PI = 3.14159;
    
    public static void main(String[] args) {
        double radius = 3.0;
        double area = PI * radius * radius;
        double circumference = 2 * PI * radius;
        System.out.println(area);
        System.out.println(circumference);
    }
}
