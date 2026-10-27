import java.io.*;
import java.util.*;

public class Main {

    static long minGap(long lo, long hi, List<long[]> spans) {
        List<long[]> covered = new ArrayList<>();
        for (long[] s : spans) {
            long a = Math.max(s[0] + 1, lo + 1), b = Math.min(s[1] - 1, hi - 1);
            if (a <= b) covered.add(new long[]{a, b});
        }
        Collections.sort(covered, (p, q) -> Long.compare(p[0], q[0]));

        List<long[]> free = new ArrayList<>();
        long cur = lo;
        for (long[] c : covered) {
            if (c[0] > cur) free.add(new long[]{cur, c[0] - 1});
            cur = Math.max(cur, c[1] + 1);
        }
        if (cur <= hi) free.add(new long[]{cur, hi});

        long best = Long.MAX_VALUE, prev = Long.MIN_VALUE;
        for (long[] f : free) {
            if (f[1] > f[0]) return 1;
            if (prev != Long.MIN_VALUE) best = Math.min(best, f[0] - prev);
            prev = f[0];
        }
        return best;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder all = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) all.append(line).append('\n');
        StringTokenizer tk = new StringTokenizer(all.toString());

        int n = Integer.parseInt(tk.nextToken());
        List<long[]> xs = new ArrayList<>(), ys = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            long x1 = Long.parseLong(tk.nextToken()), y1 = Long.parseLong(tk.nextToken());
            long x2 = Long.parseLong(tk.nextToken()), y2 = Long.parseLong(tk.nextToken());
            xs.add(new long[]{Math.min(x1, x2), Math.max(x1, x2)});
            ys.add(new long[]{Math.min(y1, y2), Math.max(y1, y2)});
        }
        long px1 = Long.parseLong(tk.nextToken()), py1 = Long.parseLong(tk.nextToken());
        long px2 = Long.parseLong(tk.nextToken()), py2 = Long.parseLong(tk.nextToken());

        long w = minGap(Math.min(px1, px2), Math.max(px1, px2), xs);
        long h = minGap(Math.min(py1, py2), Math.max(py1, py2), ys);
        System.out.print(w * h);
    }
}
