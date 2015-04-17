public class StartPulaWeb {

	public static void main(String[] args) {

		String mapping = "/pula-web";
		int port = 8127;

		T55Starter.startServer(StartPulaWeb.class, mapping, port);

	}

}
