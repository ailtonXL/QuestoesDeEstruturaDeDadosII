public class AVLTree {

    // Classe interna para representar o Nó
    class Node {
        int key, height;
        Node left, right;

        Node(int d) {
            key = d;
            height = 1; // Novo nó é inicialmente adicionado como folha
        }
    }

    Node root;

    // Função utilitária para obter a altura da árvore
    int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }

    // Função utilitária para obter o máximo de dois inteiros
    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Rotação à direita (Right Rotation)
    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Realiza a rotação
        x.right = y;
        y.left = T2;

        // Atualiza alturas
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        // Retorna nova raiz
        return x;
    }

    // Rotação à esquerda (Left Rotation)
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Realiza a rotação
        y.left = x;
        x.right = T2;

        // Atualiza alturas
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        // Retorna nova raiz
        return y;
    }

    // Obtém o fator de balanceamento do nó N
    int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    // Função recursiva para inserir uma chave na subárvore com raiz em node
    Node insert(Node node, int key) {
        // 1. Inserção normal de BST (Árvore Binária de Busca)
        if (node == null)
            return (new Node(key));

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else // Chaves duplicadas não são permitidas
            return node;

        // 2. Atualiza a altura deste nó ancestral
        node.height = 1 + max(height(node.left), height(node.right));

        // 3. Obtém o fator de balanceamento para checar se ficou desbalanceado
        int balance = getBalance(node);

        // Se o nó ficou desbalanceado, temos 4 casos:

        // Caso Esquerda-Esquerda (Rotação simples à direita)
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // Caso Direita-Direita (Rotação simples à esquerda)
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // Caso Esquerda-Direita (Rotação dupla: esquerda depois direita)
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Caso Direita-Esquerda (Rotação dupla: direita depois esquerda)
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // Retorna o ponteiro do nó (inalterado)
        return node;
    }

    // Função para imprimir em ordem simétrica (In-Order)
    void inOrder(Node node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.key + " ");
            inOrder(node.right);
        }
    }

    // Método facilitador para chamar a inserção a partir da main
    public void insertAndPrint(int key) {
        root = insert(root, key);
        System.out.print("Inserido " + key + " -> In-order: [ ");
        inOrder(root);
        System.out.println("]");
    }

    public static void main(String[] args) {
        AVLTree tree = new AVLTree();

        System.out.println("=== Teste da Árvore AVL ===\n");

        // Valores solicitados para teste: 10, 5, 15, 3, 8, 12, 18
        int[] valores = {10, 5, 15, 3, 8, 12, 18};

        for (int valor : valores) {
            tree.insertAndPrint(valor);
        }
    }
}
