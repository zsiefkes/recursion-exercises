
public class Main {

	public Main() {
		System.out.println(factorial(1));
		System.out.println(factorial(2));
		System.out.println(factorial(3));
		System.out.println(factorial(4));
		System.out.println(factorial(5));
		System.out.println(factorial(6));
		System.out.println(factorial(7));
		System.out.println(factorial(8));
		System.out.println(factorial(9));
		System.out.println(factorial(10));
		fibonacci(10);
		System.out.println("Digit from non digit char: " + Character.getNumericValue('a'));
	}
	
	public int factorial(int n) {
		// base case
		if (n == 1) {
			return 1;
		}
		// recursion call
		return n * factorial(n - 1);
	}
	
	public int fibonacci(int n) {
		// base case
		if (n == 0 || n == 1) {
			System.out.println("Fib(" + n + ") = 1");
			return 1;
		}
		// recursive call
		int next = n + fibonacci(n - 1);
		System.out.println("Fib(" + n + ") = " + next);
		return next;
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
