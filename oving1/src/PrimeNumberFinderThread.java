import java.util.ArrayList;

class PrimeNumberFinderThread extends Thread {
    private int start;
    private int end;
    ArrayList<Integer> primeNumbers = new ArrayList<>();
    int primeNumberCount = 0;

    public PrimeNumberFinderThread(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            if (checkPrime(i)) {
                primeNumberCount++;
                primeNumbers.add(i);
            }
        }
    }

    public  boolean checkPrime(int n) {

        if((n > 2 && n % 2 == 0) || n == 1 || n == 0) {
            return false;
        }

        for (int i = 3; i <= (int)Math.sqrt(n); i += 2) {

            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }
}
