import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Graph<V> {

	private class Node {
		public V info;
		public boolean visited;
		public List<Edge> adj;

		public Node(V info) {
			this.info = info;
			this.visited = false;
			this.adj = new ArrayList<Edge>();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((info == null) ? 0 : info.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Node other = (Node) obj;
			if (info == null) {
				if (other.info != null)
					return false;
			} else if (!info.equals(other.info))
				return false;
			return true;
		}
	}

	private class Edge {
		public Node neighbor;

		public Edge(Node neighbor) {
			super();
			this.neighbor = neighbor;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((neighbor == null) ? 0 : neighbor.hashCode());
			return result;
		}

		// Considero que son iguales si "apuntan" al mismo nodo (para no agregar
		// dos ejes al mismo nodo)
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Edge other = (Edge) obj;
			if (neighbor == null) {
				if (other.neighbor != null)
					return false;
			} else if (!neighbor.equals(other.neighbor))
				return false;
			return true;
		}

	}

	private HashMap<V, Node> nodes;

	public Graph() {
		this.nodes = new HashMap<V, Node>();
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public void addVertex(V vertex) {
		if (!nodes.containsKey(vertex)) {
			nodes.put(vertex, new Node(vertex));
		}
	}

	public void addEdge(V v, V w) {
		Node origin = nodes.get(v);
		Node dest = nodes.get(w);
		if (origin != null && dest != null && !origin.equals(dest)) {
			Edge edge = new Edge(dest);
			if (!origin.adj.contains(edge)) {
				origin.adj.add(edge);
				dest.adj.add(new Edge(origin));
			}
		}
	}

	public int edgeCount() {
		int count = 0;
		for (Node n : getNodes())
			count += n.adj.size();
		count /= 2;
		return count;
	}

	public void RemoveEdge(V v, V w) {
		Node origin = nodes.get(v);
		if (origin == null)
			return;
		Node dest = nodes.get(w);
		if (dest == null)
			return;
		origin.adj.remove(new Edge(dest));
		dest.adj.remove(new Edge(origin));
	}

	public boolean isEdge(V v, V w) {
		Node origin = nodes.get(v);
		if (origin == null)
			return false;

		for (Edge e : origin.adj) {
			if (e.neighbor.info.equals(w)) {
				return true;
			}
		}
		return false;

	}

	public List<V> neighbors(V v) {
		Node node = nodes.get(v);
		if (node != null) {
			List<V> l = new ArrayList<V>(node.adj.size());
			for (Edge e : node.adj) {
				l.add(e.neighbor.info);
			}
			return l;
		}
		return null;
	}

	public void removeVertex(V v) {
		Node node = nodes.get(v);
		if (node == null)
			return;

		// Primero removerlo de la lista de adyacencia de sus vecinos
		Edge e = new Edge(node);
		for (Node n : getNodes()) {
			if (!n.equals(node))
				n.adj.remove(e);
		}

		// Eliminar el nodo
		nodes.remove(v);
	}

	public int vertexCount() {
		return nodes.size();
	}

	private List<Node> getNodes() {
		List<Node> l = new ArrayList<Node>(vertexCount());
		Iterator<V> it = nodes.keySet().iterator();
		while (it.hasNext()) {
			l.add(nodes.get(it.next()));
		}
		return l;
	}

	public List<V> DFS() {
		return DFS(getNodes().get(0).info);
	}

	public List<V> DFS(V origin) {
		Node node = nodes.get(origin);
		if (node == null)
			return null;
		clearMarks();
		List<V> l = new ArrayList<V>();
		this.DFS(node, l);
		return l;
	}

	private void clearMarks() {
		for (Node n : getNodes()) {
			n.visited = false;
		}
	}

	private void DFS(Node origin, List<V> l) {
		if (origin.visited)
			return;
		l.add(origin.info);
		origin.visited = true;
		for (Edge e : origin.adj)
			DFS(e.neighbor, l);
	}

	public List<V> BFS(V origin) {
		Node node = nodes.get(origin);
		if (node == null)
			return null;
		clearMarks();
		List<V> l = new ArrayList<V>();

		Queue<Node> q = new LinkedList<Node>();
		q.add(node);
		while (!q.isEmpty()) {
			node = q.poll();
			l.add(node.info);
			node.visited = true;
			for (Edge e : node.adj) {
				if (!e.neighbor.visited) {
					q.add(e.neighbor);
				}
			}
		}
		return l;
	}

	public boolean isConnected() {
		if (isEmpty()) {
			return true;
		}
		clearMarks();
		List<Node> l = getNodes();
		List<V> laux = new ArrayList<V>();
		DFS(l.get(0), laux);
		for (Node node : l) {
			if (!node.visited) {
				return false;
			}
		}
		return true;
	}
}