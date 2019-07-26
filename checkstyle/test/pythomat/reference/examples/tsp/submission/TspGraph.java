
import java.util.*;

class TspGraph extends Graph {

    class Node {
	int noOutEdges=0;
	int edgecount = 0;
	int [] outEdges=null; //aufsteigend sortiert nach Laenge

	protected void addedgeentry(int edgenum) {
	    if (outEdges==null) {
		outEdges = new int[noOutEdges];
		edgecount = 0;
	    }
	    outEdges[edgecount] = edgenum;
	    edgecount++;
	}
    }

    BitSet bestTour = null;
    int bestTourCost = infty;
    int noEdges = 0;
    int noNodes = 0;

    private Edge [] edges;
    private Node [] nodes;

    private int BBcount = 0;

    public TspGraph(int maxnodes) {
	super(maxnodes);
    }

    private void initinfo() {  
	//initialisiert Nodes/Edges Hilfstabellen 
	//unmittelbar vor tsp

	noNodes = getnonodes();
	noEdges = 0;
	nodes = new Node[maxnonodes];
	LinkedList<GEdge> edgelist = new LinkedList<GEdge>();
	for (int i=0; i<maxnonodes; i++) {
	    nodes[i] = new Node();
	    for (int j=0; j<maxnonodes; j++) {
		if (matrix[i][j]<infty) {
		    nodes[i].noOutEdges++;
		    if (i<j) {
		        noEdges++;
		        edgelist.addFirst(new GEdge(i, j, matrix[i][j]));
		    }
		}
	    }
	}
	if (edgelist.size()>0)
	    Collections.sort(edgelist, edgelist.getFirst());
	edges = new GEdge[noEdges];
	for (int i=0; i<noEdges; i++) {
	    edges[i] = (Edge) edgelist.getFirst();
	    edgelist.removeFirst();
	    nodes[edges[i].node1()].addedgeentry(i);	    
            nodes[edges[i].node2()].addedgeentry(i);
	}
	for (int i=0;i<maxnonodes; i++) {
	    if (nodes[i].outEdges!=null)
		Arrays.sort(nodes[i].outEdges);
	    nodes[i].edgecount = 0;
	}

	/*
	//testausgabe:
	for (int i=0; i<noEdges; i++) {
	    System.out.print("edge "+i+": ");
	    edges[i].print();
	    System.out.println();
	}
	for (int i=1; i<=noNodes; i++) {
	    System.out.print("node "+i+": "+nodes[i].noOutEdges+" edges: ");
            for (int j=0; j<nodes[i].noOutEdges; j++)
		System.out.print(" "+nodes[i].outEdges[j]);
	    System.out.println();
	
	}
	*/

    }//initin

    private Edge getEdge (int node1, int node2) {
	// PRE es gibt Kante zwischen node1()/2
	Edge e;
	for (int j=0; j<nodes[node1].noOutEdges; j++) {
	    e = edges[nodes[node1].outEdges[j]];
	    if ((e.node1()==node2)||(e.node2()==node2))
		return e;
	}
	return null;
    }

    private int countpossibleselected (int nodenum, int edgelim, BitSet selected) {
	//zaehle ausgewaehlte Kanten von Knoten nodenum im bereich 0 bis edgelim
	//sowie noch moegliche kanten im bereich >edgelim
	int res = 0;
	for (int i=0; i<nodes[nodenum].noOutEdges; i++) {
	    int edgenum = nodes[nodenum].outEdges[i];
	    if (edgenum<edgelim) {
      	        if (selected.get(edgenum))
		    res++;
	    } else if (edgenum>edgelim) //kann auf jeden Fall noch hinzugenommen werden
		res++;
     
	}
	return res;
    }

    private int countselected(int nodenum, int edgelim, BitSet selected) {
	//zaehle definitv gew"ahlte Kanten fuer Knoten nodenum
	int res = 0;
	for (int i=0; i<nodes[nodenum].noOutEdges; i++) {
	    int edgenum = nodes[nodenum].outEdges[i];
	    if (edgenum<edgelim)
		if (selected.get(edgenum))
		    res++;
	}
	return res;
    }

    private Integer findselectedneighbour(int nodenum, int node1predecessor, BitSet selected) {
	//berechnet Nummer des ausgewaehlten Nachbarknotens, falls vorhanden	
	//PRE countselected(node1()/2) <= 2
	for (int i=0; i<nodes[nodenum].noOutEdges; i++) {
	    int edgenum = nodes[nodenum].outEdges[i];
	    if (selected.get(edgenum)) {
		if ((edges[edgenum].node1()==nodenum)&&(edges[edgenum].node2()!=node1predecessor))
		    return new Integer(edges[edgenum].node2());
		else if ((edges[edgenum].node2()==nodenum)&&(edges[edgenum].node1()!=node1predecessor))
		    return new Integer(edges[edgenum].node1());
	    }
	}
	return null;
    }

