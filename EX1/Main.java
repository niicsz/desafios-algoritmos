import java.io.*;
import java.util.*;

public class Main {

    static char[][] gravity(char[][] g) {
        int m = g.length, n = g[0].length;
        char[][] r = new char[m][n];
        for (int j = 0; j < n; j++) {
            int stones = 0;
            for (int i = 0; i < m; i++) if (g[i][j] == '*') stones++;
            for (int i = 0; i < m; i++) r[i][j] = (i >= m - stones) ? '*' : '.';
        }
        return r;
    }

    static char[][] rotateRight(char[][] g) {
        int m = g.length, n = g[0].length;
        char[][] r = new char[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[i][j] = g[m - 1 - j][i];
        return r;
    }

    static char[][] rotateLeft(char[][] g) {
        int m = g.length, n = g[0].length;
        char[][] r = new char[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[i][j] = g[j][n - 1 - i];
        return r;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder all = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) all.append(line).append('\n');
        StringTokenizer tk = new StringTokenizer(all.toString());

        int m = Integer.parseInt(tk.nextToken());
        int n = Integer.parseInt(tk.nextToken());

        char[][] g = new char[m][n];
        int idx = 0;
        while (idx < m * n) {
            for (char c : tk.nextToken().toCharArray()) {
                if (idx >= m * n) break;
                g[idx / n][idx % n] = c;
                idx++;
            }
        }

        int k = Integer.parseInt(tk.nextToken());
        g = gravity(g);
        for (int q = 0; q < k; q++) {
            String cmd = tk.nextToken().toLowerCase();
            g = cmd.equals("right") ? rotateRight(g) : rotateLeft(g);
            g = gravity(g);
        }

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < g.length; i++) {
            if (i > 0) out.append('\n');
            for (int j = 0; j < g[i].length; j++) {
                if (j > 0) out.append(' ');
                out.append(g[i][j]);
            }
        }
        System.out.print(out);
    }
}
