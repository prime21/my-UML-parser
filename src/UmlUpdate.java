import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlParameter;

public class UmlUpdate {

    private static void updopt(UmlTreeNode now) {
        for (UmlTreeNode nxt: now.getSons()) {
            if (((UmlParameter)nxt.getElm()).
                getDirection().equals(Direction.RETURN)) {
                ((UmlOptNode)now).markRet();
            }
            else {
                ((UmlOptNode) now).markPar();
            }
        }
    }

    private static void updass(UmlTreeNode now) {
        String end1 = ((UmlAssociation)now.getElm()).getEnd1();
        String end2 = ((UmlAssociation)now.getElm()).getEnd2();
        UmlTreeNode e1 =
            NodePool.getInstance().get(Idmap.getInstance().get(end1));
        e1 = NodePool.getInstance().get(Idmap.getInstance().get(
                ((UmlAssociationEnd)e1.getElm()).getReference()
            ));

        UmlTreeNode e2 =
            NodePool.getInstance().get(Idmap.getInstance().get(end2));
        e2 = NodePool.getInstance().get(Idmap.getInstance().get(
            ((UmlAssociationEnd)e2.getElm()).getReference()
        ));

        //System.out.println(e1.getType() + " " + e2.getType());

        if (e1.getType().equals(ElementType.UML_CLASS)) {
            ((UmlClassNode) e1).add(e2);
        }
        else {
            ((UmlItfNode)e1).addinter(e2);
        }

        if (e2.getType().equals(ElementType.UML_CLASS)) {
            ((UmlClassNode) e2).add(e1);
        }
        else {
            ((UmlItfNode)e2).addinter(e1);
        }
    }

    private static void updcls(UmlTreeNode now) {
        for (UmlTreeNode nxt: now.getSons()) {
            switch (nxt.getType()) {
                case UML_ATTRIBUTE:
                case UML_OPERATION:
                    ((UmlClassNode)now).add(nxt);
                    break;
                default:
                    break;
            }
        }
    }

    private static void updgen(UmlTreeNode now) {
        UmlGeneralization g = (UmlGeneralization)(now.getElm());
        int src = Idmap.getInstance().get(g.getSource());
        int tar = Idmap.getInstance().get(g.getTarget());
        UmlTreeNode e1 = NodePool.getInstance().get(src);
        UmlTreeNode e2 = NodePool.getInstance().get(tar);

        if (e1.getType().equals(ElementType.UML_CLASS)) {
            ((UmlClassNode)e1).setExtclass(e2);
            ((UmlClassNode)e2).addsoncls(e1);
        }
        else {
            ((UmlItfNode)e1).addup(e2);
            ((UmlItfNode)e2).addup(e1);
        }
    }

    private static void updreal(UmlTreeNode now) {
        UmlInterfaceRealization g = (UmlInterfaceRealization) now.getElm();
        int src = Idmap.getInstance().get(g.getSource());
        int tar = Idmap.getInstance().get(g.getTarget());
        UmlTreeNode e1 = NodePool.getInstance().get(src);
        UmlTreeNode e2 = NodePool.getInstance().get(tar);

        ((UmlClassNode)e1).addinter(e2);
        ((UmlItfNode)e2).adddn(e1);
    }

    public static void upd(UmlTreeNode now) {
        switch (now.getType()) {
            case UML_ASSOCIATION_END:
            case UML_ATTRIBUTE:
            case UML_PARAMETER:
                break;
            case UML_OPERATION: // get all parameter
                updopt(now);
                break;
            case UML_ASSOCIATION: // get two end
                updass(now);
                break;
            case UML_CLASS: // get all operation, attribute
                updcls(now);
                break;
            case UML_GENERALIZATION: // make father
                updgen(now);
                break;
            case UML_INTERFACE_REALIZATION: // get interface realization
                updreal(now);
                break;
            case UML_INTERFACE:
                break;
            default:
                break;
        }
    }

}
