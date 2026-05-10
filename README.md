# CrewComms

CrewComms is a local-first crew communication system with a phone hub and Wear OS wrist controller.

Architecture (v1):
- Watch app communicates only with paired phone via Wear OS Data Layer.
- Phone app communicates with nearby phones via `CrewTransport`.
- `MockTransport` is fully implemented for local UI/dev testing.
- `LocalLanTransport` is fully implemented for serverless local peer communication over the same LAN/Wi-Fi network.
- `NearbyConnectionsTransport` remains in the codebase as optional/swap-ready implementation.

## Modules

- `app-phone`: Android phone app (`com.crewcomms.phone`)
- `app-watch`: Wear OS app (`com.crewcomms.watch`)
- `core-model`: shared models
- `core-transport`: transport abstraction + mock + nearby implementation
- `core-database`: Room database for messages/members
- `core-common`: shared JSON/time/result/utility constants

## Tech Stack

- Kotlin
- Jetpack Compose (phone)
- Wear Compose (watch)
- Material 3 (phone)
- MVVM + Coroutines + Flow
- Room
- DataStore
- Hilt
- Google Play Services Wearable Data Layer
- Local LAN UDP sockets (default real transport)
- Google Nearby Connections (optional, swap-ready)

## Open In Android Studio

1. Open Android Studio (latest stable recommended).
2. `File -> Open` and select this repository root.
3. Let Gradle sync.

Notes:
- The repo includes `gradlew` scripts and `gradle-wrapper.properties`.
- If `gradle/wrapper/gradle-wrapper.jar` is missing in your clone, run `gradle wrapper` once from a machine with Gradle installed.

## Run Phone App

1. Select run configuration: `app-phone`.
2. Run on an Android 12+ device/emulator.
3. Grant runtime permissions when prompted:
- Bluetooth scan/connect/advertise
- Location
- Nearby Wi-Fi devices (Android 13+)
- Notifications
- Audio (placeholder for PTT)

## Run Watch App (Wear OS Emulator or Galaxy Watch 6)

1. Select run configuration: `app-watch`.
2. Deploy to Wear OS emulator or Galaxy Watch 6.
3. Ensure phone and watch are paired in Wear OS.

### Enable Developer Mode on Galaxy Watch 6

1. On watch: `Settings -> About watch -> Software information`.
2. Tap `Software version` repeatedly until Developer mode is enabled.
3. In Developer options, enable:
- ADB debugging
- Debug over Wi-Fi (if using wireless deployment)

## Test Phone ↔ Watch Communication

1. Start both `app-phone` and `app-watch`.
2. In phone app, create or join a crew.
3. Phone sends compact `WatchState` updates to watch at Data Layer path `/watch_state`.
4. On watch, open Crew Controls and tap commands (`PING`, `HELP`, `READY`, `COME`, `WAIT`).
5. Watch sends `WatchCommand` to phone via Data Layer path `/watch_command`.
6. Phone converts command into `CrewMessage`, saves locally, then relays through current transport.

## Test Mock Mode

1. Open phone Settings.
2. Enable `Use mock transport mode`.
3. Create crew or scan/join flow.
4. `MockTransport` will:
- emit fake nearby crew discovery
- emit fake member join
- echo outbound messages after short delay

This mode is best for emulator-only development.

## Test Local Free Mode (No Server) With Two Android Phones

1. Install `app-phone` on both phones.
2. Ensure both phones are on the same Wi-Fi/LAN and disable mock mode.
3. On host phone, tap `Create Crew`.
4. On second phone, tap `Join Nearby Crew`, then `Scan the Horizon`.
4. Join discovered crew.
5. Exchange text and quick commands.

Important:
- Local discovery and messaging depend on devices sharing the same network segment and broadcast being allowed.
- Keep both apps in foreground for v1 testing.

## Permissions Summary

Phone app requests:
- `BLUETOOTH_SCAN`
- `BLUETOOTH_CONNECT`
- `BLUETOOTH_ADVERTISE`
- `ACCESS_FINE_LOCATION`
- `NEARBY_WIFI_DEVICES`
- `POST_NOTIFICATIONS`
- `RECORD_AUDIO` (placeholder flow)
- `FOREGROUND_SERVICE`

Watch app requests:
- `VIBRATE`

`BODY_SENSORS` is intentionally not requested in v1.

## Current Limitations

- LAN transport currently uses UDP broadcast and simple room filtering (good for local crews, not hardened internet routing).
- PIN protection is modeled in domain state but full cryptographic PIN handshake is TODO.
- Voice is UI placeholder only (no live audio streaming yet).
- Foreground service currently shows active session notification only; it does not yet host full resilient background networking logic.
- No backend/cloud/account system by design.

## What Remains To Finish Optional Google Nearby Mode

To move from the existing Nearby skeleton to production-grade Google Nearby:

1. Advertise/discovery metadata handshake:
- exchange room metadata (`roomId`, `roomName`, `pinRequired`) in explicit announcement payloads.

2. PIN gate:
- implement authenticated request/response payload before adding endpoint to active channel.

3. Secure message channel:
- add message signing/encryption key exchange (E2E TODO).

4. Presence robustness:
- heartbeat/timeout and reconnection policy for endpoint churn.

5. Foreground-connected behavior:
- keep transport in a lifecycle-aware service when active session is running.

## Roadmap

### v1
- Text messages
- Quick commands
- Watch command panel
- Nearby room discovery
- Mock transport

### v1.5
- Voice notes
- Better reconnect
- Better member presence
- Message reactions

### v2
- Real push-to-talk streaming
- Strong end-to-end encryption
- Optional internet fallback
- Bluetooth mesh mode
- Offline map/event mode
- Admin/event channels
