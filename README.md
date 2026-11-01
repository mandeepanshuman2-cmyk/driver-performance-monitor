# Driver Performance Monitoring System

A student project combining rule-based driving performance scoring with a webcam-based driver monitoring demonstration. Java handles console scoring and a local HTTP server; JavaScript and MediaPipe handle face landmark detection in the browser.

> Educational prototype: camera alerts indicate prolonged eye closure or sudden head movement. They do not confirm driver fatigue or actual vehicle braking.

## Features

- **Driving performance score:** Evaluate manually entered maximum speed, sudden braking events, and harsh acceleration events.
- **Performance feedback:** Display a safety rating and improvement suggestions.
- **Eye-closure alerts:** Flag estimated eye closure lasting at least 1.5 seconds.
- **Head-movement alerts:** Flag sudden head movement as a possible harsh braking event.
- **Session counters:** Track camera alerts while the page remains open.
- **Camera controls:** Start and stop the webcam from the browser.

The console and camera modules run independently. Camera events do not automatically update the console safety score.

## Technology

| Component | Technology |
| --- | --- |
| Console scoring | Java, Scanner |
| Local server | Java HttpServer |
| Camera interface | HTML, CSS, JavaScript |
| Face landmarks | MediaPipe Tasks Vision 0.10.21 |
| Windows launcher | Batch script |

## Requirements

- Java **JDK 9 or newer**, with `java` and `javac` available on PATH, for the supplied launcher and commands.
- A webcam and a browser supporting camera access, such as Chrome or Edge.
- Internet access to load the MediaPipe library, runtime files, and pretrained model.
- Local port `8080` available.

JDK 8 is also supported with the manual compilation commands described below. A Java runtime alone cannot compile the project.

## Getting Started

Select **Code → Download ZIP** on GitHub and extract the archive, or clone the repository:

```sh
git clone https://github.com/mandeepanshuman2-cmyk/driver-performance-monitor.git
cd driver-performance-monitor
```

### Camera Monitor

On Windows, double-click **StartCamera.bat** in the project folder. Alternatively, open a terminal in that folder and run:

```sh
javac --release 8 CameraMonitor.java
java CameraMonitor
```

