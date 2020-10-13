
public class Main {

	public Main() {
		System.out.println(factorial(4));
		System.out.println(factorial(5));
		System.out.println(factorial(6));
		System.out.println(factorial(7));
		System.out.println(factorial(8));
		System.out.println(factorial(9));
		System.out.println(factorial(10));
	}
	
	public int factorial(int n) {
		// base case
		if (n == 1) {
			return 1;
		}
		// recursion call
		return n * factorial(n - 1);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
