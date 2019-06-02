import java.util.HashMap;

public class Idmap {

    private HashMap<String,Integer> mm;

    public void put(String ss,int ii) {
        mm.put(ss,ii);
    }

    public boolean containsKey(String ss) {
        return mm.containsKey(ss);
    }

    public int get(String ss) {
        return mm.get(ss);
    }

    private static volatile Idmap instance = null;

    private Idmap() {
        mm = new HashMap<String, Integer>(300);
    }

    public static Idmap getInstance() {
        if (instance == null) {
            synchronized (Idmap.class) {
                if (instance == null) {
                    instance = new Idmap();
                }
            }
        }
        return instance;
    }
}
