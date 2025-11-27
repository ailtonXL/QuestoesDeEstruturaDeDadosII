public class RedBlackTree {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // Classe interna para o Nó
    class Node {
        int data;
        Node left, right, parent;
        boolean color;

        Node(int data) {
            this.data = data;
            this.color = RED; // Novos nós sempre nascem VERMELHOS
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    private Node root;

    // Helper para evitar NullPointerException ao checar cor
    private boolean getColor(Node node) {
        if (node == null) return BLACK;
        return node.color;
    }

    // Helper para setar cor com segurança
    private void setColor(Node node, boolean color) {
        if (node != null) node.color = color;
    }

    // Rotação à Esquerda
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    // Rotação à Direita
    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.right) y.parent.right = x;
        else y.parent.left = x;
        x.right = y;
        y.parent = x;
    }

    // Método principal de inserção
    public void insert(int data) {
        Node node = new Node(data);
        root = bstInsert(root, node);
        fixViolation(node);
    }

    // Inserção padrão de Árvore Binária de Busca (BST)
    private Node bstInsert(Node root, Node node) {
        if (root == null) return node;
        if (node.data < root.data) {
            root.left = bstInsert(root.left, node);
            root.left.parent = root;
        } else if (node.data > root.data) {
            root.right = bstInsert(root.right, node);
            root.right.parent = root;
        }
        return root;
    }

    // O "Coração" da Rubro-Negra: Corrige violações após inserção
    private void fixViolation(Node node) {
        Node parent = null;
        Node grandParent = null;

        // Enquanto houver violação (Pai é Vermelho e Nó é Vermelho)
        while (node != root && getColor(node.parent) == RED) {
            parent = node.parent;
            grandParent = node.parent.parent;

            // CASO A: Pai é filho esquerdo do Avô
            if (parent == grandParent.left) {
                Node uncle = grandParent.right;

                // Caso 1: Tio é VERMELHO (Apenas recolore)
                if (getColor(uncle) == RED) {
                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandParent, RED);
                    node = grandParent; // Sobe o problema para o avô resolver
                } else {
                    // Caso 2: Tio é PRETO e nó é filho à Direita (Rotação Dupla parte 1)
                    if (node == parent.right) {
                        leftRotate(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    // Caso 3: Tio é PRETO e nó é filho à Esquerda (Rotação e Recolore)
                    rightRotate(grandParent);
                    setColor(parent, BLACK);
                    setColor(grandParent, RED);
                    node = parent;
                }
            } 
            // CASO B: Pai é filho direito do Avô (Espelho do caso A)
            else {
                Node uncle = grandParent.left;

                if (getColor(uncle) == RED) {
                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandParent, RED);
                    node = grandParent;
                } else {
                    if (node == parent.left) {
                        rightRotate(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    leftRotate(grandParent);
                    setColor(parent, BLACK);
                    setColor(grandParent, RED);
                    node = parent;
                }
            }
        }
        // A raiz sempre deve ser PRETA
        root.color = BLACK;
    }

    // Impressão In-Order formatada: Valor(Cor)
    public void printInOrder() {
        System.out.print("In-Order: ");
        inOrderHelper(root);
        System.out.println();
    }

    private void inOrderHelper(Node node) {
        if (node == null) return;
        inOrderHelper(node.left);
        String colorCode = node.color == RED ? "R" : "B";
        System.out.print(node.data + "(" + colorCode + ") ");
        inOrderHelper(node.right);
    }

    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        int[] values = {8, 4, 12, 2, 6, 10, 14};

        System.out.println("=== Inserção Rubro-Negra (R=Red, B=Black) ===");
        
        for (int val : values) {
            System.out.print("Inserindo " + val + " -> ");
            tree.insert(val);
            tree.printInOrder();
        }
    }
}
