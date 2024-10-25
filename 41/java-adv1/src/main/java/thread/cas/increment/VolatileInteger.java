package thread.cas.increment;

public class VolatileInteger implements IncrementInteger {

    private volatile int value = 0;

    @Override
    public void increment() {
        value++;
    }

    @Override
    public int get() {
        return value;
    }
}
