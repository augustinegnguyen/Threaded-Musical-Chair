/**
 * Chair object that holds it's name and a boolean value if it's taken or not.
 */
public class Chair{
    /**
     * The name/number of the chair.
     */
    private String name;

    /**
     * Boolean value to see if the seat is "taken".
     */
    private boolean taken;

    /**
     * Names the chair based on the parameter and sets the taken status to false.
     * @param c sets the name of the chair.
     */
    public Chair(int c){
        name = "C"+(c+1);
        //player = null;
        taken = false;
    }

    /**
     * Given a player, checks if the chair is taken or not.
     * if it isn't, allow the player to sit down on this class, sets taken status to true, and return true
     * if the seat is already taken, do nothing and return false.
     * @param p the player that wants to sit down.
     * @return if the player successfully sat down.
     */
    public synchronized boolean sit(Player p){
        /*
        if(player==null){
            player = p;
            taken = true;
            return true;
        }
        */

        if(taken==false){
            p.sit(this);
            System.out.println(p.toString() + " sat in " + this.toString());
            taken = true;
            return true;
        }
        return false;
    }

    /**
     * Returns if the chair is open.
     * Doesn't do much and isn't needed, but adds a "reaction" elements to the player threads.
     * It's funny, so I'm keeping it even though it's not needed.
     * @return if the seat is open for the taking.
     */
    public boolean open(){
        return !taken;
    }

    /**
     * Returns the name of the chair.
     * @return the name of the chair
     */
    public String toString(){
        return name;
    }

    /**
     * Player gets off the chair.
     * Resets the taken status to false.
     */
    public void reset(){
        taken = false;
    }
}
