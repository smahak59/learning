package com.learning.algorithms.graphs.traversal

import com.learning.data_structures.mutable.graph.AdjacencyListGraph
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class BreadthFirstSpec extends Specification {

  trait Context extends Scope {
    val traversal = new BreadthFirst
    val graph = new AdjacencyListGraph(6)
  }

  "returns the nodes in breadth first search" in new Context {
    graph.addEdge(0, 3, 10)
    graph.addEdge(0, 2, 10)
    graph.addEdge(0, 1, 10)
    graph.addEdge(1, 5, 10)
    graph.addEdge(1, 4, 10)

    traversal.traverse(graph)(0) ==== Seq(0, 3, 2, 1, 5, 4)
  }

  "returns the nodes in breadth first search when there is a cycle" in new Context {
    graph.addEdge(0, 1, 10)
    graph.addEdge(0, 2, 10)
    graph.addEdge(2, 0, 10)
    graph.addEdge(2, 4, 10)
    graph.addEdge(4, 3, 10)
    graph.addEdge(3, 5, 10)

    traversal.traverse(graph)(0) ==== Seq(0, 1, 2, 4, 3, 5)
  }

  "returns a single node when it's outDegree is 0" in new Context {
    graph.addEdge(1, 2, 10)
    graph.addEdge(2, 3, 10)
    graph.addEdge(3, 4, 10)
    graph.addEdge(4, 5, 10)

    traversal.traverse(graph)(0) ==== Seq(0)
  }
}
