@echo off
cd /d "%~dp0"
javac --release 8 CameraMonitor.java
if errorlevel 1 goto end
java CameraMonitor
:end
pause
