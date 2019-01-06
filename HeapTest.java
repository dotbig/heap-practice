
public class HeapTest{
	public static void main(String[] args){
		System.out.println("maxheap test");
		System.out.println();

		MaxHeap m = new MaxHeap();

		m.add(3);
		m.add(2);
		m.add(5);
		m.add(10);
		m.add(7);
		m.add(4);
		m.add(9);
		m.add(12);
		m.add(11);
		m.add(6);

		System.out.println("retrieved: "+m.remove());
		System.out.println("retrieved: "+m.remove());
		System.out.println();

		m.levelorder();

		m.preorder();
		m.inorder();
		m.postorder();
	}
}