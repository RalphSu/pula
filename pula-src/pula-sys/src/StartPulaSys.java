public class StartPulaSys {

	public static void main(String[] args) {

		String mapping = "/pula-sys";
		int port = 8125;

		T55Starter.startServer(StartPulaSys.class, mapping, port);

	}

}
