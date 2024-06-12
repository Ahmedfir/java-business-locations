public class DummyClassWithElseIf {
    public int meth(int a, int b) {
        if (true) return a;
        else {
            if (a > b) {
                return a + b;
            }
        }
    }
}