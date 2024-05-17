import java.util.ArrayList;
import java.util.List;

public class MyHashTable<K, V> {
    private class HashNode<K, V> {
        private K key;
        private V value;
        private HashNode<K, V> next;

        private HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" + key + " " + value + "}";
        }
    }

    private HashNode<K, V>[] chainArray;
    private int M = 11;
    private int size;

    public MyHashTable() {
        chainArray = (HashNode<K, V>[]) new HashNode[M];
    }

    public MyHashTable(int M) {
        this.M = M;
        chainArray = (HashNode<K, V>[]) new HashNode[M];
    }

    private int hash(K key) {
        return key.hashCode() % M;
    }

    public void put(K key, V value) {
        int index = hash(key);
        HashNode<K, V> newNode = new HashNode<>(key, value);
        if (chainArray[index] == null) {
            chainArray[index] = newNode;
        } else {
            HashNode<K, V> currentNode = chainArray[index];
            while (currentNode.next != null) {
                if (currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    public V get(K key) {
        int index = hash(key);
        HashNode<K, V> currentNode = chainArray[index];
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    public V remove(K key) {
        int index = hash(key);
        HashNode<K, V> currentNode = chainArray[index];
        HashNode<K, V> prevNode = null;
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                if (prevNode == null) {
                    chainArray[index] = currentNode.next;
                } else {
                    prevNode.next = currentNode.next;
                }
                size--;
                return currentNode.value;
            }
            prevNode = currentNode;
            currentNode = currentNode.next;
        }
        return null;
    }

    public boolean contains(V value) {
        for (int i = 0; i < M; i++) {
            HashNode<K, V> currentNode = chainArray[i];
            while (currentNode != null) {
                if (currentNode.value.equals(value)) {
                    return true;
                }
                currentNode = currentNode.next;
            }
        }
        return false;
    }

    public K getKey(V value) {
        for (int i = 0; i < M; i++) {
            HashNode<K, V> currentNode = chainArray[i];
            while (currentNode != null) {
                if (currentNode.value.equals(value)) {
                    return currentNode.key;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }
}
import java.util.Random;

class MyTestingClass {
    private String data;

    public MyTestingClass(String data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < data.length(); i++) {
            hash = (31 * hash) + data.charAt(i);
        }
        return hash;
    }
}

public class Main {
    public static void main(String[] args) {
        MyHashTable<MyTestingClass, Integer> table = new MyHashTable<>();

       
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            String randomData = generateRandomString();
            MyTestingClass key = new MyTestingClass(randomData);
            table.put(key, i);
        }

        for (int i = 0; i < table.chainArray.length; i++) {
            int count = 0;
            HashNode<MyTestingClass, Integer> current = table.chainArray[i];
            while (current != null) {
                count++;
                current = current.next;
            }
            System.out.println("Bucket " + i + ": " + count + " elements");
        }
    }

    private static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = random.nextInt(10) + 1;
        for (int i = 0; i < length; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            sb.append(randomChar);
        }
        return sb.toString();
    }
}



public class BST<K extends Comparable<K>, V> {
    private Node root;
    private int size;

    private class Node {
        private K key;
        private V val;
        private Node left, right;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public void put(K key, V val) {
        root = put(root, key, val);
    }

    private Node put(Node x, K key, V val) {
        if (x == null) {
            size++;
            return new Node(key, val);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        return x;
    }

    public V get(K key) {
        return get(root, key);
    }

    private V get(Node x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    public void delete(K key) {
        root = delete(root, key);
    }

    private Node delete(Node x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
            size--;
        }
        return x;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        return x;
    }

    public Iterable<Node> iterator() {
        List<Node> nodes = new ArrayList<>();
        inorder(root, nodes);
        return nodes;
    }

    private void inorder(Node x, List<Node> nodes) {
        if (x == null) return;
        inorder(x.left, nodes);
        nodes.add(x);
        inorder(x.right, nodes);
    }

    public int size() {
        return size;
    }

    public class NodeEntry {
        private K key;
        private V value;

        public NodeEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
    BST<Integer, String> tree = new BST<>();
    for (BST.Node node : tree) {
        System.out.println("Key is " + node.getKey() + " and value is " + node.getValue());
    }

}
