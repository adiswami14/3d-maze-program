public class Explorer extends Location
{
    public Explorer(int x, int y)
    {
        super(x, y);
    }
    public void move(int movex, int movey)
    {
      setX(getX()+movex);
      setY(getY()+movey);
    }
}