package Business;

public interface DataSubject {
    public static void addObserver(String e, MyObserver o) {}
    public static void removeObserver(MyObserver o) {}
    public static void avisa(String s) {}
}
