package edu.kit.informatik;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EfficientRecurser {
    private final int a;
    private final int b;
    private int[] sequence;

    public EfficientRecurser(int n, int a, int b) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        
        this.a = a;
        this.b = b;
        this.sequence = new int[n + 1];

        for (int i = 0; i <= (n > 2 ? 2 : n); i++) {
            sequence[i] = 1;
        }
        
        if (n <= 2) {
            return;
        }

        for (int i = 3; i <= n; i++) {
            sequence[i] = recurse(i);
        }
    }

    @Override
    public String toString() {
        return Arrays.stream(sequence).mapToObj(Integer::toString).collect(Collectors.joining(","));
    }

    private int recurse(int position) {
        if (sequence[position] != 0) {
            return sequence[position];
        }

        for (int i = 2; i <= 3; i++) {
            // There can never be a result that is 0, so 0 in the array means "missing value"
            if (sequence[position - i] == 0) {
                sequence[position - i] = recurse(position - i);
            }
        }

        return a * sequence[position - 2] + b * sequence[position - 3];
    }
}
