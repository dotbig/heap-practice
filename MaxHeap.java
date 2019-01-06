import java.util.LinkedList;
import java.util.Stack;

public class MaxHeap{

	Node root;
	Node nextLeaf;
	//create an empty maxheap
	public MaxHeap(){
		root = null;
		nextLeaf = root;
	}
	//create an empty maxheap with v as the root
	public MaxHeap(int v){
		root = new Node(v);
		nextLeaf = root;
	}

	public void add(int v){
		System.out.println("adding " + v);
		Node newGuy = new Node(v);
		//if heap is empty, whatever we just added is now the root
		if (nextLeaf == null){
			root = newGuy;
			nextLeaf = root;
			return;
		}
		//nextLeaf is the next non-full leaf on the tree, any new node will be nextLeaf's child (left first)
		newGuy.parent = nextLeaf;
		if (nextLeaf.left == null){
			nextLeaf.left = newGuy;
		} else {
			nextLeaf.right = newGuy;
		}
		//make sure newGuy does not violate the heap's integrity
		bubbleUp(newGuy);
		//if after bubbling up we find newGuy has no parent, then he is the new root
		if (newGuy.parent == null){
			root = newGuy;
		}
		//after giving it another child, ensure nextLeaf is the next non-full node
		nextLeaf = available(root);
	}

	private void bubbleUp(Node n){
		Node current = n;
		//if our parent is null then we're the root of the tree; we're done
		//if our parent's value is smaller than ours then we need to swap with them
		while ((current.parent != null) && (current.val > current.parent.val)){
			System.out.println("bubbleup: swapping "+current.val+ " and " + current.parent.val);
			Node parent = current.parent;
			Node grandparent = parent.parent;
			Node sibling;

			//store our own information for later
			Node copy = clone(current);
			//we swap roles with our parent, this means ..
			if (current.left != null){
				current.left.parent = parent;			//if we have any children,
			}											//our (ex)parent is now their parent
			if (current.right != null){
				current.right.parent = parent;
			}

			if (current == parent.left) { 				//if we're the left child
				sibling = parent.right; 				//then our sibling is the right child
				if (sibling != null){
					sibling.parent = current;			//and we become our siblings parent
				}
				current.parent = grandparent;			//our grandparent is now just our parent
				current.left = parent;					//our parent becomes our child
				current.right = sibling;				//our sibling becomes our child
			} else { 								
				sibling = parent.left; 			
				if (sibling != null){
					sibling.parent = current;			//mirrored for the right child case
				}										//if we're the right child, sibling is the left child
				current.parent = grandparent;	
				current.left = sibling;			
				current.right = parent;			
			}

			parent.parent = current;					//use that information we stored earlier to ensure that
			parent.left = copy.left;					//we're now the parent of our (ex)parent
			parent.right = copy.right;

			if (grandparent != null) {					//and our (ex)parent is now the parent of our (ex)children
				if (parent == grandparent.left) {		//and if we had a grandparent, 
					grandparent.left = current;			//we take our (ex)parent's spot as their child
				} else grandparent.right = current;
			}
		}
		System.out.println();
	}

	public int remove(){
		if (root == null){
			return 0;			//for now, if the heap is empty we'll return 0. this is wrong
		}
		//save the current root's value to return later
		int value = root.val;
		//we want to move the last node to the root, so get the last node
		Node last = getLast();
		//disconnect the last node from its parent
		Node lastParent = last.parent;
		if (last == lastParent.left){
			lastParent.left = null;
		} else {
			lastParent.right = null;
		}
		//the root's children are now the last node's children
		if (root.left != null){
			last.left = root.left;
			root.left.parent = last;
		}
		if (root.right != null){
			last.right = root.right;
			root.right.parent = last;
		}
		//the last node is now the root, root has no parent
		last.parent = null;
		root = last;
		//bubble the new root downwards to maintain heap integrity
		bubbleDown(root);
		//return the previous root's value
		return value;
	}

