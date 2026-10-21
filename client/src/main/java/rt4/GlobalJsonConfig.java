package rt4;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;

public class GlobalJsonConfig {
	public static GlobalJsonConfig instance = null;
	private File configDirectory = new File(".").getAbsoluteFile();

	public static void load(String path) {
		Gson gson = new Gson();
		File configFile = resolveConfigFile(path);

		try (FileReader reader = new FileReader(configFile)) {
			instance = gson.fromJson(reader, GlobalJsonConfig.class);
			if (instance == null) {
				instance = new GlobalJsonConfig();
			}
			instance.configDirectory = configFile.getAbsoluteFile().getParentFile();
		} catch (Exception ex) {
			System.err.println("No config.json file, using defaults");
			instance = new GlobalJsonConfig();
			instance.configDirectory = codeSourceDirectory();
		}
	}

	private static File resolveConfigFile(String path) {
		File requested = new File(path);
		if (requested.isFile()) {
			return requested;
		}

		File besideCode = new File(codeSourceDirectory(), path);
		return besideCode.isFile() ? besideCode : requested;
	}

	private static File codeSourceDirectory() {
		try {
			File location = new File(GlobalJsonConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			return location.isFile() ? location.getParentFile() : location;
		} catch (URISyntaxException | RuntimeException ex) {
			return new File(".").getAbsoluteFile();
		}
	}

	public File resolvePath(String path) {
		File configuredPath = new File(path);
		return configuredPath.isAbsolute() ? configuredPath : new File(configDirectory, path);
	}

	// ----

	String ip_management;
	String ip_address;
	int world;
	int server_port;
	int wl_port;
	int js5_port;
	boolean mouseWheelZoom = GlobalConfig.MOUSEWHEEL_ZOOM;
	public String pluginsFolder = "plugins";
}
