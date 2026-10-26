/**
 * Main Brain of the assignment that creates and dirrects all player threads.
 */
public class Emcee extends Thread{
    /**
     * public volatile boolean to turn the music on and off.
     * public to allow players to "reaction" to the music turning off.
     */
    public volatile boolean musicOn;

    /**
     * List of chairs for players to sit in
     */
    private Chair[] chairs;

    /**
     * List of ALL players, both in and out of the game.
     */
    private Player[] players;

    /**
     * number to track current numbers of players.
     */
    private int numPlayers;

    /**
     * int value to track the numbers of rounds.
     */
    private int round;

    /**
     * The chair for losers to sit in.
     * A chair meant to stop players to running and sit down/out.
     */
    private Chair ground = new Chair(-1);

    /**
     * boolean value to for the announcer/emcee to wait/see when the round is over.
     */
    private volatile boolean roundOver = false;

    /**
     * Sets all the default values and creates chair and players.
     * Announces the game has started at the end.
     * @param people number of people that are playing.
     */
    public Emcee(int people){
        round = 1;
        numPlayers = people;
        musicOn = true;
        chairs = new Chair[people-1];
        players = new Player[people];

        for(int i = 0; i < people; i++){
            players[i] = new Player(this, i);
            players[i].start();
        }

        for(int i = 0; i < chairs.length; i++){
            chairs[i] = new Chair(i);
        }

        System.out.println("BEGIN "+people+" players\n");
    }

    /**
     * Run method that moderates and narrates the game until 1 player is left.
     * Order of process:
     * 1. Prints round number.
     * 2. Turns off the music and waits for players to settle down.
     * 3. Gently guides the eliminated player to the ground.
     * 4. checks if there is more than 1 player left.
     * 4.5. if so, reset and repeat.
     * 5. If not, crown the last player the winner and print closing statements.
     */
    public void run(){
        //Plays the game and repeats round until only one player is standing.
        while(true) {
            //prints round number
            System.out.println("round " + round);
            /*
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
             */

            //turns off the music
            musicOff();
            //waits until the round is over then continues
            while (!roundOver) {
                isRoundOver();
            }
            //finds the eliminated player
            Player loser = killHim();

            //waitForPrint(loser);
            //prints "player lost" statement
            if (loser != null) {
                loser.sit(this.ground);
                System.out.println(loser + " lost");
                //sits the player on the ground
					 try{
						 //to avoid when the dead player rejoins the game (bug I caught last second)
					 	loser.join();
					 }catch(InterruptedException e){}
            }
            //resets the game if there is more than 1 player left standing. Else, breaks out of the while loop
            if(numPlayers > 2) {
                resetGame();
            }else{
                break;
            }
        }
        //finds the winner
        Player champ = winner();
        //the winner wins a spot on the ground
        champ.sit(ground);
        //prints the closing statements
        System.out.println("\n"+champ + " wins!");
        System.out.println("END");
        //no one is a winner :(
		  endGame();
    }


    /**
     * Finds the player who lacks a chair.
     * @return the loser of the round.
     */
    private Player killHim(){
        for(int i = 0; i < players.length; i++){
            if(!players[i].hasChair()){
                return players[i];
            }
        }
        return null;
    }

    /**
     * Turns off the music and prints music off.
     * In the opposite order though.
     */
    private void musicOff(){
        System.out.println("music off");
        this.musicOn = false;
    }

    /**
     * Checks if all the (available) chairs are taken.
     * If so, the round is over.
     * Note:
     * This and the roundOver value may be redundant,
     * but if it works, don't touch it.
     * @return True if all seats are taken
     */
    private boolean isRoundOver(){
        for(int i = 0; i < numPlayers-1; i++){
            if(chairs[i].open()){
                return false;
            }
        }
        roundOver = true;
        return true;
    }

    /**
     * Returns chair at specified index.
     * @param index index for the chair.
     * @return chair at specified index.
     */
    public Chair chairAtIndex(int index){
        return chairs[index];
    }

    /**
     * Turns the music on and restarts the round.
     * Resets chairs and players.
     * increment the round and prints an empty line so the outputs looks nice :).
     */
    private void resetGame(){
        musicOn = true;
        roundOver = false;
        numPlayers--;

        resetChairs();
        resetPlayers();

        round++;
        System.out.println("");
    }

    /**
     * Reset all chairs that are needed for next round.
     */
    private void resetChairs(){
        for(int i = 0; i < numPlayers-1; i++){
            chairs[i].reset();
        }
    }


    /**
     * Resets all "alive" players.
     * Sets them up for the next round.
     */
    private void resetPlayers(){
        for(int i = 0; i < players.length; i++){
            if(players[i].getChair()!=ground){
                players[i].reset();
            }
        }
    }

    /**
     * Returns the available amount of chairs.
     * @return the available amount of chairs.
     */
    public int openChairs(){
        return numPlayers-1;
    }

    /**
     * Finds the last man/women/person/animal/robot/being/object standing and returns that.
     * @return player that hasn't sat on the ground.
     */
    private Player winner(){
        for(int i = 0; i < players.length; i++){
            if(players[i].getChair()!=ground){
                return players[i];
            }
        }
        return null;
    }

    /**
     * Getter method for the (loser's chair) ground
     * @return a placeholder chair (the ground)
     */
    public Chair getGround(){
        return ground;
    }

	/**
	 * Method to MAKE SURE ALL THREADS ARE DEAD
	 */
	public void endGame(){
		for(int i =0; i < players.length; i++){
			players[i].sit(this.ground);
		}
	}
}