	private void bubbleDown(Node n){
		Node current = n;
		Node elder = eldest(current);
		while ((elder != null) && (elder.val > current.val)) {
			//swap current with elder
			System.out.println("bubbledown: swap "+ current.val + " with "+ elder.val);
			
			Node copy = clone(elder);

			if (elder.left != null){
				elder.left.parent = current;
			}
			if (elder.right != null){
				elder.right.parent = current;
			}

			if (elder == current.left){
				if (current.right != null){
					current.right.parent = elder;
				}
				elder.parent = current.parent;
				elder.left = current;
				elder.right = current.right;
			} else {
				if (current.left != null){
					current.left.parent = elder;
				}
				elder.parent = current.parent;
				elder.left = current.left;
				elder.right = current;
			}

			if (current.parent != null){
				if (current == current.parent.left){
					current.parent.left = elder;
				} else current.parent.right = elder;
			}

			current.parent = elder;
			current.left = copy.left;
			current.right = copy.right;

			if (elder.parent == null){
				System.out.println("setting " + elder.val + " as root");
				root = elder;
			}
			elder = eldest(current);
		}
		System.out.println();
	}

	//returns the larger of the given node's children
	private Node eldest(Node n){
		if ((n.left == null) && (n.right == null)) {
			return null;
		}
		if ((n.left != null) && (n.right == null)) {
			return n.left;
		}
		if ((n.left == null) && (n.right != null)) {
			return n.right;
		}
		if ((n.left.val > n.right.val)) {
			return n.left;
		} else {
			return n.right;
		}
	}

	//breadth first search that returns the first node that has room for children
	private static Node available(Node n){
		Node check = n;
		LinkedList<Node> q = new LinkedList<Node>();
		while (check != null){
			if (check.left == null || check.right == null){
				return check;
			}
			q.add(check.left);
			q.add(check.right);
			check = q.pollFirst();
		}
		return null;
	}

	//level order traversal adding each node we pass to the stack,
	//once we've traversed the entire tree, the last node should be on top of the stack
	private Node getLast(){
		Node check = root;
		LinkedList<Node> q = new LinkedList<Node>();
		Stack<Node> s = new Stack<Node>();
		s.push(check);
		while (check != null){
			if (check.left != null){
				s.push(check.left);
			}
			if (check.right != null){		//since the right child comes after the left child in levelorder
				s.push(check.right);		//we check it second so that it will be on top of the left child on the stack
			} 
			q.add(check.left);
			q.add(check.right);
			check = q.pollFirst();
		}
		if (s.peek() != null){
			return s.pop();					//return the top of the stack, which will be the last node we visited
		}
		return null;
	}

	//get minimum value in heap
	public int getMin(){
		return 0;
	}

	//return a clone of the given node
	public Node clone(Node n){
		Node impostor = new Node();
		impostor.parent = n.parent;
		impostor.left = n.left;
		impostor.right = n.right;
		impostor.val = n.val;
		return impostor;
	}

	public void preorder() {
		System.out.println("preorder:");
		printPreorder(root);
		System.out.println();
	}

	public void inorder(){
		System.out.println("inorder:");
		printInorder(root);
		System.out.println();
	}

	public void postorder(){
		System.out.println("postorder:");
		printPostorder(root);
		System.out.println();
	}

	public void levelorder(){
		System.out.println("levelorder: ");
		printLevelorder();
		System.out.println();
	}

	private static void printPreorder(Node root){
		if (root == null){
			return;
		}
		System.out.print(root.val + " ");
		printPreorder(root.left);
		printPreorder(root.right);
	}

	private static void printInorder(Node root){
		if (root == null){
			return;
		}
		printInorder(root.left);
		System.out.print(root.val + " ");
		printInorder(root.right);
	}

	private static void printPostorder(Node root){
		if (root == null){
			return;
		}
		printPostorder(root.left);
		printPostorder(root.right);
		System.out.print(root.val + " ");
	}

	private void printLevelorder(){
		if (root == null){
			return;
		}
		Node n = root;
		LinkedList<Node> q = new LinkedList<Node>();
		while (n != null){
			System.out.print(n.val + " ");
			q.add(n.left);
			q.add(n.right);
			n = q.pollFirst();
		}
	}

	class Node{
		Node parent;
		Node left;
		Node right;
		int val;
		Node(int v) {val = v;}
		Node() {}

		public void info(){
			System.out.print("val: " + val); System.out.print("\n");
			if (parent != null){
				System.out.print("parent: " + parent.val);
				if (this == parent.left){
					System.out.print(", parent's left: "+parent.left.val);
				} else {System.out.print(", parent's right: "+parent.right.val);}
			} else {System.out.print("parent: null");} System.out.print("\n");
			
			if (left != null){
				System.out.print("left: " + left.val + ", left's parent: "+left.parent.val);
			} else {System.out.print("left: null");} System.out.print("\n");
			
			if (right != null){
				System.out.print("right: " + right.val + ", right's parent: "+right.parent.val);
			} else {System.out.print("right: null");} System.out.print("\n");
		}
	}
}