    private boolean connected(int node1, int node1predecessor, int node2, BitSet selected) {
	//PRE selected besteht immer aus einer Menge von Pfaden ohne Verzweigungen
	//PRE countselected(node1()/2) <= 2
	// bestimmt ob Node1()/2 bereits verbunden sind via Kanten in selected
 
	//System.out.print("connected "+node1+" "+node2+" "+selected.toString());      
	int currnode = node1; 
	int currpre = node1predecessor;
	Integer neighbour;
        while (currnode!=node2) {
	    neighbour = findselectedneighbour(currnode,currpre, selected);
	    if (neighbour==null) {
		//System.out.println(" false");
		return false;
	    }
	    else {
		currpre = currnode;
		currnode = neighbour.intValue();
	    }
	}
	//System.out.println(" true");
	return true;	    
    }    

    private boolean withPossible(int edgenum, BitSet selected, int selectedCount) {
	int n1 = edges[edgenum].node1();
	int n2 = edges[edgenum].node2();
	// hoechstens 2 Kanten/Knoten:
	if (countselected(n1, edgenum, selected)>=2)
	    return false;
	if (countselected(n2, edgenum, selected)>=2)
	    return false;
	//keine vorzeitigen Kreise:
	if (selectedCount<noNodes-1)
	    if (connected(n1, n1, n2, selected))
		return false;
	return true;
    }

    private boolean withoutPossible(int edgenum, BitSet selected) {
	// fuer edges 0 - edgenum-1 wurden schon Entscheidungen getroffen
	if (countpossibleselected(edges[edgenum].node1(), edgenum, selected)<2)
	    return false;
	if (countpossibleselected(edges[edgenum].node2(), edgenum, selected)<2)
	    return false;
	return true;
    }

    private int lowerBound(int edgenum, BitSet selected) {
	// Eintr"age in selected die <=edgenum sind definitiv, der Rest noch offen

	//System.out.print("lowerbound "+edgenum+": ");
	int edgecount;
	int bound=0;
	for (int node = 1; node<=noNodes; node++) {
	    // suche die beiden billigsten von node ausgehenden Kanten
	    // unter Beruecksichtigung von selected
	    edgecount = 0;
	    for (int j=0; (j<nodes[node].noOutEdges)&&(edgecount<2); j++) {
		int nextedge = nodes[node].outEdges[j];
		if (nextedge<=edgenum) {
		    if (selected.get(nextedge)) {
			bound += edges[nextedge].length();
			edgecount++;
		    }
		} else {
		    bound += edges[nextedge].length();
		    edgecount++;
		}
	    }
	}

	//System.out.println(" 2*lowerbound: "+bound);

	return bound/2;
    }//lowerBound

    private void branchAndBound(int edgenum, BitSet selected, int selectedCount, int selectedCost) {
	int bound1, bound2;
	BitSet newselected = null;

	//System.out.println("BB: edge "+edgenum+" selected: "+selectedCount+" cost: "+selectedCost);

	BBcount++;

	if (edgenum>=noEdges) { // alle Kanten probiert
	    if (selectedCount == noNodes) {//Rundreise
	        if (selectedCost<bestTourCost) { // neue beste Rundreise
		    bestTour = (BitSet) selected.clone();
	            bestTourCost = selectedCost;
		    //System.out.println("new tour "+bestTour.toString());
	        };
	    }
        } else { // naechste Kante probieren
            //zuerst beide bounds bestimmen;
	    if (withoutPossible(edgenum, selected)) 
		bound1 = lowerBound(edgenum, selected);
	    else 
		bound1 = infty;
	    if (withPossible(edgenum, selected, selectedCount)) {
		newselected = (BitSet) selected.clone();
		newselected.set(edgenum);
		bound2 = lowerBound(edgenum, newselected);
	    } else
		bound2 = infty;
	    // nun rekursiver Aufruf, zuerst variante mit kleinerem Bound
	    if (bound1<bound2) {
		if (bound1<bestTourCost) 
		    branchAndBound(edgenum+1, selected, selectedCount, selectedCost);
		if (bound2<bestTourCost)
		    branchAndBound(edgenum+1, newselected, selectedCount+1, selectedCost+edges[edgenum].length());
	    } else { //umgekehrte Reihenfolge
		if (bound2<bestTourCost)
		    branchAndBound(edgenum+1, newselected, selectedCount+1, selectedCost+edges[edgenum].length());
		if (bound1<bestTourCost) 
		    branchAndBound(edgenum+1, selected, selectedCount, selectedCost);
	    }
	}		
    }//BB

    public List<Edge> tsp() {
	initinfo();
	bestTourCost = infty;
	bestTour = null;
	BBcount = 0;

	branchAndBound(0, new BitSet(noEdges), 0, 0);

	if (bestTour==null) 
	    return null;
	else { // Tour gefunden
	    //System.out.println("best tour: "+bestTour.toString());
	    LinkedList<Edge> res = new LinkedList<Edge>();
	    int node = 1, nextnode = 0, lastnode = 0;
	    for(int i=1; i<=noNodes; i++) {
		nextnode = findselectedneighbour(node, lastnode, bestTour).intValue();
		res.add(getEdge(node, nextnode));
		lastnode = node;
		node = nextnode;
	    }
	    return res;
	}
    }//tsp

    int getTspCallCount() {
	return BBcount;
    }

    int tourlength() {
	if (bestTour==null)
	    return 0;
	else
	    return bestTourCost;
    }

} //TspGraph
