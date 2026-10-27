import java.io.*;
import java.util.*;

public class Main {

    static final int UL = 0, UR = 1, DL = 2, DR = 3;
    static int[][] s;

    static boolean[] arms(int x, int y) {
        boolean[] a = new boolean[4];
        for (int[] t : s) {
            int x1 = t[0], y1 = t[1], x2 = t[2], y2 = t[3];
            if (x < Math.min(x1, x2) || x > Math.max(x1, x2)) continue;
            if ((long) (x - x1) * (y2 - y1) != (long) (y - y1) * (x2 - x1)) continue;
            for (int k = 0; k < 2; k++) {
                int ex = k == 0 ? x1 : x2, ey = k == 0 ? y1 : y2;
                if (ex == x && ey == y) continue;
                int dx = Integer.signum(ex - x), dy = Integer.signum(ey - y);
                if (dy > 0) a[dx < 0 ? UL : UR] = true;
                else a[dx < 0 ? DL : DR] = true;
            }
        }
        return a;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder all = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) all.append(line).append('\n');
        StringTokenizer tk = new StringTokenizer(all.toString());

        int n = Integer.parseInt(tk.nextToken());
        s = new int[n][4];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < 4; j++) s[i][j] = Integer.parseInt(tk.nextToken());
        int x = Integer.parseInt(tk.nextToken());
        int y = Integer.parseInt(tk.nextToken());
        long energy = Long.parseLong(tk.nextToken());

        int lastDir = -1;
        while (y > 0) {
            boolean[] a = arms(x, y);
            boolean canDL = a[DL] && !a[UL];
            boolean canDR = a[DR] && !a[UR];
            int dir = -1;

            if (canDL || canDR) {
                dir = canDL ? DL : DR;
            } else if ((!a[UL] && !a[DL]) || (!a[UR] && !a[DR])) {
                y--;
                lastDir = -1;
                continue;
            } else {
                long cost = (long) x * y;
                if (energy < cost) break;
                energy -= cost;
                if (lastDir >= 0 && a[lastDir]) dir = lastDir;
                else if (a[DL]) dir = DL;
                else if (a[DR]) dir = DR;
                else {
                    y--;
                    lastDir = -1;
                    continue;
                }
            }

            if (energy < 1) break;
            energy--;
            x += dir == DL ? -1 : 1;
            y--;
            lastDir = dir;
        }

        System.out.print(x + " " + y);
    }
}
