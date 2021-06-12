import java.util.concurrent.TimeUnit;

public class PID {
    private double target;
    private double minBounds;
    private double maxBounds;
    private double p;
    private double i;
    private double d;
    private double lastPError;
    private double iError;
    private long lastTime = 0;

    // check it out: https://ru.wikipedia.org/wiki/ПИД-регулятор
    public PID(double target, double minBounds, double maxBounds, double p, double i, double d) {
        this.target = target;
        this.minBounds = minBounds;
        this.maxBounds = maxBounds;
        this.p = p;
        this.i = i;
        this.d = d;
    }

    public double update(double currentValue) {
        if(lastTime == 0) {
            lastTime = System.currentTimeMillis();
            lastPError = target-currentValue;
            return 0;
        }
        long currentTime = System.currentTimeMillis();

        double dt = (double) (currentTime - lastTime) / TimeUnit.SECONDS.toMillis(1);
        if(dt == 0)
            return 0;

        double pError = target - currentValue;
        double dError = (pError - lastPError) / dt;

        if(iError < maxBounds && iError > minBounds)
            iError += pError * dt;

        lastTime = currentTime;
        lastPError = pError;

        return p * pError + i * iError + d * dError;
    }

}
