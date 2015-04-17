package puerta.system.keeper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegular {
	public static void main(String[] args) {
		String inputStr = "/test/str";
		String patternStr = "/test/.*";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		boolean matchFound = matcher.find();
		while (matchFound) {
			System.out.println(matcher.start() + "-" + matcher.end());
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String groupStr = matcher.group(i);
				System.out.println(i + ":" + groupStr);
			}
			if (matcher.end() + 1 <= inputStr.length()) {
				matchFound = matcher.find(matcher.end());
			} else {
				break;
			}
		}
	}
}
