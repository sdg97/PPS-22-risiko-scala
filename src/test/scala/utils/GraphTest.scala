package utils

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class GraphTest extends AnyFunSpec with Matchers:
  describe("A Traversable Graph"){
    it(" should have as first node the first node added"){
      val g = new GraphWithEdgeImpl with TraversableGraph {
        override type Node = String
        override type Edge = String
      }
      g.addEdge("Node1","Node2", "Edge12")
      g.addEdge("Node2","Node3", "Edge23")
      assert(g.currentNode.get == "Node1")
    }

    it(" it should not allow to connect two different nodes using the same edge") {
      val g = new GraphWithEdgeImpl with TraversableGraph {
        override type Node = String
        override type Edge = String
      }
      g.addEdge("Node1", "Node2", "Edge12")
      g.addEdge("Node1", "Node3", "Edge13")
      assertThrows[CantConnectTwoDifferentNodeWithTheSameEdge[String, String]] {
        g.addEdge("Node1", "Node3", "Edge12")
      }
    }

    it(" should raise NoEdgeToCross exception if there is no edge to cross from current node"){
      val g = new GraphWithEdgeImpl with TraversableGraph {
        override type Node = String
        override type Edge = String
      }
      g.addEdge("Node1", "Node2", "Edge12")
      g.addEdge("Node2", "Node3", "Edge23")

      assertThrows[NoEdgeToCross[String]] {
        g.crossEdge("Edge23")
      }
    }

    it(" should not allow to set as current node a node not in the graph"){
      val g = new GraphWithEdgeImpl with TraversableGraph {
        override type Node = String
        override type Edge = String
      }
      g.addEdge("Node1", "Node2", "Edge12")
      g.addEdge("Node2", "Node3", "Edge23")

      assertThrows[NodeNotFound[String]] {
       g currentNode "Node10"
      }
    }

    it(" should cross edge correctly"){
      val g = new GraphWithEdgeImpl with TraversableGraph:
        override type Node = String
        override type Edge = Int

      g.addEdge("a", "b", 2)
      g.addEdge("b", "c", 3)
      g.crossEdge(2)
      assert(g.currentNode.get == "b")
      g.crossEdge(3)
      assert(g.currentNode.get == "c")

    }
  }
