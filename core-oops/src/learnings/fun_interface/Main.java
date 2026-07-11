package learnings.fun_interface;

public class Main {

    public static void main(String[] args) {

        Operation add = (a, b) -> a+b;
        System.out.println(add.add(10, 20));
    }
}
