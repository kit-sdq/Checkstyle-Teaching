import java.util.Iterator;

public enum FunctionsTest implements Test {
	CREATE {
		@Override
		public void run() {
			printInputAndValue(
					createTuple(),
					createTuple(0),
					createTuple(-1, 3),
					createTuple(3, -1),
					createTuple(-1, 0, -25, 3, -2),
					createTuple(25, 5325, 25, -32532, 0, 2, 45, -1)
			);
		}
	},
	MIN {
		@Override
		public void run() {
			printInputAndValue(
					createTuple(1, 2, 3, 4, 5).min(),
					createTuple().min(),
					createTuple(2, 1, 1, 3, 4, 5, 5).min(),
					createTuple(235, 2134, 5364, 3, 22356).min()
			);
		}
	},
	MAX {
		@Override
		public void run() {
			printInputAndValue(
					createTuple(1, 2, 3, 4, 5).max(),
					createTuple(1, 2, 3, 4, 5, Integer.MAX_VALUE).max(),
					createTuple().max(),
					createTuple(2, 1, 1, 3, 4, 5, 5).max(),
					createTuple(235, 2134, 5364, 3, 22356).max()
			);
		}
	},
	INSERT {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple();
			printInputAndValue(
					tuple = tuple.insert(1),
					tuple = tuple.insert(2),
					tuple = tuple.insert(3),
					tuple = tuple.insert(4),
					tuple = tuple.insert(5),
					tuple = tuple.insert(0),
					tuple.insert(-1)
			);
		}
	},
	REMOVE {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple(1, 2, 3, 2, 4, 5);
			printInputAndValue(
					tuple = tuple.remove(1),
					tuple = tuple.remove(1),
					tuple = tuple.remove(0),
					tuple = tuple.remove(-1),
					tuple = tuple.remove(6),
					tuple = tuple.remove(2),
					tuple = tuple.remove(4),
					tuple = tuple.remove(3),
					tuple = tuple.remove(5),
					tuple.remove(5)
			);
		}
	},
	INDEXOF {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple(1, 2, 3, 2, 4, 5, 5);
			printInputAndValue(
					tuple.indexOf(1),
					tuple.indexOf(2),
					tuple.indexOf(3),
					tuple.indexOf(4),
					tuple.indexOf(5),
					tuple.indexOf(6),
					tuple.indexOf(0),
					tuple.indexOf(-1),
					createTuple().indexOf(1)
			);
		}
	},
	COUNTNUMBERS {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple(1, 2, 3, 2, 4, 2, 5, 5);
			printInputAndValue(
					tuple.countNumbers(1),
					tuple.countNumbers(2),
					tuple.countNumbers(3),
					tuple.countNumbers(4),
					tuple.countNumbers(5),
					tuple.countNumbers(6),
					tuple.countNumbers(0),
					tuple.countNumbers(-1),
					createTuple().countNumbers(1)
			);
		}
	},
	SWAP {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple(1, 2, 3, 4, 5, 5);
			printInputAndValue(
					tuple = tuple.swap(4, 5),
					tuple = tuple.swap(0, 4),
					tuple = tuple.swap(1, 3),
					tuple = tuple.swap(2, 2),
					tuple = tuple.swap(-1, 0),
					tuple = tuple.swap(0, -1),
					tuple = tuple.swap(6, 0),
					tuple = tuple.swap(0, 6),
					tuple.swap(-1, 6)
			);
		}
	},
	TOSET {
		@Override
		public void run() {
			printInputAndValue(
					createTuple(1, 2, 3, 4, 5).toSet(),
					createTuple(1, 2, 3, 4, 5, 5).toSet(),
					createTuple(1, 1, 1, 1, 1).toSet(),
					createTuple(5, 4, 3, 2, 1).toSet(),
					createTuple(5, 4, 3, 5, 2, 1).toSet(),
					createTuple(5, 4, 2, 3, 9, 2, 1, 5, 6, 6).toSet(),
					createTuple().toSet()
			);
		}
	},
	EQUALS {
		@Override
		public void run() {
			NaturalNumberTuple ascending = createTuple(1, 2, 3, 4, 5);
			NaturalNumberTuple twoFives = createTuple(1, 2, 3, 4, 5, 5);
			printInputAndValue(
					ascending.equals(createTuple(1, 2, 3, 4, 5)),
					ascending.equals(ascending),
					twoFives.equals(ascending),
					createTuple().equals(createTuple()),
					createTuple().equals(twoFives),
					ascending.equals(createTuple(5, 4, 3, 2, 1))
			);
		}
	};

	@Override
	public String getDirName() {
		return "functions";
	}

	@Override
	public String getFileName() {
		return name().toLowerCase();
	}
}
