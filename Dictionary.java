/*
Name: Sean Styer
File: Dictionary.java
Input: FIle path to dictionary entries
Output: Definitions of desired words
*/
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Dictionary{
  public static void main(String[] args) {
    Scanner user = new Scanner(System.in);

    //create AVL tree with dict file provided in the command line, print height
    AVLTree tree = EnterWords(args[0]);
    System.out.println(tree.TreeHeight(tree.root));

    //prompt the user for input until they type "EXIT"
    while(true){
      //prompt user and collect response
      System.out.print("$ ");
      String response = user.nextLine();
      //split the response into an array of words to be tested
      String[] words = response.split(" ");

      //***FUNCTIONS***

      //exit
      if(words[0].equals("EXIT")){
        break;
      }

      //search
      else if(words[0].equalsIgnoreCase("SEARCH")){
        //look for the target word
        Node searched = tree.SearchTree(tree.root, words[1]);
        //if the word isn't in the tree, print the error
        if(searched == null){
          System.out.println(" Search for " + words[1]);
          System.out.println("WORD does not exist");
        }
        //if the word is in the tree, print its definition(s)
        else{
          searched.PrintNode();
        }
      }
      //invalid command catcher
      else{
        System.out.println("Invalid command");
      }
    }
  }
  //enter words from the Project Gutenberg dictionary into an AVL tree
  public static AVLTree EnterWords(String filePath){
    AVLTree tree = new AVLTree();
    try{
      Scanner readFile = new Scanner(new File(filePath));
      String currentLine = "";

      currentLine = readFile.nextLine();
      //pass through the introduction until the actual entries begin
      while(!currentLine.equals("*** START OF THIS PROJECT GUTENBERG EBOOK WEBSTER'S UNABRIDGED DICTIONARY ***")){
        currentLine = readFile.nextLine();
      }

      while(readFile.hasNextLine()){
        //if the line is a word to be defined
        if(currentLine.equals(currentLine.toUpperCase())){
          //enter the word into the tree
          tree.root = tree.insert(tree.root, currentLine);
          Node currentNode = AVLTree.SearchTree(tree.root,currentLine);

          //add definitions to the current word until the next word appears
          currentLine = readFile.nextLine();
          while(readFile.hasNextLine() && (!currentLine.equals(currentLine.toUpperCase()) || currentLine.equals(""))){
            currentNode.AddDefinition(currentLine);
            currentLine = readFile.nextLine();
          }
        }
      }
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    return tree;
  }
}
class Node {
    int height; //height of the node
    String key; //word to be defined
    Node left;  //left child
    Node right; //right child
    ArrayList<String> definitions;  //list of definitions of the key
    //construct a node with no definitions
    Node(String x) {
        key = x;
        height = 1;
        definitions = new ArrayList<String>();
    }
    //print the definitions of a node
    public void PrintNode(){
      System.out.println("");
      for(int i = 0; i < definitions.size(); i++){
        System.out.println(definitions.get(i));
      }
    }
    //add a definition to the node's list
    public void AddDefinition(String definition){
      definitions.add(definition);
    }
}

class AVLTree {
    Node root;

    //return the height of a node
    int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }
    //calculate the max depth/height of the AVLTree
    public int TreeHeight(Node root) {
        //reached the bottom of the tree (base case)
        if(root == null){
            return -1;
        }
        else{
            //progress deeper into the tree
            int leftDepth = TreeHeight(root.left);
            int rightDepth = TreeHeight(root.right);

            //return the longest path
            return(1+ (leftDepth > rightDepth ? leftDepth :rightDepth));
        }
    }
    //find the max of two integers
    int max(int a, int b) {
        return Math.max(a, b);
    }
    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        // perform the rotation
        x.right = y;
        y.left = T2;
        // Update the heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;
        // Return the new root
        return x;
    }
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        // perform the rotation
        y.left = x;
        x.right = T2;
        // update the heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;
        // return the new root
        return y;
    }
    // check the balance of node N
    int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }
    //insert a new node into the AVLTree and balance if needed
    Node insert(Node node, String key) {
        //normal BST insertion
        if (node == null)
            return (new Node(key));

        if (!Alphabetical(key,node.key))//key is before node.key
            node.left = insert(node.left, key);
        else if (Alphabetical(key,node.key))//key is after node.key
            node.right = insert(node.right, key);
        else // duplicate keys not allowed
            return node;

        //Update height
        node.height = 1 + max(height(node.left), height(node.right));

		//check whether this node became unbalanced
        int balance = getBalance(node);

        // cases for unbalance
        //Left Left Case
        if (balance > 1 && !(Alphabetical(key,node.left.key)))//key < node.left.key)
            return rightRotate(node);
        // Left Right Case
        if (balance > 1 && Alphabetical(key,node.left.key)){//key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // Right Right Case
        if (balance < -1 && Alphabetical(key,node.right.key))//key > node.right.key)
            return leftRotate(node);

        // Right Left Case
        if (balance < -1 && !(Alphabetical(key,node.right.key))){//key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        // return the unchanged node pointer
        return node;
    }

    //comapare two strings aphabetically
    static Boolean Alphabetical(String a, String b){
        int compare = a.compareToIgnoreCase(b);
        //a comes first
        if(compare<0)
            return false;
        //b comes first
        else if(compare>0)
            return true;
        //a and b are the same
        return false;
    }
    //search the tree for a target string
    public static Node SearchTree(Node root, String target){
      //end of tree is reached without match (base case)
      if(root == null){
        return null;
      }
      //if the matching node is found, return it
      if(target.equals(root.key)){
        return root;
      }
      //progress further down the tree
      else if(Alphabetical(root.key, target)){
        return SearchTree(root.left, target);
      }
      else if(!Alphabetical(root.key, target)){
        return SearchTree(root.right, target);
      }
      return null;
    }
}
