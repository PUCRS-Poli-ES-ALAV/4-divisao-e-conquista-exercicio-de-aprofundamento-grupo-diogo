package br.pucrs;

public final class Multiplier {
    public static final class Stats {
        public long recursiveCalls;
        public long operations; // soma de multiplicacoes e somas intermediarias
    }

    private Multiplier() {}

    public static long multiply(long x, long y, int n, Stats stats) {
        // assume n >= 1; trata negativos convertendo para positivos e ajustando sinal
        boolean negative = (x < 0) ^ (y < 0);
        long ax = Math.abs(x);
        long ay = Math.abs(y);
        long result = multiplyRec(ax, ay, n, stats);
        return negative ? -result : result;
    }

    private static long multiplyRec(long x, long y, int n, Stats stats) {
        stats.recursiveCalls++;
        if (n <= 1) {
            stats.operations++;
            return x * y;
        }
        int m = (n + 1) / 2; // ceil(n/2)
        long powM = 1L << m; // 2^m

        long a = x / powM;
        long b = x % powM;
        long c = y / powM;
        long d = y % powM;

        long e = multiplyRec(a, c, m, stats);
        long f = multiplyRec(b, d, m, stats);
        long g = multiplyRec(b, c, m, stats);
        long h = multiplyRec(a, d, m, stats);

        // RETURN 2^(2m)*e + 2^m*(g + h) + f.
        stats.operations += 3; // duas somas e uma combinacao de shifts

        long left = e << (2 * m);
        long middle = (g + h) << m;
        return left + middle + f;
    }
}


