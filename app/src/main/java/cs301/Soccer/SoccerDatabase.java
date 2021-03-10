package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** Aashish Anand ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database = new Hashtable<String, SoccerPlayer>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        if(database.containsKey(firstName + " ## " + lastName)){
            return false;
        }
        else{
            SoccerPlayer newPlayer = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
            database.put(firstName + " ## " + lastName, newPlayer);
            return true;
        }
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        if (database.containsKey(firstName + " ## " + lastName)){
            database.remove(firstName + " ## " + lastName);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        String findKey = firstName + " ## " + lastName;
        return database.get(findKey);
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        String playerName = firstName + " ## " + lastName;
        if (database.containsKey(playerName)){
            database.get(playerName).bumpGoals();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        String playerName = firstName + " ## " + lastName;
        if (database.containsKey(playerName)){
            database.get(playerName).bumpYellowCards();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        String playerName = firstName + " ## " + lastName;
        if (database.containsKey(playerName)){
            database.get(playerName).bumpRedCards();
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        if(teamName == null){
            return database.size();
        }
        else{
            int playerCount = 0;
            Collection<SoccerPlayer> soccerPlayerSet = database.values();
            for(SoccerPlayer soccerPlayer: soccerPlayerSet){
                if(soccerPlayer.getTeamName().equals(teamName)){
                    playerCount++;
                }
            }
            return playerCount;
        }
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        if(idx < numPlayers(teamName)) {
            Collection<SoccerPlayer> playerCollection = database.values();
            int playerCount = 0;
            for(SoccerPlayer player: playerCollection){
                if(teamName == null){
                    if(playerCount == idx) {
                        return player;
                    }
                    playerCount++;
                }
                else if(player.getTeamName().equals(teamName)){
                    if(playerCount == idx) {
                        return player;
                    }
                    playerCount++;
                }
            }
        }
        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String firstName = scanner.next();
                String lastName = scanner.next();
                String teamName = scanner.next();
                int uniformNumber = scanner.nextInt();
                int goals = scanner.nextInt();
                int yellowCards = scanner.nextInt();
                int redCards = scanner.nextInt();
                scanner.nextLine();

                SoccerPlayer player = new SoccerPlayer(firstName,lastName,uniformNumber,teamName);

                for (int i = 0; i < goals; i++){
                    player.bumpGoals();
                }

                for (int i = 0; i < yellowCards; i++){
                    player.bumpYellowCards();
                }

                for (int i = 0; i < redCards; i++){
                    player.bumpRedCards();
                }

                database.put(firstName + " ## " + lastName, player);
            }
            scanner.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("Reading error", e.getMessage());
            return false;
        }
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        try {
            PrintWriter pw = new PrintWriter(file);
            for (SoccerPlayer player : database.values()){
                String stats = String.format("%s %s %s %d %d %d %d", player.getFirstName(),
                        player.getLastName(), player.getTeamName(), player.getUniform(),
                        player.getGoals(), player.getYellowCards(), player.getRedCards());
                pw.println(logString(stats));
            }
            pw.close();
            return true;
        } catch (FileNotFoundException fnf){
            Log.e("write file", fnf.getMessage());
            return false;
        }
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
//        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        HashSet<String> teams = new HashSet<String>();
        for(SoccerPlayer soccerPlayer: database.values()){
            teams.add(soccerPlayer.getTeamName());
        }
        return teams;
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
