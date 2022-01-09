package util;

public class Time {
    public static  float timeStarted = System.nanoTime(); //time the application is started (static init in app start time)

    public static float getTime(){
        return (float)((System.nanoTime()-Time.timeStarted)*1E-9); //elapsed time in SECONDS from start of app
    }
}
