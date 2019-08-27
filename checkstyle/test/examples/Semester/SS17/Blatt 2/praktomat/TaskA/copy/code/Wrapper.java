public class Wrapper {
	public static void main(String[] args) {
		switch(args[0]) {
			case "PUBLIC":
				PublicTest.valueOf(args[1]).run();
				break;
			case "FUNCTIONS":
				FunctionsTest.valueOf(args[1]).run();
				break;
			case "INTERACTION":
				InteractionTest.valueOf(args[1]).run();
				break;
			default:
				System.out.println("Test misconfigured.");
		}
	}
}
