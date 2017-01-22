package com.learning.datastructures.mutable.tree.unbalanced

import com.learning.PropertySpecification
import com.learning.datastructures.mutable.tree.DepthFirst
import com.learning.datastructures.mutable.tree.bst.unbalanced.{BinarySearchTree, Node}
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen}

class BinarySearchTreeSpec extends PropertySpecification {
  val depthFirst = new DepthFirst

  "add" >> {
    "adds elements to the tree" in {
      val bst = new BinarySearchTree[Int]()
      bst.add(2)
      bst.add(3)
      bst.add(1)

      depthFirst.traverse(bst.root)(DepthFirst.inOrder) ==== Seq(1, 2, 3)
    }
  }

  "find" >> {
    "returns element when it is in the tree" in {
      BinarySearchTree
        .from(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        .find(5) ==== Some(5)
    }

    "returns None when element is not in the tree" in {
      BinarySearchTree
        .from(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        .find(0) ==== None
    }

    "returns None when searching in an empty tree" in {
      new BinarySearchTree().find(5) ==== None
    }
  }

  "remove" >> {
    "returns false when element to remove is not in the tree" in {
      //   5
      // 2
      val bst = BinarySearchTree.from(Seq(5, 2))

      bst.remove(1) ==== false

      depthFirst.traverse(bst.root)(DepthFirst.inOrder) ==== Seq(2, 5)
      bst.size ==== 2
    }

    "returns false when trying to remove from empty tree" in {
      val bst = new BinarySearchTree[Int]()

      bst.remove(1) ==== false

      bst.size ==== 0
    }

    "removes root element" in {
      val bst = BinarySearchTree.from(Seq(1))

      bst.remove(1) ==== true

      bst.root ==== None
      bst.size ==== 0
    }

    "removes leaf element from a tree" in {
      //  5
      // 3 7
      val bst = BinarySearchTree.from(Seq(5, 3, 7))

      bst.remove(3) ==== true

      depthFirst.traverse(bst.root)(DepthFirst.inOrder) ==== Seq(5, 7)
      bst.size ==== 2
    }

    "from left subtree" >> {
      "removes element with one left child from a tree" in {
        //    5
        //   3
        //  2
        // 1
        val bst = BinarySearchTree.from(Seq(5, 3, 2, 1))

        bst.remove(3) ==== true

        depthFirst.traverse(bst.root)(DepthFirst.inOrder) ==== Seq(1, 2, 5)
        bst.size ==== 3
      }

      "removes element with one right child from a tree" in {
        //    5
        // 2
        //  3
        //   4
        val bst = BinarySearchTree.from(Seq(5, 2, 3, 4))

        bst.remove(2) ==== true

        depthFirst.traverse(bst.root)(DepthFirst.inOrder) ==== Seq(3, 4, 5)
        bst.size ==== 3
      }

      "removes element with two children from a tree" in {
        //        7
        //    4
        //  2   5
        // 0 3   6
        val bst = BinarySearchTree.from(Seq(7, 4, 2, 5, 0, 3, 6))

        bst.remove(4) ==== true

        depthFirst.traverse(bst.root)(DepthFirst.inOrder) ==== Seq(0, 2, 3, 5, 6, 7)
        bst.size ==== 6
      }
    }

    "from right subtree" >> {
      "removes element with one left child from a tree" in {
        // 5
        //    8
        //   7
        //  6
        val bst = BinarySearchTree.from(Seq(5, 8, 7, 6))

        bst.remove(8) ==== true

        depthFirst.traverse(bst.root)(DepthFirst.inOrder) ==== Seq(5, 6, 7)
        bst.size ==== 3
      }

      "removes element with one right child from a tree" in {
        // 5
        //  8
        //   9
        //    10
        val bst = BinarySearchTree.from(Seq(5, 8, 9, 10))

        bst.remove(8) ==== true

        depthFirst.traverse(bst.root)(DepthFirst.inOrder) ==== Seq(5, 9, 10)
        bst.size ==== 3
      }

      "removes element with two children from a tree" in {
        // 7
        //      11
        //   9      15
        //  8 10  13  16
        val bst = BinarySearchTree.from(Seq(7, 11, 9, 15, 8, 10, 13, 16))

        bst.remove(11) ==== true

        depthFirst.traverse(bst.root)(DepthFirst.inOrder) ==== Seq(7, 8, 9, 10, 13, 15, 16)
        bst.size ==== 7
      }
    }
  }

  "max" >> {
    "returns max element from the tree" in {
      forAll(Gen.nonEmptyListOf(Arbitrary.arbitrary[Int])) { (xs: Seq[Int]) =>
        BinarySearchTree.from(xs).max ==== Some(xs.max)
      }
    }

    "returns None when tree is empty" in {
      new BinarySearchTree[Int]().max ==== None
    }
  }

  "min" >> {
    "returns min element from the tree" in {
      forAll(Gen.nonEmptyListOf(Arbitrary.arbitrary[Int])) { (xs: Seq[Int]) =>
        BinarySearchTree.from(xs).min ==== Some(xs.min)
      }
    }

    "returns None when tree is empty" in {
      new BinarySearchTree[Int]().min ==== None
    }
  }

  "equals" >> {
    "two empty BSTs are equal" in {
      new BinarySearchTree[Int]() ==== new BinarySearchTree[Int]()
    }

    "two BSTs with the same elements are equal" in {
      forAll { (xs: Seq[Int]) =>
        BinarySearchTree.from(xs) ==== BinarySearchTree.from(xs)
      }
    }
  }

  "isValid (only for Bounded typed trees)" >> {
    "returns true if tree is empty" in {
      BinarySearchTree.isValid(None) ==== true
    }

    "returns true if tree is a single element tree" in {
      BinarySearchTree.isValid(Some(Node.leaf(1))) ==== true
    }

    "returns true if tree is bst" in {
      val root = Some(Node(
        10,
        Some(Node(
          5,
          Some(Node(
            5,
            Some(Node.leaf(3)),
            None
          )),
          Some(Node.leaf(7))
        )),
        Some(Node(
          15,
          None,
          Some(Node.leaf(20))
        ))
      ))

      BinarySearchTree.isValid(root) ==== true
    }

    "returns false if tree is not bst" in {
      val root = Some(Node(
        10,
        Some(Node(
          5,
          Some(Node(
            5,
            Some(Node.leaf(3)),
            None
          )),
          Some(Node.leaf(7))
        )),
        Some(Node(
          15,
          None,
          // problem is here
          Some(Node.leaf(0))
        ))
      ))

      BinarySearchTree.isValid(root) ==== false
    }
  }
}