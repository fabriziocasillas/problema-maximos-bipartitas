import java.io.*;
import java.util.*;

public class MatchingBipartito {

    // Tamaños de las particiones X y Y
    static int numX;
    static int numY;

    // Aristas que salen de cada vértice de X hacia Y
    static ArrayList<Integer>[] aristas;

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Uso: java MatchingBipartito archivo.txt");
            return;
        }

        try {
            leerArchivo(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("Error: No se encontró el archivo " + args[0]);
            return;
        }

        int[][] flujo = edmondsKarp();

        mostrarResultado(flujo);
    }

    // Leer archivo

    @SuppressWarnings("unchecked")
    static void leerArchivo(String nombreArchivo)
            throws FileNotFoundException {

        Scanner sc = new Scanner(new File(nombreArchivo));

        numX = sc.nextInt();
        numY = sc.nextInt();
        sc.nextLine();

        aristas = new ArrayList[numX + 1];

        for (int i = 1; i <= numX; i++) {
            aristas[i] = new ArrayList<>();
        }

        for (int i = 1; i <= numX; i++) {
            String linea = sc.nextLine();
            Scanner lectorLinea = new Scanner(linea);
            while (lectorLinea.hasNextInt()) {
                int verticeY = lectorLinea.nextInt();
                if (verticeY >= 1 && verticeY <= numY) {
                    aristas[i].add(verticeY);
                }
            }
            lectorLinea.close();
        }

        sc.close();
    }

    // Edmonds-Karp

    static int[][] edmondsKarp() {

        int s = 0;
        int t = numX + numY + 1;
        int totalNodos = t + 1;

        int[][] capResidual = new int[totalNodos][totalNodos];

        // Fuente -> X

        for (int i = 1; i <= numX; i++) {
            capResidual[s][i] = 1;
        }

        // X -> Y

        for (int i = 1; i <= numX; i++) {

            for (int verticeY : aristas[i]) {

                int nodoY = numX + verticeY;

                capResidual[i][nodoY] = 1;
            }
        }

        // Y -> t

        for (int i = 1; i <= numY; i++) {
            int nodoY = numX + i;
            capResidual[nodoY][t] = 1;
        }

        int[][] flujo = new int[totalNodos][totalNodos];

        while (true) {
            int[] padre = new int[totalNodos];
            Arrays.fill(padre, -1);
            padre[s] = s;

            Queue<Integer> cola = new LinkedList<>();
            cola.add(s);
            boolean encontrado = false;

            while (!cola.isEmpty() && !encontrado) {
                int u = cola.poll();
                for (int v = 0; v < totalNodos; v++) {
                    if (padre[v] == -1 &&
                        capResidual[u][v] > 0) {
                        padre[v] = u;
                        if (v == t) {
                            encontrado = true;
                            break;
                        }
                        cola.add(v);
                    }
                }
            }

            if (padre[t] == -1)
                break;

            // Todas las capacidades son unitarias,
            // por lo que el aumento siempre es 1.

            int aumento = 1;
            int v = t;
            while (v != s) {

                int u = padre[v];

                flujo[u][v] += aumento;
                flujo[v][u] -= aumento;

                capResidual[u][v] -= aumento;
                capResidual[v][u] += aumento;

                v = u;
            }
        }

        return flujo;
    }

    // Resultado

    static void mostrarResultado(int[][] flujo) {

        int s = 0;

        int flujoTotal = 0;

        for (int i = 1; i <= numX; i++) {
            flujoTotal += flujo[s][i];
        }

        System.out.println();
        System.out.println("=== APAREAMIENTO MÁXIMO BIPARTITO ===");
        System.out.println("Tamaño del matching: " + flujoTotal);

        System.out.println();
        System.out.println("Aristas del matching:");

        boolean[] asignadoX = new boolean[numX + 1];
        boolean[] asignadoY = new boolean[numY + 1];

        for (int x = 1; x <= numX; x++) {
            for (int y = 1; y <= numY; y++) {
                int nodoY = numX + y;
                if (flujo[x][nodoY] == 1) {
                    System.out.println(
                            "  X" +
                            numeroALetra(x) +
                            " -> Y" +
                            y);

                    asignadoX[x] = true;
                    asignadoY[y] = true;
                }
            }
        }

        ArrayList<String> libresX = new ArrayList<>();

        for (int i = 1; i <= numX; i++) {
            if (!asignadoX[i]) {
                libresX.add(
                        "X" + numeroALetra(i));
            }
        }

        if (!libresX.isEmpty()) {

            System.out.println();
            System.out.println(
                    "Vértices de X sin aparear: "
                    + String.join(", ", libresX));
        }

        ArrayList<String> libresY = new ArrayList<>();

        for (int i = 1; i <= numY; i++) {

            if (!asignadoY[i]) {
                libresY.add("Y" + i);
            }
        }

        if (!libresY.isEmpty()) {

            System.out.println(
                    "Vértices de Y sin aparear: "
                    + String.join(", ", libresY));
        }

        System.out.println();
        System.out.println("--- Optimalidad ---");
        System.out.println(
                "El algoritmo terminó sin encontrar "
                + "más caminos aumentantes.");
        System.out.println(
                "Por el Teorema de Flujo Máximo/"
                + "Corte Mínimo, el flujo obtenido "
                + "es máximo y, por tanto, el "
                + "apareamiento encontrado es máximo.");
    }

    // Conversión 1->A, 2->B,...

    static String numeroALetra(int n) {

        StringBuilder sb = new StringBuilder();

        while (n > 0) {
            n--;
            sb.insert(0,
                    (char) ('A' + (n % 26)));
            n /= 26;
        }

        return sb.toString();
    }
}