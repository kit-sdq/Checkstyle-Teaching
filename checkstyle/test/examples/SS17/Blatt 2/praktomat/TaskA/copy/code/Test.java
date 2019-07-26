import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Collectors;

public interface Test {
	void run();

	default NaturalNumberTuple createTuple(int... array) {
		return new NaturalNumberTuple(array);
	}

	default Iterator<String> readExpected() {
		Path path = Paths.get("../copy/expected/" + getDirName() + "/" + getFileName() + ".txt");
		try {
			return Files.lines(path).collect(Collectors.toList()).iterator();
		} catch (IOException e) {
			throw new IllegalStateException("I/O error while reading file " + getFileName() + ".txt! Test can't run.");
		}
	}

	default void printInputAndValue(NaturalNumberTuple... tuples) {
		Iterator<String> expectedIterator = readExpected();
		for (NaturalNumberTuple tuple : tuples) {
			printNextInputs(expectedIterator);
			tuple.print();
		}
	}

	default void printInputAndValue(Object... values) {
		Iterator<String> expectedIterator = readExpected();
		for (Object value : values) {
			printNextInputs(expectedIterator);
			if (value.getClass().equals(NaturalNumberTuple.class)) {
				((NaturalNumberTuple) value).print();
			} else {
				System.out.println(value);
			}
		}
	}

	default void printNextInputs(Iterator<String> iterator) {
		while (iterator.hasNext()) {
			String input = iterator.next();
			if (input.contains("$>") || input.contains("\n---")) {
				System.out.println(input);
			} else {
				break;
			}
		}
	}

	String getDirName();

	String getFileName();
}
