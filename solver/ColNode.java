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

    void coverNode()
    {
        unlinkHorizontal();
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
