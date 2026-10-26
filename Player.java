/**
 * Player class that plays the game.
 */
public class Player extends Thread{
    /**
     * Name of the player.
     */
    private String name;
    /**
     * The host of the game.
     */
    private Emcee emcee;

    /**
     * the chair the player is sitting on
     */
    private volatile Chair chair;

    /**
     * An int value that represent the chair the player wants to sit in.
     */
    private int hover;

    /**
     * Sets the player name, sets his host for the game, and gives him a chair to "hover".
     * @param emcee the host of the game.
     * @param p his player number and his name.
     */
    public Player(Emcee emcee, int p){
        name = "P"+(p+1);
        this.emcee = emcee;
        chair = null;
        if(p==0){
            hover = 0;
        }else{
            hover = p-1;
        }
    }

    /**
     * Returns the player name.
     * @return the player name.
     */
    public String toString(){
        return name;
    }

    /**
     * Attempts to take a seat at the given chair.
     * Returns if the player successfully sat down on the chair.
     * Note:
     * Yes, I don't need to return anything,
     * but if it works, don't touch it.
     * @param chair the chair to sit on.
     * @return if the player successfully sat down on the chair.
     */
    public boolean takeSeat(Chair chair){
        boolean sat = false;
        if(chair.open()){
            sat = chair.sit(this);
        }
        return sat;
    }

    /**
     * Runs until the player is told to sit on the floor.
     * While running, waits for the music to end and "circles" the available chairs.
     * when the music ends, sit down current chair or any chair that is available.
     * If they get a chair, the host will reset them by removing the chair.
     * If they fail to get a chair, the host will kill them by giving them the floor.
     */
    public void run() {
        while(chair != emcee.getGround()) {
				//System.out.println("BEGIN "+name+" player");
            while (emcee.musicOn && chair != emcee.getGround()) {
                hover = (hover + 1) % emcee.openChairs();
            }
            //System.out.println("Hello from "+this);
            while (chair == null && chair != emcee.getGround()) {
                takeSeat(emcee.chairAtIndex(hover));
                hover = (hover + 1) % emcee.openChairs();
            }
				//while(chair != emcee.getGround() && chair != null){}
        }
		  //System.out.println(this + " died");
    }

    /**
     * return true if player is sitting down, false otherwise.
     * @return true if player is sitting down, false otherwise.
     */
    public boolean hasChair(){
        return chair != null;
    }

    /**
     * Sits the player down on a seat.
     * @param c the chair the player sits down on.
     */
    public void sit(Chair c){
        this.chair = c;
    }

    /**
     * returns the chair the player is sitting on.
     * @return the chair the player is sitting on.
     */
    public Chair getChair(){
        return chair;
    }

    /**
     * Resets the player by removing his chair and having him hover the first chair.
     */
    public void reset(){
        chair = null;
        hover = 0;
    }
}
