package extispyb.logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * LoggerFormatter makes uniform the logs so software can be created to parse
 * them automatically
 *
 */
public class LoggerFormatter {

	public enum Type {
		START, END, ERROR
	}

	public enum Package {
		BIOSAXS_WS, BIOSAXS_WS_ERROR,

		BIOSAXS_UI, BIOSAXS_UI_ERROR,

		BIOSAXS_DB, BIOSAXS_MOBILE,

		CRIMS_WS_ERROR, CRIMS_WS, BIOSAXS_WORKFLOW_ERROR, BIOSAXS_WORKFLOW, EXTISPyB, EXTISPyB_ERROR
	}

	private static void log(Logger log, Package pack, String methodName,
			Type type, long id, long time, long duration, String params,
			String comments) {
		HashMap<String, String> message = new HashMap<String, String>();
		message.put("PACKAGE", pack.toString());
		message.put("METHOD", methodName);
		message.put("TYPE", type.toString());
		message.put("ID", String.valueOf(id));
		message.put("DURATION", String.valueOf(duration));
		message.put("PARAMS", String.valueOf(params));
		message.put("COMMENTS", String.valueOf(comments));
		message.put("LOGGER", "v2.0");
		message.put("DATE", GregorianCalendar.getInstance().getTime()
				.toString());
		log.info(new Gson().toJson(message));

	}

	public static void log(Logger log, Package pack, String methodName,
			long id, long time, String params) {
		LoggerFormatter.log(log, pack, methodName, Type.START, id, time, -1,
				params, "");

	}

	public static void log(Logger log, Package pack, String methodName,
			long id, long time, long duration) {
		LoggerFormatter.log(log, pack, methodName, Type.END, id, time,
				duration, "", "");
	}

	public static void log(Logger log, Package pack, String methodName,
			long id, long time, String cause, Exception error) {
		// StringBuilder sb = new StringBuilder();
		// for (StackTraceElement element : error.getStackTrace()) {
		// sb.append(element.toString());
		// sb.append("\n");
		// }
		LoggerFormatter.log(log, pack, methodName, Type.ERROR, id, time, -1,
				cause, Arrays.toString(error.getStackTrace()).toString());
	}

	public static void log(Logger log, Package pack, String methodName,
			String text) {
		List<String> message = new ArrayList<String>();
		message.add(pack.toString());
		message.add(methodName);
		message.add(text);
		log.info(message);
	}

}
