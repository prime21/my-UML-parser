import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyUmlInteraction implements UmlInteraction {

    //private ArrayList<UmlTreeNode> nd;

    private HashMap<String,UmlTreeNode> clsnd;
    private HashSet<String> multicls;

    private HashSet<Integer> clsvis;
    private HashSet<Integer> intvis;

    private int allclass;

    public void dfsuml(UmlTreeNode now) {
        for (UmlTreeNode nxt: now.getSons()) {
            dfsuml(nxt);
        }
        UmlUpdate.upd(now);
    }

    public void dfscls(UmlClassNode now) {
        for (UmlTreeNode nxt: now.getSonclass()) {
            ((UmlClassNode) nxt).updfromfa(now);
            dfscls((UmlClassNode) nxt);
        }
    }

    public void interbfs(int len) {
        LinkedList<UmlTreeNode> que =
            new LinkedList<UmlTreeNode>();
        for (int i = 0; i < len; i++) {
            if (NodePool.getInstance().get(i) instanceof UmlItfNode) {
                UmlItfNode now = (UmlItfNode) NodePool.getInstance().get(i);
                if (now.getUpint().size() == 0) {
                    que.addLast(now);
                }
            }
        }

        while (!que.isEmpty()) {
            UmlTreeNode now = que.getFirst();
            que.removeFirst();
            for (UmlTreeNode nxt: ((UmlItfNode)now).getDnint()) {
                if (nxt instanceof UmlItfNode) {
                    ((UmlItfNode) nxt).addss(now);
                    ((UmlItfNode) nxt).addss(((UmlItfNode) now).getSsint());
                    ((UmlItfNode) nxt).incVisit();
                    ((UmlItfNode) nxt).getFaasso().
                        addAll(((UmlItfNode)now).getAsso());
                    ((UmlItfNode) nxt).getFaasso().
                        addAll(((UmlItfNode)now).getFaasso());
                    if (((UmlItfNode) nxt).getVisit()
                        == ((UmlItfNode) nxt).getUpint().size()) {
                        que.addLast(nxt);
                    }
                }
                else if (nxt instanceof UmlClassNode) {
                    ((UmlClassNode) nxt).getFainter().addAll(
                        ((UmlItfNode) now).getSsint());
                    ((UmlClassNode) nxt).getFaassoclass().
                        addAll(((UmlItfNode)now).getAsso());
                    ((UmlClassNode) nxt).getFaassoclass().
                        addAll(((UmlItfNode)now).getFaasso());
                    ((UmlClassNode) nxt).incVisit();
                    /*
                    if (((UmlClassNode) nxt).getVisit()
                        == ((UmlClassNode) nxt).getInter().size()) {
                        que.addLast(nxt);
                    }
                    */
                }

            }
        }
    }

    public MyUmlInteraction(UmlElement... elements) {
        clsnd = new HashMap<String, UmlTreeNode>(elements.length);
        multicls = new HashSet<String>(elements.length);
        clsvis = new HashSet<Integer>(elements.length);
        intvis = new HashSet<Integer>(elements.length);
        allclass = 0;

        int cnt = 0;

        for (int i = 0; i < elements.length; i++) {
            UmlTreeNode now = UmlTreeNode.newInstance(elements[i],cnt);
            NodePool.getInstance().add(now);
            if (elements[i].getElementType().equals(ElementType.UML_CLASS)) {
                allclass++;
                if (clsnd.containsKey(elements[i].getName())) {
                    multicls.add(elements[i].getName());
                }
                else {
                    clsnd.put(elements[i].getName(),now);
                }
            }
            Idmap.getInstance().put(elements[i].getId(),cnt);
            cnt++;
        }

        for (int i = 0; i < elements.length; i++) {
            UmlTreeNode fand = null;
            if (Idmap.getInstance().containsKey(elements[i].getParentId())) {
                fand = NodePool.getInstance().get(
                    Idmap.getInstance().get(elements[i].getParentId()));
            }
            NodePool.getInstance().get(i).setFa(fand);
            if (fand != null) {
                fand.addSon(NodePool.getInstance().get(i));
            }
        }

        // for uml dfs
        for (int i = 0; i < elements.length; i++) {
            if (NodePool.getInstance().get(i).getFa() == null) {
                dfsuml(NodePool.getInstance().get(i));
            }
        }

        // for inter upd
        interbfs(elements.length);

        // for class upd
        for (int i = 0; i < elements.length; i++) {
            if (
                NodePool.getInstance().get(i) instanceof UmlClassNode &&
                ((UmlClassNode) NodePool.getInstance().get(i)).getExt() == null
            ) {
                dfscls((UmlClassNode) NodePool.getInstance().get(i));
            }
        }
    }

    @Override
    public int getClassCount() {
        return allclass;
    }

    public void checkisclass(String s)
        throws ClassNotFoundException, ClassDuplicatedException {
        if (multicls.contains(s)) {
            throw new ClassDuplicatedException(s);
        }
        else if (!clsnd.containsKey(s)) {
            throw new ClassNotFoundException(s);
        }
    }

    @Override
    public int getClassOperationCount(String s, OperationQueryType oqt)
        throws ClassNotFoundException, ClassDuplicatedException {
        checkisclass(s);
        return ((UmlClassNode)clsnd.get(s)).getoptcnt(oqt);
    }

    @Override
    public int getClassAttributeCount(String s, AttributeQueryType aqt)
        throws ClassNotFoundException, ClassDuplicatedException {
        checkisclass(s);
        return ((UmlClassNode)clsnd.get(s)).getattrcnt(aqt);
    }

    @Override
    public int getClassAssociationCount(String s)
        throws ClassNotFoundException, ClassDuplicatedException {
        checkisclass(s);
        return ((UmlClassNode)clsnd.get(s)).getassocnt();
    }

    @Override
    public List<String> getClassAssociatedClassList(String s)
        throws ClassNotFoundException, ClassDuplicatedException {
        checkisclass(s);
        return ((UmlClassNode)clsnd.get(s)).getassocls();
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
        String s, String s1)
        throws ClassNotFoundException, ClassDuplicatedException {
        checkisclass(s);
        return ((UmlClassNode)clsnd.get(s)).opvis(s1);
    }

    @Override
    public Visibility getClassAttributeVisibility(String s, String s1)
        throws ClassNotFoundException, ClassDuplicatedException,
        AttributeNotFoundException, AttributeDuplicatedException {
        checkisclass(s);
        return ((UmlClassNode)clsnd.get(s)).getatrvis(s1);
    }

    @Override
    public String getTopParentClass(String s)
        throws ClassNotFoundException, ClassDuplicatedException {
        checkisclass(s);
        return ((UmlClassNode)clsnd.get(s)).getTopclass();
    }

    @Override
    public List<String> getImplementInterfaceList(String s)
        throws ClassNotFoundException, ClassDuplicatedException {
        checkisclass(s);
        return ((UmlClassNode)clsnd.get(s)).getintcls();
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String s)
        throws ClassNotFoundException, ClassDuplicatedException {
        checkisclass(s);
        return ((UmlClassNode)clsnd.get(s)).getnotpri();
    }
}
