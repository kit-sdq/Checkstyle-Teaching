public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        System.out.println(main.getSequence(8, 20));
        System.out.println(main.getPointRange(2.0));
        System.out.println(main.getMultiplicationTable());
    }
    
    private String getSequence(int start, int iterations) {
        int number = start;
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            output.append(number).append(",");
            
            if (number % 2 == 0) {
                number /= 2;
            } else {
                number = 5 * number + 1;
            }
        }
        
        // Strip last comma
        return output.substring(0, output.length() - 1);
    }
    
    private String getPointRange(double grade) {
        int integerGrade = (int) (grade * 10);
        switch (integerGrade) {
            case 10:
                return "69.5 74";
            case 13:
                return "64.5 69";
            case 17:
                return "59.5 64";
            case 20:
                return "54.5 59";
            case 23:
                return "49.5 54";
            case 27:
                return "44.5 49";
            case 30:
                return "39.5 44";
            case 35:
                return "34.5 39";
            case 40:
                return "29.5 34";
            case 50:
                return "0 29";
            default:
                return "This shouldn't happen.";
                
        }
    }
    
    private String getMultiplicationTable() {
        StringBuilder output = new StringBuilder();
        for (int i = 11; i <= 20; i++) {
            for (int j = 1; j <= 10; j++) {
                output.append(j);
                output.append(" * ");
                output.append(i);
                output.append(" = ");
                output.append(i * j);
                output.append("\n");
            }
        }
        
        // Strip last line break
        return output.substring(0, output.length() - 1);
    }
}
