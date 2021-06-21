import java.util.concurrent.TimeUnit;

public class PID {
    private double target;
    private double p;
    private double i;
    private double d;
    private double lastPError;
    private double iError;
    private long lastTime = 0;


    public PID(double target, double p, double i, double d) {
        this.target = target;
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

        // calculate the error
        double pError = target - currentValue;
        // calculate the integral
        iError += pError * dt;
        // calculate the derivative
        double dError = (pError - lastPError) / dt;

        // updating temp variables
        lastTime = currentTime;
        lastPError = pError;

        // formula of the PID controller
        return p * pError + i * iError + d * dError;
    }

}
