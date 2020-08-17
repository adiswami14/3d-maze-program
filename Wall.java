public class Wall extends Location
{
    private int occupant;
    public Wall(int x, int y, int occupant)
    {
        super(x,y);
        this.occupant= occupant;
    }
    public int getOccupant()
    {
        return occupant;
    }
    public void setOccupant(int occupant)
    {
        this.occupant = occupant;
    }
}