import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.Property;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;


@RunWith(JUnitQuickcheck.class)
public class TestSorting {
    @Property(trials = 1000) public void checkSorting(int[] array) throws Exception {
        NaturalNumberTuple tuple = new NaturalNumberTuple(new int[] {1});
        Method method = tuple.getClass().getDeclaredMethod("sort", int[].class);
        method.setAccessible(true);
        method.invoke(tuple, array);

        for (int i = 0; i < array.length; i++) {
            if (i != 0 && array[i - 1] > array[i]) {
                fail();
            }
        }
    }

    @Property(trials = 100000000) public void checkToSet(@From(MultipleValueArrayGenerator.class) int[] array) throws Exception {
        NaturalNumberTuple tuple = new NaturalNumberTuple(array);
        NaturalNumberTuple set = tuple.toSet();

        Field setField = set.getClass().getDeclaredField("tuple");
        setField.setAccessible(true);
        int[] setArray = (int[]) setField.get(set);

        Field tupleField = tuple.getClass().getDeclaredField("tuple");
        tupleField.setAccessible(true);
        int[] tupleArray = (int[]) tupleField.get(tuple);

        Integer[] tupleArrayBoxed = new Integer[tupleArray.length];
        for (int i = 0; i < tupleArray.length; i++) {
            tupleArrayBoxed[i] = tupleArray[i];
        }

        List<Integer> list = Arrays.asList(tupleArrayBoxed);
        Integer[] correctSetArray = new TreeSet<>(list).toArray(new Integer[0]);
        
        int[] correctSetArrayUnboxed = new int[correctSetArray.length];
        for (int i = 0; i < correctSetArray.length; i++) {
            correctSetArrayUnboxed[i] = correctSetArray[i];
        }

        NaturalNumberTuple correctSet = new NaturalNumberTuple(correctSetArrayUnboxed);

        assertEquals(correctSetArrayUnboxed.length, setArray.length);
        assertEquals(correctSet.toString(), set.toString());
    }
}