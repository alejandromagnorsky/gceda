package io;


import java.io.IOException;

import algorithm.Graph;
import algorithm.TreeState;

public class GraphColoring {

	public static void main(String args[]) {
		try {
			if (args.length < 2) {
				System.out
						.println("Ingrese el archivo a cargar y el metodo elegido");
				return;
			}
			Graph<String> graph = GraphLoader.loadGraph(args[0]);
			String fileName = args[0].substring(0, args[0].lastIndexOf("."));
			String method = args[1];
			if (method.equals("exact")) {
				boolean makeTree = false;
				if (args.length >= 3) {
					if (args[2].equals("tree")) {
						makeTree = true;
					} else {
						System.out
								.println("Error. El tercer argumento debe ser \"tree\" o null");
						return;
					}
				}
				Graph<TreeState<String>> tree = graph.exactColoring(makeTree);
				if (makeTree)
					GraphExporter.exportGraphTree("tree - " + fileName, tree);
			} else if (method.equals("greedy")) {
				graph.greedyColoring();
			} else if (method.equals("ts")) {
				graph.tsColoring();
			} else {
				System.out
						.println("Error. Debe especificarse algun de los siguientes metodos : exact, greedy o ts.");
				return;
			}
			GraphExporter.exportColors(fileName, graph);
			GraphExporter.exportGraph(fileName, graph);
		} catch (IOException e) {
			System.out.println("Error de archivo.");
		}
	}
}
