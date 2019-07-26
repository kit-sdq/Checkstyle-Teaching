enum PublicTest implements Test {
	PRINT {
		@Override
		public void run() {
			printInputAndValue(
					createTuple(1, 2, 3, 4, 5),
					createTuple(1),
					createTuple()
			);
		}
	},

	CREATE {
		@Override
		public void run() {
			printInputAndValue(
					createTuple(0, 1, 2, 3, 4, 5),
					createTuple(-1, 1, 2, 3, 4, 5)
			);
		}
	};

	@Override
	public String getFileName() {
		return name().toLowerCase();
	}

	@Override
	public String getDirName() {
		return "public";
	}
}
