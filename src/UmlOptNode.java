import com.oocourse.uml1.models.elements.UmlElement;

public class UmlOptNode extends UmlTreeNode {
    private boolean isret;
    private boolean ispar;

    public UmlOptNode(UmlElement ee, int id) {
        super(ee,id);
        isret = false;
        ispar = false;
    }

    public void markRet() {
        isret = true;
    }

    public void markPar() {
        ispar = true;
    }

    public boolean isIsret() {
        return isret;
    }

    public boolean isIspar() {
        return ispar;
    }
}
