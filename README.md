# Driver Performance Monitoring System

## Download and run

GitHub par **Code → Download ZIP** select karo aur ZIP extract karo. Windows computer par Java **JDK 9 or newer** (including `javac`) installed aur PATH mein available hona chahiye for `StartCamera.bat`. Sirf Java runtime/JRE sufficient nahi hai. Phir extracted folder mein `StartCamera.bat` double-click karo. Camera demo ke liye internet aur webcam chahiye. Java JDK 8 use kar rahe ho toh `javac CameraMonitor.java` aur `java CameraMonitor` terminal mein run karo.

## NEW: Camera demo

`StartCamera.bat` double-click karo. Browser mein `http://localhost:8080` open hoga. **Start Camera** dabao aur browser ke camera prompt mein **Allow** select karo. Stop Camera webcam release karta hai. Java server stop karne ke liye terminal mein Ctrl+C dabao. Camera kisi aur app mein busy ho toh us app ko close karo. Permission block ho toh browser ke site settings se camera allow karo.

- Internet required: MediaPipe library aur trained model download hote hain.
- Aankhein lagatar 1.5 seconds band: drowsiness alert aur counter.
- Head achanak neeche/aage move: **Possible harsh braking** alert aur counter.
- Camera video browser mein process hota hai; project record/upload nahi karta.
- Counters session tak hain; purane console score se automatically connected nahi hain.
- Normal blinking ko ignore karne ke liye timer hai. Poor light, glasses, head turns aur camera shake se missed/false alerts aa sakte hain. Stationary classroom demo mein test karo; actual driving safety system ke roop mein use nahi karna.

### Camera viva

**Java ka role?** `CameraMonitor.java` built-in HTTP server se camera page localhost par serve karta hai. `main()` file read karta hai, server start karta hai aur browser kholta hai.

**JavaScript kyun?** Browser webcam permission aur video access deta hai. Camera part JavaScript aur MediaPipe mein hai; yeh pure-Java detection implementation nahi hai.

**AI model kya karta hai?** MediaPipe face aur eye landmark points deta hai. Humne AI model train nahi kiya; existing model use kiya hai. Reference: https://developers.google.com/edge/mediapipe/solutions/vision/face_landmarker/web_js

**Eyes closed kaise?** `eyeRatio()` aankh ki vertical opening ko horizontal width se divide karta hai. Average ratio 0.21 se kam, continuously 1500 milliseconds tak rahe toh alert. Threshold demo heuristic hai, har face ke liye guaranteed nahi hai.

**Harsh braking kaise?** `monitor()` consecutive frames mein face position aur size ka change dekhta hai. Sudden change possible head jolt hai. Camera actual vehicle deceleration measure nahi karta, isliye label possible harsh braking hai. Actual confirmation ke liye vehicle acceleration/speed sensor chahiye.

**Important functions?** `monitor()` detection loop; `eyeRatio()` eye opening; `shutdown()` camera release; `getUserMedia()` camera permission aur stream; `requestAnimationFrame()` next frame.

**Code padhne ka order:** Pehle `monitor()` padho. Uske andar `checkEyes()` eye timer handle karta hai, `checkHeadMovement()` pichhle frame se movement compare karta hai, aur `showReport()` screen par result dikhata hai. `EYE_LIMIT`, `CLOSED_EYE_TIME` aur `HEAD_ALERT_TIME` demo settings hain. Har function ka ek clear kaam hai, isliye alag-alag explain kar sakte ho.

### Camera manual checks

1. Start, permission Allow, preview aur face detected status check karo.
2. Normal blink: no alert. Eyes 2 seconds close: one alert; open then close: next alert.
3. Face hide: face not visible; return: closure timer fresh start.
4. Sudden head nod/lean: possible braking alert (sensitivity lighting/camera par depend karti hai).
5. Stop: camera indicator off. Start again: monitoring resume.
6. Permission deny / internet disconnect: readable error, Start button retry ke liye available.

Simple Java console project. User trip details enter karta hai aur program safety score, rating aur suggestions dikhata hai. Live GPS/sensor tracking aur database is version mein nahi hain; report program close hone ke baad save nahi hoti.

## Run

Java JDK installed hona chahiye. Project folder mein terminal open karke:

```text
javac --release 8 DriverPerformance.java
java DriverPerformance
```

Yeh compile command installed JDK 21 se Java 8-compatible file banati hai, kyunki is computer ka Java runtime 8 hai. Agar JDK bhi 8 ho toh `javac DriverPerformance.java` use karo.

## Score ka logic

- Starting score: 100.
- Maximum speed 80 km/h se zyada: 20 points deduct (sirf ek baar).
- Har sudden brake: 5 points deduct.
- Har harsh acceleration: 5 points deduct.
- Minimum score: 0.
- 80–100: Good; 50–79: Average; 0–49: Needs Improvement.

Example: speed 90, brakes 2, acceleration 1 → 100 − 20 − 10 − 5 = 65 (Average).

Yeh classroom demonstration ke assumed rules hain. 80 km/h universal legal speed limit nahi hai. Counts aur speed manually enter hote hain; program unhe sensor se detect nahi karta.

## Viva mein explain kaise karein

**Project kya karta hai?** Driving behavior ke manually entered data se rule-based safety score aur advice generate karta hai.

**Flow?** Input → validation → score calculation → rating → report → another trip ya exit.

**Scanner kya hai?** Java ki class jo keyboard input read karti hai. `nextLine()` ek poori line read karta hai.

**readNumber()?** Input ko integer banata hai. Negative value, text aur integer range se bahar value par dobara input maangta hai. `try-catch` invalid number ki exception handle karta hai.

**calculateScore()?** Teen inputs lekar penalties calculate karta hai aur score return karta hai. Calculation ke liye `long` bade numbers ko handle karta hai. `Math.max()` score ko zero se neeche nahi jaane deta.

**static kyun?** In methods ko main se call karne ke liye DriverPerformance ka alag object banana nahi padta.

**do-while kyun?** Report kam se kam ek baar generate hoti hai; uske baad user chahe toh repeat hoti hai.

**if-else kyun?** Score ke hisaab se category decide karta hai. Advice ke independent `if` statements ek saath multiple suggestions de sakte hain.

**Database connectivity?** Is version mein database nahi hai. Data variables mein temporarily rehta hai. Database requirement ho toh teacher se confirm karke storage add karna hoga.

**Limitations?** Manual data, assumed scoring rules, no saved history, no live monitoring.

## Practice inputs

| Speed | Brakes | Acceleration | Expected score | Rating |
| --- | --- | --- | --- | --- |
| 60 | 0 | 0 | 100 | Good |
| 80 | 0 | 0 | 100 | Good |
| 90 | 2 | 1 | 65 | Average |
| 70 | 10 | 1 | 45 | Needs Improvement |
| 100 | 20 | 20 | 0 | Needs Improvement |

Text aur negative number enter karke validation bhi check karo. Team members apna actual contribution hi explain karein aur poora program ek baar khud run karke samjhein.
