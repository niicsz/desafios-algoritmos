import java.io.*;
import java.util.*;

public class Main {

    static Map<Long, Integer> ids = new HashMap<>();
    static List<double[]> nodes = new ArrayList<>();

    static long key(double x, double y) {
        long rx = Math.round(x * 100), ry = Math.round(y * 100);
        return rx * 1_000_000L + ry;
    }

    static int nodeId(double x, double y) {
        long k = key(x, y);
        Integer id = ids.get(k);
        if (id == null) {
            id = nodes.size();
            ids.put(k, id);
            nodes.add(new double[]{Math.round(x * 100) / 100.0, Math.round(y * 100) / 100.0});
        }
        return id;
    }

    static boolean onSegment(double[] s, double px, double py) {
        double cross = (s[2] - s[0]) * (py - s[1]) - (s[3] - s[1]) * (px - s[0]);
        if (Math.abs(cross) > 1e-9) return false;
        return px >= Math.min(s[0], s[2]) - 1e-9 && px <= Math.max(s[0], s[2]) + 1e-9
            && py >= Math.min(s[1], s[3]) - 1e-9 && py <= Math.max(s[1], s[3]) + 1e-9;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder all = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) all.append(line).append('\n');
        StringTokenizer tk = new StringTokenizer(all.toString());

        int n = Integer.parseInt(tk.nextToken());
        double[][] s = new double[n][4];
        double total = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) s[i][j] = Integer.parseInt(tk.nextToken());
            total += Math.hypot(s[i][2] - s[i][0], s[i][3] - s[i][1]);
        }

        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double[] a = s[i];
            double dx = a[2] - a[0], dy = a[3] - a[1];
            if (dx == 0 && dy == 0) continue;
            List<double[]> pts = new ArrayList<>();
            pts.add(new double[]{0, a[0], a[1]});
            pts.add(new double[]{1, a[2], a[3]});
            for (int j = 0; j < n; j++) {
                if (j == i) continue;
                double[] b = s[j];
                double ex = b[2] - b[0], ey = b[3] - b[1];
                double den = dx * ey - dy * ex;
                if (Math.abs(den) > 1e-12) {
                    double t = ((b[0] - a[0]) * ey - (b[1] - a[1]) * ex) / den;
                    double u = ((b[0] - a[0]) * dy - (b[1] - a[1]) * dx) / den;
                    if (t >= -1e-9 && t <= 1 + 1e-9 && u >= -1e-9 && u <= 1 + 1e-9)
                        pts.add(new double[]{t, a[0] + t * dx, a[1] + t * dy});
                } else {
                    for (int k = 0; k < 4; k += 2)
                        if (onSegment(a, b[k], b[k + 1])) {
                            double t = Math.abs(dx) > Math.abs(dy) ? (b[k] - a[0]) / dx : (b[k + 1] - a[1]) / dy;
                            pts.add(new double[]{t, b[k], b[k + 1]});
                        }
                }
            }
            Collections.sort(pts, (p, q) -> Double.compare(p[0], q[0]));
            int prev = -1;
            for (double[] p : pts) {
                int id = nodeId(p[1], p[2]);
                if (prev >= 0 && id != prev) edges.add(new int[]{prev, id});
                prev = id;
            }
        }

        Set<Long> seen = new HashSet<>();
        List<int[]> uniq = new ArrayList<>();
        for (int[] e : edges) {
            int u = Math.min(e[0], e[1]), v = Math.max(e[0], e[1]);
            if (seen.add((long) u * 100000 + v)) uniq.add(new int[]{u, v});
        }

        int m = nodes.size();
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < m; i++) adj.add(new ArrayList<>());
        for (int k = 0; k < uniq.size(); k++) {
            adj.get(uniq.get(k)[0]).add(k);
            adj.get(uniq.get(k)[1]).add(k);
        }

        boolean[] removed = new boolean[uniq.size()];
        int[] deg = new int[m];
        for (int i = 0; i < m; i++) deg[i] = adj.get(i).size();
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) if (deg[i] == 1) queue.add(i);
        while (!queue.isEmpty()) {
            int v = queue.poll();
            if (deg[v] != 1) continue;
            for (int k : adj.get(v)) {
                if (removed[k]) continue;
                removed[k] = true;
                deg[v]--;
                int w = uniq.get(k)[0] == v ? uniq.get(k)[1] : uniq.get(k)[0];
                if (--deg[w] == 1) queue.add(w);
            }
        }

        int start = -1;
        double perimeter = 0;
        for (int k = 0; k < uniq.size(); k++) {
            if (removed[k]) continue;
            double[] p = nodes.get(uniq.get(k)[0]), q = nodes.get(uniq.get(k)[1]);
            perimeter += Math.hypot(q[0] - p[0], q[1] - p[1]);
            if (start < 0) start = uniq.get(k)[0];
        }
        if (start < 0) {
            System.out.print("Abandoned");
            return;
        }

        boolean[] used = new boolean[uniq.size()];
        double area2 = 0;
        int cur = start;
        while (true) {
            int next = -1;
            for (int k : adj.get(cur)) {
                if (removed[k] || used[k]) continue;
                used[k] = true;
                next = uniq.get(k)[0] == cur ? uniq.get(k)[1] : uniq.get(k)[0];
                break;
            }
            if (next < 0) break;
            double[] p = nodes.get(cur), q = nodes.get(next);
            area2 += p[0] * q[1] - q[0] * p[1];
            cur = next;
            if (cur == start) break;
        }

        double kalyan = Math.abs(area2) / 2;
        double leftover = total - perimeter;
        double computer = leftover * leftover / (4 * Math.PI);

        System.out.print(kalyan >= computer ? "Kalyan" : "Computer");
    }
}
