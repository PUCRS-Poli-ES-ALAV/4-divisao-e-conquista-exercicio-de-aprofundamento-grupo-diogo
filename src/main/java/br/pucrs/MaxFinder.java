package br.pucrs;

public final class MaxFinder {
    public static final class Stats {
        public long iterations;
        public long recursiveCalls;
        public long comparisons;
    }

    private MaxFinder() {}

    public static long maxVal1(long[] array, Stats stats) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("array vazio");
        }
        long max = array[0];
        for (int i = 1; i < array.length; i++) {
            stats.iterations++;
            stats.comparisons++;
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static long maxVal2(long[] array, int init, int end, Stats stats) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("array vazio");
        }
        if (init < 0 || end >= array.length || init > end) {
            throw new IllegalArgumentException("intervalo invalido");
        }
        return maxVal2Rec(array, init, end, stats);
    }

    private static long maxVal2Rec(long[] array, int init, int end, Stats stats) {
        stats.recursiveCalls++;
        if (end - init <= 0) {
            return array[init];
        }
        if (end - init == 1) {
            stats.comparisons++;
            return array[init] >= array[end] ? array[init] : array[end];
        }
        int m = (init + end) / 2;
        long v1 = maxVal2Rec(array, init, m, stats);
        long v2 = maxVal2Rec(array, m + 1, end, stats);
        stats.comparisons++;
        return v1 >= v2 ? v1 : v2;
    }
}


