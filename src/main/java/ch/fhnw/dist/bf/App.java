package ch.fhnw.dist.bf;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        // define used variables
        double p;
        int n, m, k;

        // define parameters
        String words = "words.txt";
        p = scanProbability();

        // get strings to filter
        FileHandler f = new FileHandler();
        Set<String> filterList = f.readFilterList(words);

        // calculate parameters
        n = filterList.size();
        m = calculateM(n, p);
        k = calculateK(n, m);

        // initialize bloom-filter with strings in file
        BloomFilter bloomFilter = new BloomFilter(n, k, m);
        bloomFilter.addAll(filterList);

        // outbut parameters and test bloom-filter
        System.out.println(String.format("n: %d", n));
        System.out.println(String.format("k: %d", k));
        System.out.println(String.format("m: %d", m));
        System.out.println(String.format("p: %f", p));
        System.out.println(String.format("Tested p: %f", checkBloomFilterP(bloomFilter, filterList, 100000)));
    }

    /**
     * Read probablility p from console
     * 
     * @return Probablility p
     */
    private static double scanProbability() {
        double p;
        Scanner input = new Scanner(System.in);

        System.out.println("Enter probability of false values (%): ");
        p = input.nextDouble() / 100;
        input.close();
        return p;
    }

    /**
     * Check the accuracy of the bloom-filter with random values
     * @param bloomFilter Initialized bloom-filter with values
     * @param filterList List of values set in the bloom-filter
     * @param testSize How many testobjects should be tested
     * @return Calculated probability p
     */
    private static double checkBloomFilterP(BloomFilter bloomFilter, Set<String> filterList, int testSize) {
        Set<String> testSet = new HashSet<>();

        while (testSet.size() <= testSize) {
            String word = generateRandomWord();
            if (!filterList.contains(word))
                testSet.add(word);
        }

        int falseFound = 0;
        for (String s : testSet) {
            falseFound += bloomFilter.contains(s) ? 1 : 0;
        }

        return (double) falseFound / (double) testSize;
    }

    /**
     * Generate Random word of 10 lowercase characters
     * @return Random word
     */
    private static String generateRandomWord() {
        return generateRandomWord(10);
    }

    /**
     * Generate Random word of lowercase characters
     * @return Random word
     */
    private static String generateRandomWord(int maxLength) {
        return new Random().ints(97, 123).limit(maxLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    /**
     * Calculate optimal size for the bloom-filter m
     * @param n Number of strings in filter-set
     * @param p Wanted probability of false outbput values
     * @return Optimal size of bloom-filter
     */
    private static int calculateM(int n, double p) {
        return (int) Math.ceil((-(double) n * Math.log(p)) / (Math.pow(Math.log(2), 2)));
    }

    /**
     * Calculate optimal size of number of times to run the hashing algorythm k
     * @param n Number of strings in filter-set
     * @param m Size of bloom-filter
     * @return Number of times to run the hashing algorythm
     */
    private static int calculateK(int n, int m) {
        return (int) Math.ceil(((double) m / (double) n) * Math.log(2));
    }
}
