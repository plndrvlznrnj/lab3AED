package trees;

import java.util.ArrayList;
import java.util.List;

public class RedBlackTree<K extends Comparable<K>,V> extends BST<K,V>{

    // Root initialized to nil.
    private RedBlackNode<K,V> nil = new RedBlackNode<>();
    private RedBlackNode<K,V> root = getNil();

    public RedBlackTree() {
        root.setLeft(getNil());
        root.setRight(getNil());
        root.setParent(getNil());
    }

    private void leftRotate(RedBlackNode<K,V> x){

        leftRotateFixup(x);

        RedBlackNode<K,V> y;
        y = x.getRight();
        x.setRight(y.getLeft());

        // Check for existence of y.left and make pointer changes
        if (!isNil(y.getLeft()))
            y.getLeft().setParent(x);
        y.setParent(x.getParent());

        // x's parent is nul
        if (isNil(x.getParent()))
            root = y;

            // x is the left child of it's parent
        else if (x.getParent().getLeft() == x)
            x.getParent().setLeft(y);

            // x is the right child of it's parent.
        else
            x.getParent().setRight(y);

        // Finish of the leftRotate
        y.setLeft(x);
        x.setParent(y);
    }// end leftRotate(RedBlackNode x)

    private void leftRotateFixup(RedBlackNode x){

        // Case 1: Only x, x.right and x.right.right always are not nil.
        if (isNil(x.getLeft()) && isNil(x.getRight().getLeft())){
            x.setNumLeft(0);
            x.setNumRight(0);
            x.getRight().setNumLeft(1);
        }

        // Case 2: x.right.left also exists in addition to Case 1
        else if (isNil(x.getLeft()) && !isNil(x.getRight().getLeft())){
            x.setNumLeft(0);
            x.setNumRight(1 + x.getRight().getLeft().getNumLeft() +
                    x.getRight().getLeft().getNumRight());
            x.getRight().setNumLeft(2 + x.getRight().getLeft().getNumLeft() +
                    x.getRight().getLeft().getNumRight());
        }

        // Case 3: x.left also exists in addition to Case 1
        else if (!isNil(x.getLeft()) && isNil(x.getRight().getLeft())){
            x.setNumRight(0);
            x.getRight().setNumLeft(2 + x.getLeft().getNumLeft() + x.getLeft().getNumRight());

        }
        // Case 4: x.left and x.right.left both exist in addtion to Case 1
        else{
            x.setNumRight(1 + x.getRight().getLeft().getNumLeft() +
                    x.getRight().getLeft().getNumRight());
            x.getRight().setNumLeft(3 + x.getLeft().getNumLeft() + x.getLeft().getNumRight() +
                    x.getRight().getLeft().getNumLeft() + x.getRight().getLeft().getNumRight());
        }

    }

    private void rightRotate(RedBlackNode<K,V> y){

        // Call rightRotateFixup to adjust numRight and numLeft values
        rightRotateFixup(y);

        // Perform the rotate as described in the course text.
        RedBlackNode<K,V> x = y.getLeft();
        y.setLeft(x.getRight());

        // Check for existence of x.right
        if (!isNil(x.getRight()))
            x.getRight().setParent(y);
        x.setParent(y.getParent());

        // y.parent is nil
        if (isNil(y.getParent()))
            root = x;

            // y is a right child of it's parent.
        else if (y.getParent().getRight() == y)
            y.getParent().setRight(x);

            // y is a left child of it's parent.
        else
            y.getParent().setLeft(x);
        x.setRight(y);

        y.setParent(x);

    }

    private void rightRotateFixup(RedBlackNode y){

        // Case 1: Only y, y.left and y.left.left exists.
        if (isNil(y.getRight()) && isNil(y.getLeft().getRight())){
            y.setNumRight(0);
            y.setNumLeft(0);
            y.getLeft().setNumRight(1);
        }

        // Case 2: y.left.right also exists in addition to Case 1
        else if (isNil(y.getRight()) && !isNil(y.getLeft().getRight())){
            y.setNumRight(0);
            y.setNumLeft(1 + y.getLeft().getRight().getNumRight() +
                    y.getLeft().getRight().getNumLeft());
            y.getLeft().setNumRight(2 + y.getLeft().getRight().getNumRight() +
                    y.getLeft().getRight().getNumLeft());
        }

        // Case 3: y.right also exists in addition to Case 1
        else if (!isNil(y.getRight()) && isNil(y.getLeft().getRight())){
            y.setNumLeft(0);
            y.getLeft().setNumRight(2 + y.getRight().getNumRight() + y.getRight().getNumLeft());

        }

        // Case 4: y.right & y.left.right exist in addition to Case 1
        else{
            y.setNumLeft(1 + y.getLeft().getRight().getNumRight() +
                    y.getLeft().getRight().getNumLeft());
            y.getLeft().setNumRight(3 + y.getRight().getNumRight() +
                    y.getRight().getNumLeft() +
                    y.getLeft().getRight().getNumRight() + y.getLeft().getRight().getNumLeft());
        }

    }

