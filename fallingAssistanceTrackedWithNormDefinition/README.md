PHAT Framework is a set of tools (coded using Java) to model and simulate activities of daily living.
The main components are:

- **SociAALML Editor** is a graphical editor to model the elements for the simulation.
- **PHAT Simulator** is a simulator developed from scratch using jMonkeyEngine.
- **PHAT Generator** is a tool that transforms the model defined with SociAALML in java code. The code extends PHAT Simulator and can be simulated.


This example shows how to perform a norm based monitoring of a simulation. Inspected norms are Obligations (must do) and Prohibitions (must not do)

- AssistanceIsGivenInCaseFall. The caregiver must attend the caretaker when the caretaker falls.
- AssistWhilePatientDrinks. The caregiver must assist the caretaker when the caretaker is drinking, just in case the caretaker cannot swallow liquids properly.
- MustNotWashDishes. The caregiver must not be washing dishes while the patient is falling.

The simulated activities are the following. When the patient has high tremors:
- the patient goes to the kitchen
- the caretaker goes to wash the dishes
- the patient sits down and drinks. It takes longer than usual to drink.
- the patient goes to the hall
- the patient sits down at the sofa
- the patient falls and loses conscience. After a while, asks for help.
- the caregiver comes to aid
- the caregiver returns to washing
- the caretaker returns to the kitchen and the actions start over again.

When the patient has low tremors:
- the patient goes to the kitchen
- the caretaker goes to wash the dishes
- the patient drinks normally
- the patient goes to the hall
- the patient sits down at the sofa
- the patient falls and loses conscience. After a while, asks for help.
- the caregiver comes to aid
- the caregiver returns to washing
- the caretaker returns to the kitchen and the actions start over again.

While running these activities, the system can evaluate to what extent the norms are being violated. As time goes by, violations are accounted as well as for how long the norm remained invalid. 

The monitoring assumes no perfect execution is possible. The worst execution is the one that has no assistance on behalf AAL solutions. Solutions for assisting the patient will make the To prevent norms being violated or reduce the number of times they are violated or the duration of the invalid status.

However, the different solutions to issue alarms when the caretaker spends too much time while drinking or when falling down, will obtain less negative scores 

### Instructions

The demonstration has three parts: launching the JADE platform, launching the monitoring solution, and launching the simulation.

Whenever the simulation changes, it is convenient to stop and relaunch the simulation. However, changes in the monitored norms, do not require to relaunch the simulation. It may be kept running while the monitoring solution is restarted.

Before anything, it is convenient to recompile everything

**$ mvn clean compile**

Now, the startup sequence can commence. To launch the JADE platform

	**$ sh startPlatform.sh**

when a GUI appears, the platform is considered to be started.  Then, start the monitoring solution:

	**$ sh startMonitoring.sh**

And, now the two different simulations can be launched. For the low tremors scenario

	**$ ant runSimLowTremorsNoDevices**

If a high tremors is expected, launch instead:

	**$ ant runSimHighTremorsNoDevices**

The simulation itself can be stopped and launched, but it may require to relaunch the monitoring too, to prevent faulty norm detection.


### REQUIREMENTS:

- Java 1.7 (set variable JAVA_HOME)
- Maven 3.1.1+ installed, see http://maven.apache.org/download.html (set variable M2_HOME)
- Ant (set variable ANT_HOME)
- Android SDK (r21.1 or later, latest is best supported) installed, preferably with all platforms, see http://developer.android.com/sdk/index.html
- Make sure you have images for API 19 installed in your android SDK. It is required to have the IntelAtomx86 image to permit hardware acceleration. Instructions for Android are available in the [Android site](http://developer.android.com/tools/devices/emulator.html#acceleration)
- Set environment variable ANDROID_HOME to the path of your installed Android SDK and add $ANDROID_HOME/tools as well as $ANDROID_HOME/platform-tools to your $PATH. (or on Windows %ANDROID_HOME%\tools and %ANDROID_HOME%\platform-tools).
- Add binaries to environment variable PHAT (PATH=$PATH:$HOME/bin:$JAVA_HOME/bin:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$M2_HOME/bin)