1. Open [http://localhost:8080](http://localhost:8080) if the browser does not open automatically.
2. Select **Start Camera** and allow camera access when prompted.
3. Face the camera with adequate lighting.
4. Select **Stop Camera** to release the webcam.
5. Press **Ctrl+C** in the terminal to stop the Java server.

With JDK 8, compile using `javac CameraMonitor.java` instead.

### Console Scoring

Run these commands from the project folder:

```sh
javac --release 8 DriverPerformance.java
java DriverPerformance
```

Enter the driver's name, maximum speed in km/h, sudden braking count, and harsh acceleration count. The application displays a score, rating, and suggestions, then offers to evaluate another trip.

With JDK 8, compile using `javac DriverPerformance.java` instead.

## Scoring Rules

Each evaluation starts at **100 points**. The following demonstration penalties apply:

| Condition | Penalty |
| --- | --- |
| Maximum speed exceeds 80 km/h | 20 points, applied once |
| Each sudden braking event | 5 points |
| Each harsh acceleration event | 5 points |

The minimum score is zero.

| Score | Rating |
| --- | --- |
| 80–100 | Good |
| 50–79 | Average |
| 0–49 | Needs Improvement |

**Example:** A maximum speed of 90 km/h, two sudden braking events, and one harsh acceleration event produce `100 - 20 - 10 - 5 = 65`, rated **Average**.

These rules are illustrative. The 80 km/h threshold is not a universal legal speed limit. All console inputs are entered manually.

## Camera Detection

The Java server serves `camera.html` on localhost. The browser requests webcam access and uses a pretrained MediaPipe model to obtain face landmarks. JavaScript applies the following rules:

- **Eye closure:** The average eye aspect ratio must remain below `0.21` for at least `1500 ms` to trigger one drowsiness alert. Reopening the eyes resets the closure timer.
- **Head movement:** Nose position and face width are compared between consecutive frames. Rapid downward movement or apparent face-size growth can trigger a possible harsh braking alert. A two-second cooldown limits repeated counts.
- **Face not visible:** Temporary tracking state resets and a message asks the user to face the camera.

Stopping and restarting the camera retains the page's event counters. Reloading the page resets them.

## Project Structure

```text
driver-performance-monitor/
├── DriverPerformance.java   # Console input, scoring, and feedback
├── CameraMonitor.java       # Local HTTP server and browser launch
├── camera.html              # Camera interface and detection logic
├── StartCamera.bat          # Windows compile-and-run launcher
├── .gitignore               # Excludes compiled files and editor settings
└── README.md                # Project documentation
```

### Key Methods and Functions

| Method / Function | Responsibility |
| --- | --- |
| `readNumber()` | Validate non-negative whole-number input |
| `calculateScore()` | Apply penalties and return the driving score |
| `CameraMonitor.main()` | Read the camera page and start the local server |
| `distance()` | Calculate distance between face landmarks |
| `eyeRatio()` | Calculate eye opening relative to eye width |
| `checkEyes()` | Track closure duration and count alerts |
| `checkHeadMovement()` | Compare head measurements between frames |
| `showReport()` | Update alert messages and counters |
| `monitor()` | Process new frames and coordinate detection |
| `shutdown()` | Stop monitoring and release the webcam |

## Validation

Java compilation and synthetic detection-logic checks have passed. Real webcam behavior still requires testing on the target computer; no real-world accuracy measurement is claimed.

### Console Examples

| Maximum speed (km/h) | Sudden brakes | Harsh accelerations | Expected score | Rating |
| --- | --- | --- | --- | --- |
| 60 | 0 | 0 | 100 | Good |
| 80 | 0 | 0 | 100 | Good |
| 90 | 2 | 1 | 65 | Average |
| 70 | 10 | 1 | 45 | Needs Improvement |
| 100 | 20 | 20 | 0 | Needs Improvement |

### Camera Checks

Perform these checks while stationary:

1. Start the camera and verify that the preview appears.
2. Blink normally and check that brief blinks do not trigger an alert.
3. Close both eyes for approximately two seconds and check for one alert. Reopen and repeat to check the next event.
4. Move out of view and verify that tracking resets.
5. Make a sudden head nod or forward lean and observe the possible braking alert. Results depend on the camera setup and movement.
6. Stop the camera and verify that webcam access is released.
7. Deny camera permission and verify that an error message appears and retry remains available.

## Troubleshooting

| Issue | Suggested action |
| --- | --- |
| `javac` is not recognized | Install a JDK and add its `bin` directory to PATH |
| `--release` is not recognized | On JDK 8, compile manually without `--release 8` |
| Port 8080 is already in use | Stop the application using it, or change the port in `CameraMonitor.java` and recompile |
| Browser does not open | Open `http://localhost:8080` manually |
| Camera cannot start | Check browser permission and close other apps using the webcam |
| Model fails to load | Check internet access and whether the CDN or model URLs are blocked |
| Face tracking is unstable | Improve lighting, keep the camera steady, and face it directly |
| `camera.html` cannot be found | Run the application from the project folder |

## Privacy and Limitations

- Camera frames are processed in the browser. The application does not record or upload video; external requests load the detection library and model.
- There is no database, persistent trip history, GPS tracking, or vehicle sensor integration.
- Camera alerts use configurable heuristics. Lighting, glasses, head turns, and camera movement can cause false or missed detections.
- Head movement cannot establish actual braking. Confirmation requires vehicle speed or acceleration measurements.
- This prototype is intended for stationary classroom demonstrations and is not a validated driving safety system.

## Reference

- [MediaPipe Face Landmarker for Web — Google AI Edge](https://developers.google.com/edge/mediapipe/solutions/vision/face_landmarker/web_js)
