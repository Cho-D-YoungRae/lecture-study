package thread.cas.increment;

public class BasicInteger implements IncrementInteger {

    private int value = 0;

    @Override
    public void increment() {
        value++;
    }

    @Override
    public int get() {
        return value;
    }
}
