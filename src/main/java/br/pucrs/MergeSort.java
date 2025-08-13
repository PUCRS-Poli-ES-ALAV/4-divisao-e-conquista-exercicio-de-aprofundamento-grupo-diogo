package br.pucrs;

import java.util.Arrays;

public final class MergeSort {
    public static final class Stats {
        public long comparisons;
        public long moves;
        public long recursiveCalls;

        @Override
        public String toString() {
            return "comparisons=" + comparisons + ", moves=" + moves + ", calls=" + recursiveCalls;
        }
    }

    private MergeSort() {}

    public static Stats sort(long[] array) {
        Stats stats = new Stats();
        if (array == null || array.length <= 1) {
            return stats;
        }
        long[] aux = Arrays.copyOf(array, array.length);
        sortRecursive(array, aux, 0, array.length - 1, stats);
        return stats;
    }

    private static void sortRecursive(long[] array, long[] aux, int left, int right, Stats stats) {
        stats.recursiveCalls++;
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        sortRecursive(array, aux, left, mid, stats);
        sortRecursive(array, aux, mid + 1, right, stats);
        // Small optimization: if already in order, skip merge
        stats.comparisons++;
        if (array[mid] <= array[mid + 1]) {
            return;
        }
        merge(array, aux, left, mid, right, stats);
    }

    private static void merge(long[] array, long[] aux, int left, int mid, int right, Stats stats) {
        // copy to aux
        for (int i = left; i <= right; i++) {
            aux[i] = array[i];
            stats.moves++;
        }

        int i = left;
        int j = mid + 1;
        int k = left;
        while (i <= mid && j <= right) {
            stats.comparisons++;
            if (aux[i] <= aux[j]) {
                array[k++] = aux[i++];
            } else {
                array[k++] = aux[j++];
            }
            stats.moves++;
        }
        while (i <= mid) {
            array[k++] = aux[i++];
            stats.moves++;
        }
        while (j <= right) {
            array[k++] = aux[j++];
            stats.moves++;
        }
    }
}


