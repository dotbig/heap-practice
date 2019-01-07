import java.lang.Math;


public class HeapTest{
	public static void main(String[] args){
		System.out.println("maxheap test");
		System.out.println();

		MaxHeap m = new MaxHeap();

		for (int i=0; i<26; i++){
			m.add((int)(Math.random()*100));
		}

		System.out.println("retrieved: "+m.remove());
		System.out.println(m.min());
		System.out.println();
		System.out.println("retrieved: "+m.remove());
		System.out.println("min: "+m.min());
		System.out.println();

		for (int i=0; i<12; i++){
			int t = m.remove();
		}

		for (int i=0; i<8; i++){
			m.add((int)(Math.random()*100));
		}

		m.levelorder();

		m.preorder();
		m.inorder();
		m.postorder();

		System.out.println("min: "+m.min());
		System.out.println("count: "+m.size());
	}
}