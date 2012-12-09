Vanilla Player with Gestures
===========
This is my mod of Vanilla, the open source music player for android.
It embraces minimalistic, no-controls design with gesture manipulation.
Also features non-ISO-8859-1 1-byte encodings support and a few useful tweaks/bugfixes.

The rest of this README.md file is from the original vanilla player readme

Building
========
To build you will need:

 * A Java compiler compatible with Java 1.6
 * The Android SDK with platform 16 (JellyBean) installed

Building from command-line
--------------------------
 * `android update project --path .` to generate local.properties
 * `ant debug` to build the APK at bin/VanillaMusic-debug.apk
 * Optional: `ant installd` to install the APK to a connected device

Building from Eclipse
---------------------
You can also build from Eclipse. Create a new Android Project, choosing "Create
project from exisiting source", then set the compiler compliance level to 1.6
in project settings.

Documentation
=============
Javadocs can be generated using `ant doc`


  [1]: http://crowdin.net/project/vanilla-music/invite

