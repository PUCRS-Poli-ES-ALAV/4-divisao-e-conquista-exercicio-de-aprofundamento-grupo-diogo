package br.pucrs;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class App {
    public static void main(String[] args) {
        runArrayAlgorithms();
        runMultiplyAlgorithms();
    }

    private static void runArrayAlgorithms() {
        int[] sizes = new int[] { 32, 2048, 1_048_576 };

        System.out.println("Resultados - Ordenação e Máximo (tempos em ms)");
        System.out.println("Algoritmo, 32(iters), 32(ms), 2048(iters), 2048(ms), 1048576(iters), 1048576(ms)");

        // MergeSort
        StringBuilder mergeRow = new StringBuilder("MergeSort");
        for (int size : sizes) {
            long[] data = randomArray(size);
            long[] copy = Arrays.copyOf(data, data.length);
            long t0 = System.nanoTime();
            MergeSort.Stats s = MergeSort.sort(copy);
            long t1 = System.nanoTime();
            mergeRow.append(", ").append(s.comparisons).append(", ").append(nanosToMillis(t1 - t0));
        }
        System.out.println(mergeRow);

        // Max iterativo
        StringBuilder max1Row = new StringBuilder("MaxIterativo");
        for (int size : sizes) {
            long[] data = randomArray(size);
            MaxFinder.Stats s = new MaxFinder.Stats();
            long t0 = System.nanoTime();
            long v = MaxFinder.maxVal1(data, s);
            long t1 = System.nanoTime();
            // usa iterações do laço como proxy de iterações
            max1Row.append(", ").append(s.iterations).append(", ").append(nanosToMillis(t1 - t0));
            // previne eliminação por JIT
            if (v == Long.MIN_VALUE) System.out.print("");
        }
        System.out.println(max1Row);

        // Max recursivo (divisão e conquista)
        StringBuilder max2Row = new StringBuilder("MaxRecursivo");
        for (int size : sizes) {
            long[] data = randomArray(size);
            MaxFinder.Stats s = new MaxFinder.Stats();
            long t0 = System.nanoTime();
            long v = MaxFinder.maxVal2(data, 0, data.length - 1, s);
            long t1 = System.nanoTime();
            // usa comparações como proxy de iterações
            max2Row.append(", ").append(s.comparisons).append(", ").append(nanosToMillis(t1 - t0));
            if (v == Long.MIN_VALUE) System.out.print("");
        }
        System.out.println(max2Row);
        System.out.println();
    }

    private static void runMultiplyAlgorithms() {
        System.out.println("Resultados - Multiplicação por Divisão e Conquista (tempos em µs)");
        System.out.println("Bits, Operações, Chamadas, Tempo(µs)");
        int[] bits = new int[] { 4, 16, 64 };
        for (int n : bits) {
            long x = randomNBits(n);
            long y = randomNBits(n);
            Multiplier.Stats s = new Multiplier.Stats();
            long t0 = System.nanoTime();
            long r = Multiplier.multiply(x, y, n, s);
            long t1 = System.nanoTime();
            long micros = (t1 - t0) / 1_000L;
            System.out.println(n + ", " + s.operations + ", " + s.recursiveCalls + ", " + micros);
            if (r == Long.MIN_VALUE) System.out.print("");
        }
    }

    private static long[] randomArray(int size) {
        long[] arr = new long[size];
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < size; i++) {
            arr[i] = rnd.nextLong(-1_000_000_000L, 1_000_000_000L);
        }
        return arr;
    }

    private static long randomNBits(int n) {
        if (n <= 0) return 0L;
        if (n >= 63) {
            // evita overflow na geração; mantém dentro de 63 bits para caber em long positivo
            long v = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE >>> 1);
            // sorteia sinal
            return ThreadLocalRandom.current().nextBoolean() ? v : -v;
        }
        long max = (1L << n) - 1L;
        long v = ThreadLocalRandom.current().nextLong(max + 1);
        return ThreadLocalRandom.current().nextBoolean() ? v : -v;
    }

    private static long nanosToMillis(long nanos) {
        return nanos / 1_000_000L;
    }
}
