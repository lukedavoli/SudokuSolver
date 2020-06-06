package solver;

public class ColNode extends ECMNode
{
    int size;
    String name;

    ColNode(String name)
    {
        super();
        size = 0;
        this.name = name;
        col = this;
    }

    //Unlink this node from other nodes, but leave it attached by outgoing pointers
    void coverNode()
    {
        //Unlink it horizontally
        unlinkHorizontal();
        //Unlink vertically all row nodes satisfied by the covering of the col node
        ECMNode currColNode = this.down;
        while(currColNode != this)
        {
            ECMNode currRowNode = currColNode.right;
            while(currRowNode != currColNode)
            {
                currRowNode.unlinkVertical();
                currRowNode.col.size -= 1;
                currRowNode = currRowNode.right;
            }
            currColNode = currColNode.down;
        }
    }

    //Relink the covered node using its outgoing pointers
    void uncoverNode()
    {
        ECMNode currColNode = this.up;
        while(currColNode != this)
        {
            ECMNode currRowNode = currColNode.left;
            while(currRowNode != currColNode)
            {
                currRowNode.col.size += 1;
                currRowNode.linkVertical();
                currRowNode = currRowNode.left;
            }
            currColNode = currColNode.up;
        }
    }


}
