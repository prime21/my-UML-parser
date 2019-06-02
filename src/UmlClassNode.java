
import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class UmlClassNode extends UmlTreeNode {

    private ArrayList<UmlTreeNode> opnoret;
    private ArrayList<UmlTreeNode> opret;

    private ArrayList<UmlTreeNode> opnopar;
    private ArrayList<UmlTreeNode> oppar;

    private ArrayList<UmlTreeNode> attrpri;
    private ArrayList<UmlTreeNode> attrnotpri;
    private ArrayList<UmlTreeNode> faattrpri;
    private ArrayList<UmlTreeNode> faattrnotpri;

    private ArrayList<UmlTreeNode> assoclass;
    private ArrayList<UmlTreeNode> faassoclass;

    private ArrayList<UmlTreeNode> inter;
    private ArrayList<UmlTreeNode> fainter;

    private int visit;

    private UmlTreeNode extclass;
    private UmlTreeNode topclass;

    private ArrayList<UmlTreeNode> sonclass;

    public int getVisit() {
        return visit;
    }

    public void incVisit() {
        visit++;
    }

    public void updfromfa(UmlClassNode now) {
        topclass = now.gettop();

        fainter.addAll(now.getFainter());
        fainter.addAll(now.getInter());

        faassoclass.addAll(now.getAssoclass());
        faassoclass.addAll(now.getFaassoclass());

        faattrpri.addAll(now.getAttrpri());
        faattrpri.addAll(now.getFaattrpri());

        faattrnotpri.addAll(now.getAttrnotpri());
        faattrnotpri.addAll(now.getFaattrnotpri());
    }

    public ArrayList<UmlTreeNode> getAttrnotpri() {
        return attrnotpri;
    }

    public ArrayList<UmlTreeNode> getFaattrnotpri() {
        return faattrnotpri;
    }

    public ArrayList<UmlTreeNode> getAttrpri() {
        return attrpri;
    }

    public ArrayList<UmlTreeNode> getFaattrpri() {
        return faattrpri;
    }

    public ArrayList<UmlTreeNode> getAssoclass() {
        return assoclass;
    }

    public ArrayList<UmlTreeNode> getFaassoclass() {
        return faassoclass;
    }

    public ArrayList<UmlTreeNode> getInter() {
        return inter;
    }

    public ArrayList<UmlTreeNode> getFainter() {
        return fainter;
    }

    public UmlClassNode(UmlElement ele, int id) {
        super(ele,id);
        opnoret = new ArrayList<UmlTreeNode>(10);
        opret = new ArrayList<UmlTreeNode>(10);
        opnopar = new ArrayList<UmlTreeNode>(10);
        oppar = new ArrayList<UmlTreeNode>(10);

        attrpri = new ArrayList<UmlTreeNode>(10);
        attrnotpri = new ArrayList<UmlTreeNode>(10);
        faattrpri = new ArrayList<UmlTreeNode>(10);
        faattrnotpri = new ArrayList<UmlTreeNode>(10);

        assoclass = new ArrayList<UmlTreeNode>(10);
        faassoclass = new ArrayList<UmlTreeNode>(10);

        inter = new ArrayList<UmlTreeNode>(10);
        fainter = new ArrayList<UmlTreeNode>(10);

        extclass = null;
        topclass = this;
        sonclass = new ArrayList<UmlTreeNode>(10);

        visit = 0;
    }

    public UmlTreeNode getExt() {
        return extclass;
    }

    public ArrayList<UmlTreeNode> getSonclass() {
        return sonclass;
    }

    public void addsoncls(UmlTreeNode now) {
        sonclass.add(now);
    }

    public void add(UmlTreeNode now) {
        UmlElement ee = now.getElm();
        switch (ee.getElementType()) {
            case UML_OPERATION:
                if (((UmlOptNode)now).isIsret()) { opret.add(now); }
                else { opnoret.add(now); }
                if (((UmlOptNode)now).isIspar()) { oppar.add(now); }
                else { opnopar.add(now); }
                break;
            case UML_ATTRIBUTE:
                if (((UmlAttribute)ee).getVisibility().equals(
                    Visibility.PRIVATE)) { attrpri.add(now); }
                else { attrnotpri.add(now); }
                break;
            case UML_CLASS:
            case UML_INTERFACE:
                assoclass.add(now);
                break;
            default:
                break;
        }
    }

    public void addinter(UmlTreeNode now) {
        inter.add(now);
    }

    public void setTopclass(UmlTreeNode topclass) {
        this.topclass = topclass;
    }

    public void setExtclass(UmlTreeNode extclass) {
        this.extclass = extclass;
    }

    public int getoptcnt(OperationQueryType queryType) {
        if (queryType.equals(OperationQueryType.RETURN)) {
            return opret.size();
        }
        else if (queryType.equals(OperationQueryType.NON_RETURN)) {
            return opnoret.size();
        }
        else if (queryType.equals(OperationQueryType.PARAM)) {
            return oppar.size();
        }
        else if (queryType.equals(OperationQueryType.NON_PARAM)) {
            return opnopar.size();
        }
        else {
            return opnopar.size() + oppar.size();
        }
    }

    public int getattrcnt(AttributeQueryType queryType) {
        if (queryType.equals(AttributeQueryType.SELF_ONLY)) {
            return attrpri.size() + attrnotpri.size();
        }
        else {
            return attrpri.size() + attrnotpri.size()
                + faattrpri.size() + faattrnotpri.size();
        }
    }

    public int getassocnt() {
        return assoclass.size() + faassoclass.size();
    }

    public List<String> getassocls() {
        HashSet<UmlElement> ret = new HashSet<UmlElement>(150);
        for (int i = 0; i < assoclass.size(); i++) {
            if (assoclass.get(i).getType().equals(ElementType.UML_CLASS)) {
                ret.add(assoclass.get(i).getElm());
            }
        }
        for (int i = 0; i < faassoclass.size(); i++) {
            if (faassoclass.get(i).getType().equals(ElementType.UML_CLASS)) {
                ret.add(faassoclass.get(i).getElm());
            }
        }
        ArrayList<String> val = new ArrayList<String>(150);
        for (UmlElement e: ret) {
            val.add(e.getName());
        }
        return val;
    }

    public Map<Visibility,Integer> opvis(String mtdnm) {
        HashMap<Visibility,Integer> ret = new HashMap<Visibility, Integer>(10);
        for (int i = 0; i < oppar.size(); i++) {
            UmlElement ee = oppar.get(i).getElm();
            if (ee.getName().equals(mtdnm)
                && ElementType.UML_OPERATION.equals(ee.getElementType())) {
                Visibility vv = ((UmlOperation) ee).getVisibility();
                if (!ret.containsKey(vv)) {
                    ret.put(vv,1);
                }
                else {
                    int now = ret.get(vv);
                    ret.remove(vv);
                    ret.put(vv,now + 1);
                }
            }
        }
        for (int i = 0; i < opnopar.size(); i++) {
            UmlElement ee = opnopar.get(i).getElm();
            if (ee.getName().equals(mtdnm)
                && ElementType.UML_OPERATION.equals(ee.getElementType())) {
                Visibility vv = ((UmlOperation) ee).getVisibility();
                if (!ret.containsKey(vv)) {
                    ret.put(vv,1);
                }
                else {
                    int now = ret.get(vv);
                    ret.remove(vv);
                    ret.put(vv,now + 1);
                }
            }
        }
        return ret;
    }

    public Visibility getatrvis(String atrnm)
        throws AttributeDuplicatedException,AttributeNotFoundException {
        Visibility ret = null;
        int cnt = 0;

        for (int i = 0; i < attrpri.size(); i++) {
            UmlElement ee = attrpri.get(i).getElm();
            if (ee.getName().equals(atrnm)
                && ee.getElementType().equals(ElementType.UML_ATTRIBUTE)) {
                cnt++;
                ret = ((UmlAttribute)ee).getVisibility();
            }
        }

        for (int i = 0; i < attrnotpri.size(); i++) {
            UmlElement ee = attrnotpri.get(i).getElm();
            if (ee.getName().equals(atrnm)
                && ee.getElementType().equals(ElementType.UML_ATTRIBUTE)) {
                cnt++;
                ret = ((UmlAttribute)ee).getVisibility();
            }
        }

        for (int i = 0; i < faattrpri.size(); i++) {
            UmlElement ee = faattrpri.get(i).getElm();
            if (ee.getName().equals(atrnm)
                && ee.getElementType().equals(ElementType.UML_ATTRIBUTE)) {
                cnt++;
                ret = ((UmlAttribute)ee).getVisibility();
            }
        }

        for (int i = 0; i < faattrnotpri.size(); i++) {
            UmlElement ee = faattrnotpri.get(i).getElm();
            if (ee.getName().equals(atrnm)
                && ee.getElementType().equals(ElementType.UML_ATTRIBUTE)) {
                cnt++;
                ret = ((UmlAttribute)ee).getVisibility();
            }
        }

        if (cnt == 0) {
            throw new AttributeNotFoundException(
                super.getElm().getName(),atrnm);
        }
        else if (cnt == 1) {
            return ret;
        }
        else {
            throw new AttributeDuplicatedException(
                super.getElm().getName(),atrnm);
        }
    }

    public UmlTreeNode gettop() { return topclass; }

    public String getTopclass() {
        return topclass.getElm().getName();
    }

    public List<String> getintcls() {
        ArrayList<UmlElement> ret = new ArrayList<>(150);

        for (int i = 0; i < fainter.size(); i++) {
            ret.add(fainter.get(i).getElm());
        }
        for (int i = 0; i < inter.size(); i++) {
            ret.add(inter.get(i).getElm());
        }

        ret = new ArrayList<UmlElement>(new HashSet<UmlElement>(ret));

        ArrayList<String> val = new ArrayList<String>(150);
        for (int i = 0; i < ret.size(); i++) {
            val.add(ret.get(i).getName());
        }
        return val;
    }

    public List<AttributeClassInformation> getnotpri() {
        ArrayList<AttributeClassInformation> ret =
            new ArrayList<AttributeClassInformation>(10);
        for (int i = 0; i < attrnotpri.size(); i++) {
            ret.add(new AttributeClassInformation(
                attrnotpri.get(i).getElm().getName(),super.getElm().getName()
            ));
        }
        for (int i = 0; i < faattrnotpri.size(); i++) {
            ret.add(new AttributeClassInformation(
                faattrnotpri.get(i).getElm().getName(),super.getElm().getName()
            ));
        }

        return new ArrayList<AttributeClassInformation>(
            new HashSet<AttributeClassInformation>(ret)
        );
    }
}
