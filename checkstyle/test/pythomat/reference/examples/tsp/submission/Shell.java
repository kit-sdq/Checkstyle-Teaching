
import java.io.*;
import java.util.*;


class Shell {

    final static int MAX_NODES = 50;

    static private TspGraph myGraph = new TspGraph(MAX_NODES);

    static void doadd(int node1, int node2, int length) {
		try {
			myGraph.addEdge(node1, node2, length);
		} catch (GraphException ge) {
			System.out.println("Error! Graph Exception while adding edge");
			System.exit(1);
		}
    }
    
    static void salesman() {
		List<Edge> res = myGraph.tsp();
		if (res == null || res.size() == 0) {
			System.out.println("Error! No roundtrip found");
		} else {
			Edge start = res.get(0);
			Edge end = res.get(res.size()-1);
			int lastnode;
			if (start.node1() == end.node2() || start.node2() == end.node2()) {
				lastnode = end.node2();
			} else {
				lastnode = end.node1();
			}
			System.out.print(lastnode);
			for (Edge edge : res) {
				System.out.print(",");
				if (edge.node1()==lastnode) {
					System.out.print(edge.node2());
					lastnode = edge.node2();
				} else {
					System.out.print(edge.node1());
					lastnode = edge.node1();
				}
			}
			System.out.println();
		}
    }

    public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: ...");
			System.exit(1);
		}
		
		BufferedReader br = null;
 
		try { 
			String sCurrentLine;
 			br = new BufferedReader(new FileReader(args[0]));
 
			while ((sCurrentLine = br.readLine()) != null) {
				String[] tokens = sCurrentLine.split(",");
				if (tokens.length != 3) {
					System.out.println("Error! Unexpected input '" + sCurrentLine + "'");
					System.exit(1);
				}
				// convert tokens to int
				int node1 = -1;
				int node2 = -1;
				int length = -1;
				try {
					node1 = Integer.parseInt(tokens[0]);
					node2 = Integer.parseInt(tokens[1]);
					length = Integer.parseInt(tokens[2]);
			    } catch (NumberFormatException nfe) {
					System.out.println("Error! Unexpected input '" + sCurrentLine + "'");
					System.exit(1);
				}
				// check ranges
				if (node1 < 0 || node1 > MAX_NODES) {
					System.out.println("Error! Node 1 out of range");
					System.exit(0);
				}
				if (node2 < 0 || node2 > MAX_NODES) {
					System.out.println("Error! Node 2 out of range");
					System.exit(0);
				}
				if (length < 0) {
					System.out.println("Error! Length out of range");
					System.exit(0);
				}
				// misc checks
				if (node1 == node2) {
					System.out.println("Error! node numbers are the same");
				}
				
				doadd(node1, node2, length);
			}
 
			salesman();
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