    @Override
    public boolean insert(K key,V value) {
        insert(new RedBlackNode<K,V>(key,value));
        return true;
    }

    private void insert(RedBlackNode<K,V> z) {

        // Create a reference to root & initialize a node to nil
        RedBlackNode<K,V> y = getNil();
        RedBlackNode<K,V> x = root;

        // While we haven't reached a the end of the tree keep. Tryint to figure out where z should go
        while (!isNil(x)){
            y = x;

            // if z.key is < than the current key, go left
            if (z.getKey().compareTo(x.getKey()) < 0){

                // Update x.numLeft as z is < than x
                x.setNumLeft(x.getNumLeft() + 1);
                x = x.getLeft();
            }

            // else z.key >= x.key so go right.
            else{

                // Update x.numGreater as z is => x
                x.setNumRight(x.getNumRight() + 1);
                x = x.getRight();
            }
        }
        // y will hold z's parent
        z.setParent(y);

        if (isNil(y))
            root = z;
        else if (z.getKey().compareTo(y.getKey()) < 0)
            y.setLeft(z);
        else
            y.setRight(z);
        
        z.setLeft(getNil());
        z.setRight(getNil());
        z.setColor(RedBlackNode.RED);

        // Call insertFixup(z)
        insertFixup(z);

    }// end insert(RedBlackNode z)

