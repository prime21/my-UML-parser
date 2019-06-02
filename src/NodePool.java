import java.util.ArrayList;

public class NodePool {
    private ArrayList<UmlTreeNode> nds;

    private static volatile NodePool instance = null;

    public void add(UmlTreeNode now) {
        nds.add(now);
    }

    public UmlTreeNode get(int index) {
        return nds.get(index);
    }

    private NodePool() {
        nds = new ArrayList<UmlTreeNode>(300);
    }

    public static NodePool getInstance() {
        if (instance == null) {
            synchronized (NodePool.class) {
                if (instance == null) {
                    instance = new NodePool();
                }
            }
        }
        return instance;
    }
}
