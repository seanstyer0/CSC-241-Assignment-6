public class Dictionary{
  public static void main(String[] args){
    String[] words = new String[] {"hi", "i", "am","gay"};

    DictEntry test = new DictEntry("test", words);
    DictEntry test2 = new DictEntry();

    test.PrintEntry();
    test2.PrintEntry();
  }
}

class DictEntry{
  private String word;
  private String[] definitions;

  public DictEntry(String word, String[] defs){
    this.word = word;
    definitions = defs;
  }

  public DictEntry(){
    word = null;
    definitions = null;
  }

  public void PrintEntry(){
    if(word == null){
      System.out.println("ERROR");
    }
    else{
      System.out.println(word);
      for(int i = 0; i < definitions.length; i++){
        System.out.println(definitions[i]);
      }
    }
  }
}

class Node{
  Node lChild;
  Node rChild;
  DictEntry entry;

  public Node(Node l, Node r, DictEntry entry){
    lChild = l;
    rChild = r;
    this.entry = entry;
  }
  public Node(){
    lChild = null;
    rChild = null;
    this.entry = null;
  }
}