    private void insertFixup(RedBlackNode<K,V> z){

        RedBlackNode<K,V> y = getNil();
        // While there is a violation of the RedBlackTree properties..
        while (z.getParent().getColor() == RedBlackNode.RED){

            // If z's parent is the the left child of it's parent.
            if (z.getParent() == z.getParent().getParent().getLeft()){

                // Initialize y to z 's cousin
                y = z.getParent().getParent().getRight();

                // Case 1: if y is red...recolor
                if (y.getColor() == RedBlackNode.RED){
                    z.getParent().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    z = z.getParent().getParent();
                }
                // Case 2: if y is black & z is a right child
                else if (z == z.getParent().getRight()){

                    // leftRotaet around z's parent
                    z = z.getParent();
                    leftRotate(z);
                }

                // Case 3: else y is black & z is a left child
                else{
                    // recolor and rotate round z's grandpa
                    z.getParent().setColor(RedBlackNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    rightRotate(z.getParent().getParent());
                }
            }

            // If z's parent is the right child of it's parent.
            else{

                // Initialize y to z's cousin
                y = z.getParent().getParent().getLeft();

                // Case 1: if y is red...recolor
                if (y.getColor() == RedBlackNode.RED){
                    z.getParent().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    z = z.getParent().getParent();
                }

                // Case 2: if y is black and z is a left child
                else if (z == z.getParent().getLeft()){
                    // rightRotate around z's parent
                    z = z.getParent();
                    rightRotate(z);
                }
                // Case 3: if y  is black and z is a right child
                else{
                    // recolor and rotate around z's grandpa
                    z.getParent().setColor(RedBlackNode.BLACK);
                    z.getParent().getParent().setColor(RedBlackNode.RED);
                    leftRotate(z.getParent().getParent());
                }
            }
        }
        // Color root black at all times
        root.setColor(RedBlackNode.BLACK);

    }// end insertFixup(RedBlackNode z)

   
    private RedBlackNode<K,V> treeMinimum(RedBlackNode<K, V> node){

        // while there is a smaller key, keep going left
        while (!isNil(node.getLeft()))
            node = node.getLeft();
        return node;
    }// end treeMinimum(RedBlackNode node)

    private RedBlackNode<K,V> treeSuccessor(RedBlackNode<K, V> x){

        // if x.left is not nil, call treeMinimum(x.right) and
        // return it's value
        if (!isNil(x.getLeft()) )
            return treeMinimum(x.getRight());

        RedBlackNode<K,V> y = x.getParent();

        // while x is it's parent's right child...
        while (!isNil(y) && x == y.getRight()){
            // Keep moving up in the tree
            x = y;
            y = y.getParent();
        }
        // Return successor
        return y;
    }// end treeMinimum(RedBlackNode x)

    private void remove(RedBlackNode<K, V> v){

        RedBlackNode<K,V> z = search(v.getKey());

        // Declare variables
        RedBlackNode<K,V> x = getNil();
        RedBlackNode<K,V> y = getNil();

        // if either one of z's children is nil, then we must remove z
        if (isNil(z.getLeft()) || isNil(z.getRight()))
            y = z;

            // else we must remove the successor of z
        else y = treeSuccessor(z);

        // Let x be the left or right child of y (y can only have one child)
        if (!isNil(y.getLeft()))
            x = y.getLeft();
        else
            x = y.getRight();

        // link x's parent to y's parent
        x.setParent(y.getParent());

        // If y's parent is nil, then x is the root
        if (isNil(y.getParent()))
            root = x;

            // else if y is a left child, set x to be y's left sibling
        else if (!isNil(y.getParent().getLeft()) && y.getParent().getLeft() == y)
            y.getParent().setLeft(x);

            // else if y is a right child, set x to be y's right sibling
        else if (!isNil(y.getParent().getRight()) && y.getParent().getRight() == y)
            y.getParent().setRight(x);

        // if y != z, trasfer y's satellite data into z.
        if (y != z){
            z.setKey(y.getKey());
        }

        fixNodeData(x,y);

        if (y.getColor() == RedBlackNode.BLACK)
            removeFixup(x);
    }// end remove(RedBlackNode z)

    private void fixNodeData(RedBlackNode<K,V> x, RedBlackNode<K,V> y){

        // Initialize two variables which will help us traverse the tree
        RedBlackNode<K,V> current = getNil();
        RedBlackNode<K,V> track = getNil();


        // if x is nil, then we will start updating at y.parent
        // Set track to y, y.parent's child
        if (isNil(x)){
            current = y.getParent();
            track = y;
        }

        // if x is not nil, then we start updating at x.parent
        // Set track to x, x.parent's child
        else{
            current = x.getParent();
            track = x;
        }

        // while we haven't reached the root
        while (!isNil(current)){
     
            if (y.getKey() != current.getKey()) {

                if (y.getKey().compareTo(current.getKey()) > 0)
                    current.setNumRight(current.getNumRight() - 1);

                if (y.getKey().compareTo(current.getKey()) < 0)
                    current.setNumLeft(current.getNumLeft() - 1);
            }

            else{
 
                if (isNil(current.getLeft()))
                    current.setNumLeft(current.getNumLeft() - 1);
                else if (isNil(current.getRight()))
                    current.setNumRight(current.getNumRight() - 1);

                else if (track == current.getRight())
                    current.setNumRight(current.getNumRight() - 1);
                else if (track == current.getLeft())
                    current.setNumLeft(current.getNumLeft() - 1);
            }

            // update track and current
            track = current;
            current = current.getParent();

        }

    }//end fixNodeData()


    private void removeFixup(RedBlackNode<K,V> x){

        RedBlackNode<K,V> w;

        // While we haven't fixed the tree completely...
        while (x != root && x.getColor() == RedBlackNode.BLACK){

            // if x is it's parent's left child
            if (x == x.getParent().getLeft()){

                // set w = x's sibling
                w = x.getParent().getRight();

                // Case 1, w's color is red.
                if (w.getColor() == RedBlackNode.RED){
                    w.setColor(RedBlackNode.BLACK);
                    x.getParent().setColor(RedBlackNode.RED);
                    leftRotate(x.getParent());
                    w = x.getParent().getRight();
                }

                // Case 2, both of w's children are black
                if (w.getLeft().getColor() == RedBlackNode.BLACK &&
                        w.getRight().getColor() == RedBlackNode.BLACK){
                    w.setColor(RedBlackNode.RED);
                    x = x.getParent();
                }
                // Case 3 / Case 4
                else{
                    // Case 3, w's right child is black
                    if (w.getRight().getColor() == RedBlackNode.BLACK){
                        w.getLeft().setColor(RedBlackNode.BLACK);
                        w.setColor(RedBlackNode.RED);
                        rightRotate(w);
                        w = x.getParent().getRight();
                    }
                    // Case 4, w = black, w.right = red
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(RedBlackNode.BLACK);
                    w.getRight().setColor(RedBlackNode.BLACK);
                    leftRotate(x.getParent());
                    x = root;
                }
            }
            // if x is it's parent's right child
            else{

                // set w to x's sibling
                w = x.getParent().getLeft();

                // Case 1, w's color is red
                if (w.getColor() == RedBlackNode.RED){
                    w.setColor(RedBlackNode.BLACK);
                    x.getParent().setColor(RedBlackNode.RED);
                    rightRotate(x.getParent());
                    w = x.getParent().getLeft();
                }

                // Case 2, both of w's children are black
                if (w.getRight().getColor() == RedBlackNode.BLACK &&
                        w.getLeft().getColor() == RedBlackNode.BLACK){
                    w.setColor(RedBlackNode.RED);
                    x = x.getParent();
                }

                // Case 3 / Case 4
                else{
                    // Case 3, w's left child is black
                    if (w.getLeft().getColor() == RedBlackNode.BLACK){
                        w.getRight().setColor(RedBlackNode.BLACK);
                        w.setColor(RedBlackNode.RED);
                        leftRotate(w);
                        w = x.getParent().getLeft();
                    }

                    // Case 4, w = black, and w.left = red
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(RedBlackNode.BLACK);
                    w.getLeft().setColor(RedBlackNode.BLACK);
                    rightRotate(x.getParent());
                    x = root;
                }
            }
        }

        x.setColor(RedBlackNode.BLACK);
    }


    private RedBlackNode<K,V> search(K key){

        // Initialize a pointer to the root to traverse the tree
        RedBlackNode<K,V> current = root;

        while (!isNil(current)){

            // If we have found a node with a key equal to key
            if (current.getKey().equals(key))

                // return that node and exit search(int)
                return current;

                // go left or right based on value of current and key
            else if (current.getKey().compareTo(key) < 0)
                current = current.getRight();

                // go left or right based on value of current and key
            else
                current = current.getLeft();
        }

        // we have not found a node whose key is "key"
        return null;


    }// end search(int key)

    public int numGreater(K key){

        return findNumGreater(root,key);

    }// end numGreater(int key)

    public int numSmaller(K key){
        return findNumSmaller(root,key);

    }// end numSmaller(int key)


    public int findNumGreater(RedBlackNode<K,V> node, K key){

        // Base Case: if node is nil, return 0
        if (isNil(node))
            return 0;
        else if (key.compareTo(node.getKey()) < 0)
            return 1+ node.getNumRight() + findNumGreater(node.getLeft(),key);

        else
            return findNumGreater(node.getRight(),key);

    }// end findNumGreater(RedBlackNode, int key)

  
    public List<K> getGreaterThan(K key, Integer maxReturned) {
        List<K> list = new ArrayList<K>();
        getGreaterThan(root, key, list);
        return list.subList(0, Math.min(maxReturned, list.size()));
    }


    private void getGreaterThan(RedBlackNode<K,V> node, K key,
                                List<K> list) {
        if (isNil(node)) {
            return;
        } else if (node.getKey().compareTo(key) > 0) {
            getGreaterThan(node.getLeft(), key, list);
            list.add(node.getKey());
            getGreaterThan(node.getRight(), key, list);
        } else {
            getGreaterThan(node.getRight(), key, list);
        }
    }

    public int findNumSmaller(RedBlackNode<K,V> node, K key){

        // Base Case: if node is nil, return 0
        if (isNil(node)) return 0;

            // If key is less than node.key, look to the left as all
            // elements on the right of node are greater than key
        else if (key.compareTo(node.getKey()) <= 0)
            return findNumSmaller(node.getLeft(),key);

        else
            return 1+ node.getNumLeft() + findNumSmaller(node.getRight(),key);

    }// end findNumSmaller(RedBlackNode nod, int key)


    @Override
    public void delete(K delKey) {
        remove(search(delKey));
    }

    @Override
    public V searchElement(K key) {
        RedBlackNode<K,V> e;
        if ((e = search(key)) == null) return null;
        return e.getValue();
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean exists(K key) {
        return search(key) != null;
    }

    private boolean isNil(RedBlackNode node){

        // return appropriate value
        return node == getNil();

    }// end isNil(RedBlackNode node)

    @Override
    public int size(){

        // Return the number of nodes to the root's left + the number of
        // nodes on the root's right + the root itself.
        return root.getNumLeft() + root.getNumRight() + 1;
    }// end size()

    @Override
    public List<K> keysInOrder(){
        if (root != null) return keysInOrder(root,new ArrayList<>());
        return null;
    }

    @Override
    public List<V> valuesInOrder(){
        if (root != null) return valuesInOrder(root,new ArrayList<>());
        return null;
    }

    @Override
    public List<Box<K,V>> pairOfElementsInOrder(){
        if (root != null) return pairOfElementsInOrder(root,new ArrayList<>());
        return null;
    }

    List<Box<K, V>> pairOfElementsInOrder(Node<K, V> root, List<Box<K, V>> list) {
        if (root.getLeft() != getNil()) pairOfElementsInOrder(root.getLeft(),list);
        list.add(root.getBox());
        if (root.getRight() != getNil()) pairOfElementsInOrder(root.getRight(),list);
        return list;
    }

    List<K> keysInOrder(Node<K, V> root, List<K> list) {
        if (root.getLeft() != getNil()) keysInOrder(root.getLeft(),list);
        list.add(root.getKey());
        if (root.getRight() != getNil()) keysInOrder(root.getRight(),list);
        return list;
    }

    List<V> valuesInOrder(Node<K, V> root, List<V> list) {
        if (root.getLeft() != getNil()) valuesInOrder(root.getLeft(),list);
        list.add(root.getValue());
        if (root.getRight() != getNil()) valuesInOrder(root.getRight(),list);
        return list;
    }

    private RedBlackNode<K, V> getNil() {
        return nil;
    }
}