package solver;

public class ECMNode {
    
    ECMNode left;
    ECMNode right;
    ECMNode up;
    ECMNode down;
    ColNode col;

    //Constructor for col nodes
    ECMNode()
    {
        left = this;
        right = this;
        up = this;
        down = this;
    }

    //Constructor for row nodes
    ECMNode(ColNode col)
    {
        left = this;
        right = this;
        up = this;
        down = this;
        this.col = col;
    }

    // Create a link between this and the node passed in, this <=> node
    ECMNode linkNewDown(ECMNode node)
    {
        node.down = this.down;
        node.down.up = node;
        node.up = this;
        this.down = node;
        return node;
    }

    // Create a link between this and the node passed in, this <=> node
    ECMNode linkNewRight(ECMNode node)
    {
        node.right = this.right;
        node.right.left = node;
        node.left = this;
        this.right = node;
        return node;
    }

    // Cover a node, linking nodes either side of it
    void unlinkHorizontal()
    {
        this.left.right = this.right;
        this.right.left = this.left;
    }

    // Uncover a node, by relinking it in between the nodes either side of it
    void linkHorizontal()
    {
        this.left.right = this;
        this.right.left = this;
    }

    // Cover a node, linking nodes above and below it 
    void unlinkVertical()
    {
        this.up.down = this.down;
        this.down.up = this.up;
    }

    // Uncover a node, by relinking it in between the nodes above and below it
    void linkVertical()
    {
        this.up.down = this.down;
        this.down.up = this.up;
    }
}
