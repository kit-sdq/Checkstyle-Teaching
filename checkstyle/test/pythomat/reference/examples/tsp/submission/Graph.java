
import java.util.*;

class Graph {


    static final int infty = Integer.MAX_VALUE;

    protected int [][] matrix;
    protected int maxnonodes;
    protected int maxnoedges;

    public Graph(int maxnodes) {
	maxnonodes = maxnodes;
	matrix  = new int[maxnodes][maxnodes];
	for (int i=0; i<maxnodes; i++)
	    for (int j=0; j<maxnodes; j++)
		matrix[i][j] = infty;
    }

    public int getnonodes() {
	int count = 0;
	boolean connected;
	for (int i=0; i<maxnonodes; i++) {
	    connected = false;
	    for (int j=0; (j<maxnonodes)&&(!connected); j++)
		if(matrix[i][j]!=infty) {
		    connected = true;
		    count++;
		}
	}
	return count;
    }//getnonodes


    public void  addEdge(int node1, int node2, int dist) throws GraphException {
	//PRE 0<dist<infty
	if (matrix[node1][node2]==infty) {
	    matrix[node1][node2] = dist;
	    matrix[node2][node1] = dist;
	}
	else
	    throw new GraphException();

    }//addedge

    public void delEdge(int node1, int node2) throws GraphException {
	if (matrix[node1][node2]!=infty) {
	    matrix[node1][node2] = infty;
	    matrix[node2][node1] = infty;
	}
	else
	    throw new GraphException();

    }//deledge

    public int edgelength(int node1, int node2) {
	return matrix[node1][node2];
    }//edgelength

    private boolean edgeinlist(List l,int i, int j) {
	boolean found = false;
	Edge e;
	for(ListIterator it=l.listIterator(); it.hasNext()&&!found;) {
	    e = (Edge) it.next();
	    found = ((e.node1()==i)&&(e.node2()==j))||
		    ((e.node2()==i)&&(e.node1()==j));
	}
	return found;
    }//edgeinlist

}
