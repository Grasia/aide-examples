These are examples developed with AIDE framework. To know more of AIDE, please, visit http://grasia.fdi.ucm.es/aide

The examples are:

- fallingAssistanceTrackedWithNormDefinition. A norm based monitoring of a simulation. The example is used to prototype long term observation of a simulation through a set of deontic norms. 
- smarttvandremote. A sample development of a remote control for a smart tv. The remote control operation is affected by the tremors of the patient. 

### REQUIREMENTS:

- Java 1.7 (set variable JAVA_HOME)
- Maven 3.1.1+ installed, see http://maven.apache.org/download.html (set variable M2_HOME)
- Ant (set variable ANT_HOME)
- Android SDK (r21.1 or later, latest is best supported) installed, preferably with all platforms, see http://developer.android.com/sdk/index.html
- Make sure you have images for API 19 installed in your android SDK. It is required to have the IntelAtomx86 image to permit hardware acceleration. Instructions for Android are available in the [Android site](http://developer.android.com/tools/devices/emulator.html#acceleration)
- Set environment variable ANDROID_HOME to the path of your installed Android SDK and add $ANDROID_HOME/tools as well as $ANDROID_HOME/platform-tools to your $PATH. (or on Windows %ANDROID_HOME%\tools and %ANDROID_HOME%\platform-tools).
- Add binaries to environment variable PHAT (PATH=$PATH:$HOME/bin:$JAVA_HOME/bin:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$M2_HOME/bin)
