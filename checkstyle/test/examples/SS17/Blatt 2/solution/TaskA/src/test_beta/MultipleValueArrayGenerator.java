import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleValueArrayGenerator extends Generator {


    public MultipleValueArrayGenerator() {
        super(int[].class);
    }

    @Override
    public int[] generate(SourceOfRandomness r, GenerationStatus status) {
        int length = Math.abs(r.nextInt(101));
        int[] array = new int[length];

        for (int i = 0; i < array.length; i++) {
            int numberOfRepeats = r.nextInt(1, 3);
            int number = r.nextInt(-100, 100);
            for (int j = 0; j < numberOfRepeats && i + j < array.length; j++) {
                array[i] = number;
                i++;
            }
            i--;
        }

        List<Integer> arrayAsList = new ArrayList<>();
        for (int current : array) {
            arrayAsList.add(current);
        }
        Collections.shuffle(arrayAsList);

        for (int i = 0; i < arrayAsList.size(); i++) {
            array[i] = arrayAsList.get(i);
        }

        return array;
    }
}