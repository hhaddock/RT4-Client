## Goals

* Identify all classes
  * Create new static classes by grouping related members
* Identify all methods
* Identify all fields
* Identify all local variables
* Remove any remaining obfuscation (possibly none left)
* Fix poor decompiler behavior (fernflower)
* Replace magic numbers and bitmasks with named final fields
* Refactor code to improve behavior/readability
* Modernize code/libraries (High DPI support, modern refresh rates, ...)
* Support existing servers via global config flags that adjust packet behaviors
* Organize classes into packages

OpenRS2 annotations are left in the source to build a deob map from, in case some of my changes aren't desirable.  
That mapping can be used to generate a new deob with everything renamed for you.

## Instructions

Build requirements:
* Java 8+

Runtime requirements:
* SD: Java 8+
* HD on Windows, use Java 15 or lower. There is a JOGL issue on 16+ related to how they grab the WGL context from the window.
* HD on Linux: Java 8+
* HD on macOS: Not possible yet on latest macOS. Might work for earlier OS versions.

```
git clone https://github.com/Pazaz/RT4-Client.git
cd RT4-Client
./gradlew run
```

### Build a shareable client

From the RT4 client root, build the self-contained ZIP with Java 11 on Windows:

```powershell
$env:JAVA_HOME = 'C:\Path\To\Java11'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat assembleShareable
```

The artifact is written to `client/build/distributions/2009scape-client-1.0.0.zip`.
It contains the executable JAR, the compiled plugins, `config.json`,
`config.example.json`, and `run-client.bat`/`run-client.sh` launchers. If a local
`config.json` exists, it is used; otherwise the example is staged as the initial
configuration. Use `-PclientConfig=/path/to/config.json` to choose explicitly.

The bundle includes the `BasicInputQOL` plugin, which provides middle-button camera
dragging and mouse-wheel zoom without requiring Shift.

### Build a native installer with a bundled JRE

The native package uses `jlink` to create a trimmed Java 11 runtime and `jpackage`
to create an OS-native application image or installer. Java 11 is intentional: it
avoids the Java 8 `ByteBuffer` runtime failure and remains compatible with the
Windows HD JOGL limitation.

Build an application image on Windows with JDK 11 for the runtime and JDK 17 for
the packaging tool:

```powershell
$env:RUNTIME_JAVA_HOME = 'C:\Users\hayde\.jdks\corretto-11.0.21'
$env:JPACKAGE_JAVA_HOME = 'C:\Users\hayde\.jdks\corretto-17.0.13'
.\gradlew.bat assembleInstaller
```

The result is written to `client/build/jpackage/installers`. The default
`app-image` is directly runnable and includes the JRE. It stages a working
`config.json` beside the client JAR, resolving the bundled plugins correctly when
the EXE is opened from Explorer. If a local `config.json` exists, it is packaged;
otherwise use the example and override it with `-PclientConfig=/path/to/config.json`.
To build a Windows EXE installer, use `-PjpackageType=exe`; WiX must be installed
and available to `jpackage`.

Native installers are platform-specific: build Windows packages on Windows,
Linux packages on Linux, and macOS packages on macOS. Use `deb`, `rpm`, `pkg`, or
`dmg` as supported by the target OS and its packaging tools.

Platform icons are optional and belong in `client/packaging/icons/` as
`2009scape.ico`, `2009scape.icns`, or `2009scape.png`. A custom icon can also be
passed with `-PjpackageIcon=/path/to/icon`. For example, from the RT4 client root:

```powershell
.\gradlew.bat assembleInstaller -PjpackageIcon="$PWD\Dragon_dagger(p)_detail.ico"
```

Use `-PjpackageConsole=true` for a diagnostic build that leaves the Windows
console visible when the client exits. The native package includes the selected
configuration and compiled plugins.

You will be connected to a test server automatically.  
This server is provided by 2009scape for their own internal developments.

## Deviations

Configurable:
- Packet behaviors to make it compatible with existing servers
- View distance in HD
- Bilinear map filtering in HD/SD
- Tweening enabled by default (existed in client)
- Shift-click behavior on inventory items enabled by default (existed in client)

Unconfigurable:
- JOGL was updated to 2.4.0
- Update/render loop was decoupled to tick indepedently from each other
- Camera panning input rewritten to use render loop timing
- Varp array size was extended to 3500 instead of 2500
- Mouse wheel camera movement (click middle button and move mouse)
- Render FPS is set to your monitor's refresh rate

## Libraries Used

- JOGL/Gluegen 2.4.0rc
- Google Gson 2.9.0
