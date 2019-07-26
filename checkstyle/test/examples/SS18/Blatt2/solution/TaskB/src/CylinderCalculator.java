public class CylinderCalculator {
    public static void main(String[] args) {
        int diameter = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        
        double radius = diameter / 2.0;
        double volume = Math.PI * radius * radius * height;
        double lateralSurface = Math.PI * diameter * height;

        System.out.println(volume + ";" + lateralSurface);
    }
}
