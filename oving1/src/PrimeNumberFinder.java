import java.util.ArrayList;
import java.util.Scanner;

public class PrimeNumberFinder {
    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter start of interval: ");
            int start = scanner.nextInt();

            System.out.println("Enter end of interval: ");
            int end = scanner.nextInt();

            System.out.println("Enter number of threads: ");
            int numberOfThreads = scanner.nextInt();

            int interval = (end - start) / numberOfThreads;
            int currentStart = start;
            int currentEnd = start + interval;

            ArrayList<PrimeNumberFinderThread> threads = new ArrayList<>();
            long startTime = System.nanoTime();
            for (int i = 0; i < numberOfThreads; i++) {
                PrimeNumberFinderThread thread = new PrimeNumberFinderThread(currentStart, currentEnd);
                threads.add(thread);
                thread.start();

                currentStart = currentEnd;
                currentEnd += interval;
            }

            ArrayList<Integer> primeNumbers = new ArrayList<>();
            ArrayList<Integer> primeNumberCounts = new ArrayList<>();

            for (PrimeNumberFinderThread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                primeNumbers.addAll(thread.primeNumbers);
                primeNumberCounts.add(thread.primeNumberCount);
            }
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;

            System.out.println("Prime numbers: " + primeNumbers);
            System.out.println("Duration: " + duration + "ms");
            System.out.println("Number of prime numbers: " + primeNumbers.size());
            System.out.println("Number of prime numbers per thread: " + primeNumberCounts);
        }
    }
}

