import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder all = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) all.append(line).append('\n');
        StringTokenizer tk = new StringTokenizer(all.toString());

        int n = Integer.parseInt(tk.nextToken());
        int[][] seg = new int[n][4];
        TreeSet<Integer> xsSet = new TreeSet<>(), ysSet = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 4; j++) seg[i][j] = Integer.parseInt(tk.nextToken());
            xsSet.add(seg[i][0]);
            xsSet.add(seg[i][2]);
            ysSet.add(seg[i][1]);
            ysSet.add(seg[i][3]);
        }

        int[] xs = toArray(xsSet), ys = toArray(ysSet);
        int nx = xs.length, ny = ys.length;

        int[][] hPre = new int[ny][nx];
        int[][] vPre = new int[nx][ny];
        boolean[][] hCov = new boolean[ny][nx];
        boolean[][] vCov = new boolean[nx][ny];

        for (int[] s : seg) {
            int x1 = Math.min(s[0], s[2]), x2 = Math.max(s[0], s[2]);
            int y1 = Math.min(s[1], s[3]), y2 = Math.max(s[1], s[3]);
            if (y1 == y2 && x1 < x2) {
                int yi = Arrays.binarySearch(ys, y1);
                for (int k = Arrays.binarySearch(xs, x1); k < Arrays.binarySearch(xs, x2); k++) hCov[yi][k] = true;
            } else if (x1 == x2 && y1 < y2) {
                int xi = Arrays.binarySearch(xs, x1);
                for (int k = Arrays.binarySearch(ys, y1); k < Arrays.binarySearch(ys, y2); k++) vCov[xi][k] = true;
            }
        }

        for (int i = 0; i < ny; i++)
            for (int k = 1; k < nx; k++) hPre[i][k] = hPre[i][k - 1] + (hCov[i][k - 1] ? 1 : 0);
        for (int i = 0; i < nx; i++)
            for (int k = 1; k < ny; k++) vPre[i][k] = vPre[i][k - 1] + (vCov[i][k - 1] ? 1 : 0);

        long count = 0;
        for (int ya = 0; ya < ny; ya++)
            for (int yb = ya + 1; yb < ny; yb++) {
                for (int x = 0; x < nx; x++) {
                    if (vPre[x][yb] - vPre[x][ya] != yb - ya) continue;
                    for (int xa = x - 1; xa >= 0; xa--) {
                        if (hPre[ya][x] - hPre[ya][xa] != x - xa || hPre[yb][x] - hPre[yb][xa] != x - xa) break;
                        if (vPre[xa][yb] - vPre[xa][ya] == yb - ya) count++;
                    }
                }
            }

        System.out.print(count);
    }

    static int[] toArray(TreeSet<Integer> set) {
        int[] a = new int[set.size()];
        int i = 0;
        for (int v : set) a[i++] = v;
        return a;
    }
}
