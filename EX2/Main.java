import java.io.*;
import java.util.*;

public class Main {

    static int sign(int a) {
        return a > 0 ? 1 : (a < 0 ? -1 : 0);
    }

    static int[][] removeCollinear(int[][] p) {
        int n = p.length;
        List<int[]> r = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int[] a = p[(i - 1 + n) % n], b = p[i], c = p[(i + 1) % n];
            if ((a[0] == b[0] && b[0] == c[0]) || (a[1] == b[1] && b[1] == c[1])) continue;
            r.add(b);
        }
        return r.toArray(new int[0][]);
    }

    static long signedArea2(int[][] p) {
        long s = 0;
        for (int i = 0; i < p.length; i++) {
            int j = (i + 1) % p.length;
            s += (long) p[i][0] * p[j][1] - (long) p[j][0] * p[i][1];
        }
        return s;
    }

    static int[][] inset(int[][] p, int h) {
        int n = p.length;
        int[][] off = new int[n][2];
        for (int i = 0; i < n; i++) {
            int[] a = p[i], b = p[(i + 1) % n];
            int dx = sign(b[0] - a[0]), dy = sign(b[1] - a[1]);
            off[i][0] = a[0] - dy * h;
            off[i][1] = a[1] + dx * h;
        }
        int[][] r = new int[n][2];
        for (int i = 0; i < n; i++) {
            int prev = (i - 1 + n) % n;
            if (p[prev][1] == p[i][1]) {
                r[i][0] = off[i][0];
                r[i][1] = off[prev][1];
            } else {
                r[i][0] = off[prev][0];
                r[i][1] = off[i][1];
            }
        }
        return r;
    }

    static boolean valid(int[][] orig, int[][] in) {
        int n = orig.length;
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            int odx = sign(orig[j][0] - orig[i][0]), ody = sign(orig[j][1] - orig[i][1]);
            int dx = in[j][0] - in[i][0], dy = in[j][1] - in[i][1];
            if (sign(dx) != odx || sign(dy) != ody || Math.abs(dx) + Math.abs(dy) < 1) return false;
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder all = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) all.append(line).append('\n');
        StringTokenizer tk = new StringTokenizer(all.toString());

        int n = Integer.parseInt(tk.nextToken());
        int[][] p = new int[n][2];
        for (int i = 0; i < n; i++) {
            p[i][0] = Integer.parseInt(tk.nextToken()) * 10;
            p[i][1] = Integer.parseInt(tk.nextToken()) * 10;
        }

        p = removeCollinear(p);
        if (signedArea2(p) < 0) {
            for (int i = 0, j = p.length - 1; i < j; i++, j--) {
                int[] t = p[i];
                p[i] = p[j];
                p[j] = t;
            }
        }

        int minEdge = Integer.MAX_VALUE;
        for (int i = 0; i < p.length; i++) {
            int j = (i + 1) % p.length;
            minEdge = Math.min(minEdge, Math.abs(p[j][0] - p[i][0]) + Math.abs(p[j][1] - p[i][1]));
        }

        long best = 0;
        for (int h = 1; minEdge - 2 * h >= 1; h++) {
            int[][] in = inset(p, h);
            if (!valid(p, in)) continue;
            best = Math.max(best, signedArea2(in) / 2 * h);
        }

        System.out.print(String.format(Locale.US, "%.2f", best / 1000.0));
    }
}
