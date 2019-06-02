import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.elements.UmlElement;

import java.util.HashSet;

public class UmlTreeNode {
    private UmlElement elm;
    private ElementType type;
    private int id;
    private HashSet<UmlTreeNode> sons;
    private UmlTreeNode fa;

    public UmlTreeNode(UmlElement element,int nowid) {
        elm = element;
        type = element.getElementType();
        id = nowid;
        sons = new HashSet<UmlTreeNode>();
        fa = null;
    }

    public ElementType getType() {
        return type;
    }

    public HashSet<UmlTreeNode> getSons() {
        return sons;
    }

    public void setFa(UmlTreeNode fa) {
        this.fa = fa;
    }

    public void addSon(UmlTreeNode so) {
        sons.add(so);
    }

    public int getId() {
        return id;
    }

    public UmlTreeNode getFa() {
        return fa;
    }

    public UmlElement getElm() {
        return elm;
    }

    @Override
    public int hashCode() {
        return elm.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  UmlTreeNode) {
            return elm.equals(((UmlTreeNode) obj).getElm());
        }
        return false;
    }

    public static UmlTreeNode newInstance(UmlElement ele, int nowid) {
        if (ele.getElementType().equals(ElementType.UML_CLASS)) {
            return new UmlClassNode(ele,nowid);
        }
        else if (ele.getElementType().equals(ElementType.UML_OPERATION)) {
            return new UmlOptNode(ele,nowid);
        }
        else if (ele.getElementType().equals(ElementType.UML_INTERFACE)) {
            return new UmlItfNode(ele,nowid);
        }
        else {
            return new UmlTreeNode(ele,nowid);
        }
    }
}
