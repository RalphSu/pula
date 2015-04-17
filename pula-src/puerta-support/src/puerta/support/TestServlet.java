package puerta.support;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8476906175334820075L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, String[]> params = req.getParameterMap();
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			int c = 0;
			for (String nk : values) {
				c++;
				System.out.println(key + "[" + c + "] = " + nk);
			}
		}
	}

}
