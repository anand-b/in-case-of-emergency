package edu.nandboolean.incaseofemergency.utils;

public class ICEEventLogger {
    private int currentEventCount;
    private long lastEventTimeInMillis;

    public ICEEventLogger() {
        currentEventCount = 0;
        lastEventTimeInMillis = -1;
    }

    public void logEvent() {
        long tappedTimeInMillis = System.currentTimeMillis();
        if (this.lastEventTimeInMillis!= -1 && tappedTimeInMillis - this.lastEventTimeInMillis > 1000) {
            this.currentEventCount = 0;
        }
        this.lastEventTimeInMillis = tappedTimeInMillis;
        this.currentEventCount++;
    }

    public int getCurrentEventCount() {
        return this.currentEventCount;
    }

    public void clear() {
        this.currentEventCount = 0;
        this.lastEventTimeInMillis = -1;
    }
}
