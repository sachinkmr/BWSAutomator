package sachin.bws.helpers;

import java.util.Map;

/**
 *
 * @author Sachin
 */
public class Config {
	public static final String BROWSER_TYPE;
	public static final int TIMEOUT;
//	public static String step;
	static {
		Map<String, String> map=new ExcelManager().readConfigData();
		BROWSER_TYPE=map.get("Browser");
		TIMEOUT=Integer.parseInt(map.get("Timeout"));
	}
}
