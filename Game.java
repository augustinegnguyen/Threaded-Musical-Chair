/**
 * Main method class where it only checks if there's an int input
 * and sets the amount of players to that
 * and 10 if there isn't an input.
 */
public class Game {

    /**
     * MAIN METHOD :D.
     * @param args a possible int input for the number of players.
     */
    public static void main(String[] args){
        //default numbers of players
        int players = 10;

        //checks if there is an input and if it's valid
        //if it is, then set that input to be the player number.
        if(args.length > 0){
            try {
                if(Integer.parseInt(args[0]) <= 1){
                    throw new NumberFormatException();
                }
                players = Integer.parseInt(args[0]);
            }catch(NumberFormatException e){
                System.out.println("Invalid number of players: " + args[0]);
            }
        }

        //Spawn in a god to start the game
        Emcee game = new Emcee(players);

        //Never gonna give you up, never gonna let you down . . .
        game.start();

    }
    
}
