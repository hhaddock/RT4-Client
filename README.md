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
It contains the executable JAR, the compiled plugins, `config.example.json`, and
`run-client.bat`/`run-client.sh` launchers. The first launch copies the example
configuration to `config.json`; edit that file with the server hostname and ports.

The bundle includes the `BasicInputQOL` plugin, which provides middle-button camera
dragging and mouse-wheel zoom without requiring Shift.

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
