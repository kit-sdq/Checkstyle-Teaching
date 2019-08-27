public enum InteractionTest implements Test {
	MODIFYMINMAX {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple(1, 2, 3, 4, 5);
			printInputAndValue(
					tuple.min(),
					tuple.max(),
					tuple = tuple.insert(10).remove(1),
					tuple.min(),
					tuple.max(),
					tuple = tuple.insert(7).remove(4),
					tuple.min(),
					tuple.max(),
					tuple = tuple.insert(Integer.MAX_VALUE).insert(1),
					tuple.min(),
					tuple.max()
			);
		}
	},
	SWAPMINMAX {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple(1, 2, 3, 4, 5);
			printInputAndValue(
					tuple.min(),
					tuple.max(),
					tuple = tuple.swap(0, 4),
					tuple.min(),
					tuple.max(),
					tuple = tuple.swap(1, 3),
					tuple.min(),
					tuple.max()
			);
		}
	},
	INDEXOFSWAP {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple(1, 2, 3, 4, 5);
			printInputAndValue(
					tuple.indexOf(2),
					tuple.indexOf(4),
					tuple = tuple.swap(0, 4),
					tuple.indexOf(2),
					tuple.indexOf(4),
					tuple = tuple.swap(1, 3),
					tuple.indexOf(2),
					tuple.indexOf(4)
			);
		}
	},
	EQUALS {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple(1, 2, 3, 4, 5);
			NaturalNumberTuple origin = createTuple(1, 2, 3, 4, 5);
			printInputAndValue(
					tuple.equals(origin),
					tuple = tuple.swap(0, 4),
					tuple.equals(origin),
					tuple = tuple.swap(0, 4),
					tuple.equals(origin),
					tuple = tuple.insert(1),
					tuple.equals(origin),
					tuple = tuple.remove(1),
					tuple.equals(origin),
					tuple = tuple.insert(1),
					tuple.equals(origin)
			);
		}
	},
	CHANGE {
		@Override
		public void run() {
			NaturalNumberTuple tuple = createTuple(1, 2, 3, 2, 4, 5);
			printInputAndValue(
					tuple.insert(2),
					tuple,
					tuple.remove(3),
					tuple,
					tuple.swap(0, 1),
					tuple,
					tuple.toSet(),
					tuple
			);
		}
	};

	@Override
	public String getDirName() {
		return "interaction";
	}

	@Override
	public String getFileName() {
		return name().toLowerCase();
	}
}