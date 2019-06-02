import com.oocourse.uml1.models.elements.UmlElement;

import java.util.ArrayList;

public class UmlItfNode extends UmlTreeNode {
    private ArrayList<UmlTreeNode> upint;
    private ArrayList<UmlTreeNode> ssint;
    private ArrayList<UmlTreeNode> dnint;
    private int visit;

    private ArrayList<UmlTreeNode> asso;
    private ArrayList<UmlTreeNode> faasso;

    public UmlItfNode(UmlElement ele,int id) {
        super(ele,id);
        visit = 0;
        upint = new ArrayList<UmlTreeNode>(10);
        dnint = new ArrayList<UmlTreeNode>(10);
        ssint = new ArrayList<UmlTreeNode>(10);
        asso = new ArrayList<UmlTreeNode>(10);
        faasso = new ArrayList<UmlTreeNode>(10);
    }

    public ArrayList<UmlTreeNode> getUpint() {
        return upint;
    }

    public ArrayList<UmlTreeNode> getDnint() {
        return dnint;
    }

    public ArrayList<UmlTreeNode> getSsint() {
        return ssint;
    }

    public ArrayList<UmlTreeNode> getAsso() {
        return asso;
    }

    public ArrayList<UmlTreeNode> getFaasso() {
        return faasso;
    }

    public int getVisit() {
        return visit;
    }

    public void incVisit() {
        visit++;
    }

    public void addinter(UmlTreeNode e) { asso.add(e); }

    public void addup(UmlTreeNode e) {
        upint.add(e);
    }

    public void adddn(UmlTreeNode e) {
        dnint.add(e);
    }

    public void addss(UmlTreeNode e) { ssint.add(e); }

    public void addss(ArrayList<UmlTreeNode> a) { ssint.addAll(a); }
}
