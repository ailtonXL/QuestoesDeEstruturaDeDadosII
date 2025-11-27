import java.util.Arrays;

public class BTree {
    private int T; // Grau mínimo. Para ordem 3, usamos uma adaptação da lógica geral.
    // Na prática, Ordem 3 significa: Max Filhos = 3, Max Chaves = 2.
    private int MAX_KEYS = 2;
    private Node root;

    // Classe interna do Nó
    private class Node {
        int n; // Número atual de chaves
        int[] keys = new int[MAX_KEYS + 1]; // +1 para facilitar o split temporário
        Node[] children = new Node[MAX_KEYS + 2]; // +1 para facilitar o split
        boolean leaf = true;

        public int findKey(int k) {
            for (int i = 0; i < this.n; i++) {
                if (this.keys[i] == k) {
                    return i;
                }
            }
            return -1;
        }
    }

    public BTree() {
        root = new Node();
        root.n = 0;
        root.leaf = true;
    }

    // Método principal de inserção
    public void insert(int k) {
        Node r = root;
        // Se a raiz estiver cheia, a árvore precisa crescer em altura
        if (r.n == MAX_KEYS) {
            Node s = new Node();
            root = s;
            s.leaf = false;
            s.n = 0;
            s.children[0] = r;
            splitChild(s, 0, r);
            insertNonFull(s, k);
        } else {
            insertNonFull(r, k);
        }
    }

    // Divide o filho 'y' do nó 'x'
    private void splitChild(Node x, int i, Node y) {
        // Cria novo nó z que vai armazenar (t-1) chaves de y
        Node z = new Node();
        z.leaf = y.leaf;
        
        // Em Ordem 3 (Max Keys 2), o split é simples:
        // [A, B, C] -> B sobe. Esquerda fica A, Direita fica C.
        // A chave do meio é a de índice 1 (a segunda chave)
        
        int midIndex = 1; // Para ordem 3, dividimos no índice 1
        
        // z recebe a chave da direita (a que estava sobrando ou após o meio)
        z.n = 1; 
        z.keys[0] = y.keys[2]; // Pega a terceira chave (índice 2) que causou overflow lógico

        // Se não for folha, z recebe os filhos da direita
        if (!y.leaf) {
             z.children[0] = y.children[2];
             z.children[1] = y.children[3];
        }

        y.n = 1; // y fica apenas com a chave da esquerda (índice 0)

        // Abre espaço no pai (x) para subir a chave do meio
        for (int j = x.n; j >= i + 1; j--)
            x.children[j + 1] = x.children[j];

        x.children[i + 1] = z;

        for (int j = x.n - 1; j >= i; j--)
            x.keys[j + 1] = x.keys[j];

        x.keys[i] = y.keys[midIndex]; // Chave do meio sobe
        x.n++;
    }

    // Inserção auxiliar quando o nó não está cheio
    private void insertNonFull(Node x, int k) {
        int i = x.n - 1;

        if (x.leaf) {
            // Se é folha, acha a posição e insere (shift para direita)
            // Aqui usamos o array com espaço extra para simplificar
            while (i >= 0 && k < x.keys[i]) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }
            x.keys[i + 1] = k;
            x.n++;
        } else {
            // Se não é folha, acha qual filho deve receber a chave
            while (i >= 0 && k < x.keys[i]) {
                i--;
            }
            i++;
            // Verifica se o filho está cheio antes de descer
            if (x.children[i].n == MAX_KEYS) {
                splitChild(x, i, x.children[i]);
                if (k > x.keys[i]) {
                    i++;
                }
            }
            insertNonFull(x.children[i], k);
        }
    }

    // Método para imprimir a árvore de forma hierárquica
    public void printTree() {
        printTree(root, "");
    }

    private void printTree(Node node, String indent) {
        if (node != null) {
            System.out.print(indent);
            System.out.print("[ ");
            for (int i = 0; i < node.n; i++) {
                System.out.print(node.keys[i] + (i < node.n - 1 ? ", " : " "));
            }
            System.out.println("]");

            if (!node.leaf) {
                for (int i = 0; i <= node.n; i++) {
                    printTree(node.children[i], indent + "    ");
                }
            }
        }
    }

    public static void main(String[] args) {
        BTree bTree = new BTree();
        int[] values = {10, 20, 5, 6, 12, 30, 7, 17};

        System.out.println("=== Inserção Árvore B (Ordem 3 / Max Chaves 2) ===");
        
        for (int val : values) {
            System.out.println("Inserindo: " + val);
            bTree.insert(val);
        }

        System.out.println("\n=== Estado Final da Árvore ===");
        bTree.printTree();
    }
}
