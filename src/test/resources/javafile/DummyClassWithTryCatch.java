public class DummyClassWithTryCatch {
    public int meth(int a, int b) {
        try {
            if (true) return a;
        } catch (Exception e) {
            if (a > b) {
                return a + b;
            }
        }
    }
}