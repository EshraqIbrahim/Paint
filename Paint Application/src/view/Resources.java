package view;

import java.io.InputStream;

public class Resources {
	public static InputStream resourceLoader(String path) {
		InputStream input = Resources.class.getResourceAsStream(path);
		if(input == null) {
			input = Resources.class.getResourceAsStream("/"+path);
		}
		return input;
	}
}
