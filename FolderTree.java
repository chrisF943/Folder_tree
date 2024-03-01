package cen4025;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.stream.Stream;

public class FolderTree {
    Node root;

    public static void main(String[] args) throws Exception {
        FolderTree tree = new FolderTree();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a file path: ");
        String path = input.nextLine();
        Path currentPath = Paths.get(path);
        tree.listDirectory(currentPath, 0);
        tree.inOrderTraverseTree(tree.root);
    }//end main

    public void listDirectory(Path path, int depth) throws  Exception {
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        if (attr.isDirectory()) {
            System.out.println(spacesForDepth(depth) + " > " + path.getFileName());
            try (Stream<Path> files = Files.list(path)) {
                long count = files.count();
                System.out.println(spacesForDepth(depth + 1) + "Number of files: " + count);
                long size = Files.walk(path).mapToLong(p -> p.toFile().length()).sum();
                System.out.println(spacesForDepth(depth + 1) + "Total size: " + size + " bytes");
                addNode(depth, path.getFileName().toString());
            }
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(path)) {
                for (Path tempPath : paths) {
                    listDirectory(tempPath, depth + 1);
                }
            }
        } else {
            System.out.println(spacesForDepth(depth) + " -- " + path.getFileName());
        }
    }//end listDir

    public static String spacesForDepth(int depth) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < depth; i++) {
            sb.append("   ");
        }
        return sb.toString();
    }//end spacesForDepth

    public void inOrderTraverseTree(Node focusNode) {
        if (focusNode != null) {
            inOrderTraverseTree(focusNode.leftChild);

            // Print only if the toString is not empty
            if (!focusNode.toString().isEmpty()) {
                System.out.println(focusNode);
            }

            inOrderTraverseTree(focusNode.rightChild);
        }
    }//end inOrderTraverseTree

    public static class Node {
        int key;
        String name;
        Node leftChild;
        Node rightChild;

        Node (int key, String name) {
            this.key = key;
            this.name = name;
        }//end constructor

        @Override
        public String toString() {
            return ("");
        }//end toString (It is empty since I don't want keys etc. to be printed)
    }//end class

    private void addNode(int key, String name) {
        Node newNode = new Node(key, name);
        if (root == null) {
            root = newNode;
        } else {
            addNodeRecursive(root, newNode);
        }
    }//end addNode

    private void addNodeRecursive(Node focusNode, Node newNode) {
        if (newNode.key < focusNode.key) {
            if (focusNode.leftChild == null) {
                focusNode.leftChild = newNode;
            } else {
                addNodeRecursive(focusNode.leftChild, newNode);
            }
        } else {
            if (focusNode.rightChild == null) {
                focusNode.rightChild = newNode;
            } else {
                addNodeRecursive(focusNode.rightChild, newNode);
            }
        }
    }//end addNodeRecursive
}//end class
