public class Main {
    public static final double M = 0.5;

    public static void main(String[] args) {
        Main m = new Main();
        
        //A.1
        System.out.println(m.product(6, 11));
        System.out.println(m.countdown(5, 0));
        System.out.println(m.sequence(1, 14));
        
        //A.2
        System.out.println(m.insuranceRate(3, 3000));
    }

    //A.1.1 Produkt
    public int product(int n1, int n2) {
        int result = 1;
        for (int i = n1; i <= n2; i++) {
            result *= i;
        }

        return result;
    }

    //A.1.2 Countdown
    public String countdown(int n1, int n2) {
        String result = "";
        for (int i = n1; i >= n2; i--) {
            result += i;
            if (i != n2) {
                result += " ";
            }
        }

        return result;
    }

    //A.1.3 Zahlenfolge
    public String sequence(int n1, int n2) {
        int i = 0;
        if (n1 % 2 == 0) {
            i = n1 + 1;
        } else {
            i = n1;
        }

        String result = "";
        for (; i <= n2; i += 2) {
            result += i;
            if (i < n2 - 1) {
                result += " ";
            }
        }

        return result;
    }

    //A.2
    public double insuranceRate(int numberOfCrashes, int valueOfCar) {
        int fixed = 0;
        switch (numberOfCrashes) {
            case 1:
                fixed = 33;
                break;
            case 2:
                fixed = 64;
                break;
            case 3:
                fixed = 85;
                break;
            case 4:
                fixed = 112;
                break;
        }
        
        return fixed + M * valueOfCar;
    }
}