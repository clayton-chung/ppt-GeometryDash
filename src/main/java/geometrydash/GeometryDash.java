package geometrydash;

import java.util.HashSet;
import java.util.Set;

public class GeometryDash {
    /**
     * Returns whether the given level can be completed using the given play.
     * @param level is not null and not empty
     * @param play is not null and not empty
     * @return true if the play completes the level and false otherwise
     *
     */
    public static boolean isSuccessfulPlay(String level, String play) {
        char[] levelArray = level.toCharArray();
        char[] playArray = play.toCharArray();

        if (levelArray[0] == '^' || levelArray[levelArray.length-1] == '^'){
            return false;
        }

        int i = 0;
        for (char move : playArray) {
            if (move == '1' && i < levelArray.length - 1) {
                i += 1;
            } else if (move == '2' && i < levelArray.length - 2) {
                i += 2;
            } else if (move == '3' && i < levelArray.length - 3) {
                i += 3;
            } else if (move == '0') {
            } else {
                return false;
            }
            if (levelArray[i] == '^') {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the subset of plays which can complete the given level ending
     * with the target resting energy
     * @param level is not null and not empty
     * @param possiblePlays is not null
     * @param startingEnergy the energy at the start of the level
     * @param targetRestingEnergy the minimum energy to end the level at
     * @return a subset of {@code possiblePlays} which complete the level with
     * {@code targetRestingEnergy} units of energy remaining
     */
    public static Set<String> successfulPlays(String level, Set<String> possiblePlays,
                                              int startingEnergy, int targetRestingEnergy) {
        Set<String> success = new HashSet<>();

        for (String play : possiblePlays) {
            if (valid(level, play, startingEnergy, targetRestingEnergy)) {
                success.add(play);
            }
        }

        return success;
    }

    private static boolean valid (String level, String possiblePlay,
                                 int energy, int targetRestingEnergy){

        char[] levelArray = level.toCharArray();
        char[] playArray = possiblePlay.toCharArray();

        if (levelArray[0] == '^' || levelArray[levelArray.length-1] == '^'){
            return false;
        }

        int i = 0;
        for (char move : playArray) {
            if (move == '1' && i < levelArray.length - 1) {
                i += 1;
                energy-=1;
            } else if (move == '2' && i < levelArray.length - 2) {
                i += 2;
                energy-=2;
            } else if (move == '3' && i < levelArray.length - 3) {
                i += 3;
                energy-=3;
            } else if (move == '0' && energy < 3) {
                energy += 1;
            } else if (move == '0'){
                // do nothing
            } else {
                return false;
            }

            if (levelArray[i] == '*') {
                i+=4;
            } else if (levelArray[i] == '^' || energy < 0){
                return false;
            }
        }
        return energy>=targetRestingEnergy && i == levelArray.length-1;
    }

    /**
     * Returns the shortest play that completes the given level
     * @param level is not null and not empty
     * @param startingEnergy the energy at the start of the level
     * @param targetRestingEnergy the minimum energy to end the level at
     * @return the shortest play that allows a player to complete the given level
     * @throws UnplayableLevelException if no play can complete the level
     */
    public static String shortestPlay(String level, int startingEnergy, int targetRestingEnergy)
            throws UnplayableLevelException {
        String shortest = generatePlay(level, startingEnergy, targetRestingEnergy, "",0);
        if (shortest.equals("N/A")){
            throw new UnplayableLevelException();
        }
        return shortest;
    }

    private static String generatePlay(String level, int energy, int targetEnergy, String play, int levelIndex){
        if (levelIndex >= level.length()) {
            return "N/A";
        }

        if (levelIndex == level.length()-1){
            if (energy>=targetEnergy){
                return play;
            } else {
                return "N/A";
            }
        }

        if (energy < 0){
            return "N/A";
        }

        if (level.charAt(levelIndex) == '*') {
            return generatePlay(level, energy, targetEnergy, play, levelIndex + 4);
        } else if (level.charAt(levelIndex) == '^'){
            return "N/A";
        }

        String play0 = energy < 3? generatePlay(level, energy+1, targetEnergy, play+"0", levelIndex):"N/A";
        String play1 = generatePlay(level, energy-1, targetEnergy, play+"1", levelIndex+1);
        String play2 = generatePlay(level, energy-2, targetEnergy, play+"2", levelIndex+2);
        String play3 = generatePlay(level, energy-3, targetEnergy, play+"3", levelIndex+3);

        String shortest = "N/A";
        for (String plays : new String[]{play0,play1,play2,play3}){
            if(!plays.equals("N/A") && (shortest.equals("N/A") || plays.length() < shortest.length())){
                shortest = plays;
            }
        }
        return shortest;
    }

    /**
     * Returns the total number of plays which allow a player to complete the given level
     * @param level is not null and not empty
     * @param startingEnergy the energy at the start of the level
     * @param targetRestingEnergy the minimum energy to end the level at
     * @return the total number of plays which allow a player to complete the given level
     * with target resting energy {@code targetRestingEnergy}
     */
    public static int numberOfPlays(String level, int startingEnergy, int targetRestingEnergy) {
        return countPlays(level, startingEnergy, targetRestingEnergy, 0);
    }

    private static int countPlays(String level, int energy, int targetEnergy, int levelIndex) {
        if (levelIndex >= level.length()) {
            return 0;
        }

        if (levelIndex == level.length() - 1) {
            return (energy >= targetEnergy) ? 1 : 0;
        }

        if (energy < 0) {
            return 0;
        }

        if (level.charAt(levelIndex) == '*') {
            return countPlays(level, energy, targetEnergy, levelIndex + 4);
        } else if (level.charAt(levelIndex) == '^') {
            return 0;
        }

        int count0 = energy < 3 ? countPlays(level, energy + 1, targetEnergy, levelIndex) : 0;
        int count1 = countPlays(level, energy - 1, targetEnergy, levelIndex + 1);
        int count2 = countPlays(level, energy - 2, targetEnergy, levelIndex + 2);
        int count3 = countPlays(level, energy - 3, targetEnergy, levelIndex + 3);

        return count0 + count1 + count2 + count3;
    }
}
