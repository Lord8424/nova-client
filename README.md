# Nova Client (Fabric, Minecraft 1.21.4)

Client-side utility mod with an animated click GUI. Intended for testing on your own server.

## Build
1. Install JDK 21.
2. Add the Gradle wrapper (needs Gradle 8.11+):  `gradle wrapper --gradle-version 8.12`
   (or copy `gradlew`, `gradlew.bat` and `gradle/` from https://github.com/FabricMC/fabric-example-mod)
3. `./gradlew build`  ->  `build/libs/nova-client-1.0.0.jar`
4. Drop the jar into `.minecraft/mods` together with Fabric API 0.119.2+1.21.4 and Fabric Loader >= 0.16.
   For development: `./gradlew runClient`.

## Controls
- Right Shift: open GUI (rebindable in Minecraft's Controls menu)
- Left-click module: toggle | Right-click: open settings | Middle-click: bind a key (ESC clears)
- Drag a panel header to move it, right-click a header to collapse it
- "Theme" module (Client panel): hue, saturation, rainbow, gradient spread, dim
- Settings + keybinds + panel positions are saved to `config/nova.json`

## Server notes
Flight/Speed/Step/NoFall etc. will be kicked or flagged by vanilla and anticheat checks.
On your own server set `allow-flight=true` in server.properties and turn anticheat off (or on, to test it).
