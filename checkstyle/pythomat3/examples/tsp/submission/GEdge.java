

import java.util.*;

class GEdge implements Edge, Comparator<GEdge> {
    int node1, node2, dist;
    GEdge(int i , int j, int d) {
	node1 = i;
	node2 = j;
	dist = d;
    }
    public int node1() {
	return this.node1;
    }
    public int node2() {
	return this.node2;
    }
    public int length() {
	return this.dist;
    }
    public void print() {
	if (node1<node2)
	    System.out.print(" ("+node1+" "+node2+" "+dist+") ");
	else 
	    System.out.print(" ("+node2+" "+node1+" "+dist+") ");
    }
    public int compare(GEdge edge1, GEdge edge2) {
	if (edge1.dist== edge2.dist)
	    return 0;
	else if (edge1.dist > edge2.dist)
	    return 1;
	else
	    return -1;
    }
    public boolean equals(GEdge edge) {
	return this.dist== edge.dist;
    }
    
    public String toString() {
		return "(" + node1 + "," + node2 + ")";
	}
}
