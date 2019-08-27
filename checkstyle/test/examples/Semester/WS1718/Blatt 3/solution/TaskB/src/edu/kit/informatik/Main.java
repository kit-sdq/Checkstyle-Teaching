package edu.kit.informatik;

public class Main {
    public static void main(String[] args) {
        int[] turns = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            turns[i] = Integer.parseInt(args[i]);
        }
        
        System.out.println(new TicTacToe().simulateGame(turns));
    }
}
