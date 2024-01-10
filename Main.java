import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        int numThreads = 100;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countRightTurns(route);
                updateSizeToFreq(countR);
                System.out.println("Поток: " + Thread.currentThread().getId() +
                        " | Путь: " + route + " | Количество R: " + countR);
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printResults();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countRightTurns(String route) {
        int count = 0;
        for (char c : route.toCharArray()) {
            if (c == 'R') {
                count++;
            }
        }
        return count;
    }

    public static void updateSizeToFreq(int countR) {
        synchronized (sizeToFreq) {
            sizeToFreq.merge(countR, 1, Integer::sum);
        }
    }

    public static void printResults() {
        System.out.println("Результат:");
        int maxFreq = 0;
        for (int freq : sizeToFreq.values()) {
            maxFreq = Math.max(maxFreq, freq);
        }

        System.out.println("Самое частое количество повторений " + maxFreq + " (встретилось " +
                sizeToFreq.get(maxFreq) + " раз)");

        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getKey() != maxFreq) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }
}
