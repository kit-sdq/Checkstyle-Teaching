package kit.test;

import java.util.Set;
import java.util.TreeSet;

public class LogInterpreter {

    LoggingRobot robot = null;
    
    String lastTrace = null;
    
    enum Programs { 
        SPIRAL {
            public String toString() {
                return "'Spiral'";
            }    
        }, 
        ROOM_CROSSING {
            public String toString() {
                return "'Room-Crossing'";
            }    
        }, 
        WALL_FOLLOWING {
            public String toString() {
                return "'Wall-Following'";
            }    
        }, 
        UNTANGLE {
            public String toString() {
                return "'Untangle'";
            }    
        }, 
        CLEAN {
            public String toString() {
                return "'Clean'";
            }    
        };
        public abstract String toString();
    }
    
    public LogInterpreter(LoggingRobot robot) {
        this.robot = robot;
    }
    
    /**
     * 
     * @return trace of last call to getPossiblePrograms()
     */
    public String getLastTrace() {
        StringBuilder result = new StringBuilder();
        result.append('(');
        for (int i = 0; i < lastTrace.length(); ++i) {
            switch(lastTrace.charAt(i)) {
            case 'f': result.append("forward"); break;
            case 'u': result.append("untangle"); break;
            case 'l': result.append("left"); break;
            case 'r': result.append("right"); break;
            }
            if (i < lastTrace.length() - 1) {
                result.append(", ");
            }
        }
        result.append(')');
        return result.toString();
    }
    
    public Set<Programs> getPossiblePrograms() {
        TreeSet<Programs> set = new TreeSet<Programs>();
        String log = robot.getLog();
        lastTrace = log;
        if (possibleUntangle(log)) {
            set.add(Programs.UNTANGLE);
        }
        if (possibleClean(log)) {
            set.add(Programs.CLEAN);
        }
        if (possibleSpiral(log)) {
            set.add(Programs.SPIRAL);
        }
        if (possibleRoomCrossing(log)) {
            set.add(Programs.ROOM_CROSSING);
        }
        if (possibleWallFollowing(log)) {
            set.add(Programs.WALL_FOLLOWING);
        }
        return set;
    }
    
    private boolean possibleRoomCrossing(String log) {
	int start = 0;
	if (log.length() > start && (log.charAt(start) == 'r' || log.charAt(start) == 'l')) start++;
	if (log.length() > start && (log.charAt(start) == 'r' || log.charAt(start) == 'l')) start++;
        return countCharacterSequence(log, 'f', start) == log.length() - start;
    }
    
    private boolean possibleSpiral(String log) {
        if (log.length() == 0) {
            return true;
        }
        // spiral is uni-directional
        if (log.contains("l") && log.contains("r")) {
            return false;
        }
        
        // get direction
        char turn = 'r';
        if (log.contains("l")) turn = 'l';
        
        // strip trailing turns
        log = log.substring(countCharacterSequence(log, turn, 0));
        
        int length = 1;
        int i = 0;
        int rep = 0;
        while (i < log.length()) {
            if (i + length > log.length()) {
                return countCharacterSequence(log, 'f', i) == log.length()-i;
            }
            if (countCharacterSequence(log, 'f', i) != length) {
                return false;
            }
            i += length;
            if (i == log.length()) {
                return true;
            }
            if (countCharacterSequence(log, turn, i) != 1) {
                return false;
            }
            i++;
            rep++;
            if (rep % 2 == 0) length++;
        }
        return true;
    }
    
    public boolean possibleWallFollowing(String log) {
        if (log.length() == 0) {
            return true;
        }
        // determine direction of wall-following
        char dir = 'r';
        char opp = 'l';
        if (log.charAt(0) == 'l') {
            dir = 'l';
            opp = 'r';
        } else if (log.charAt(0) != 'r') {
            return false;
        }
        
        // determine impossible patterns
        if (log.contains("f" + opp + "f" + opp + "f")) {
            return false;
        }
        
        // determine regular pattern
        int i = 1;
        int n;
        while (i < log.length()) {
            n = countCharacterSequence(log, dir, i);
            if (n >= 4) {
                return false;
            }
            i += n;
            
            if (i >= log.length()) break;
            
            n = countCharacterSequence(log, 'f', i);
            if (n != 1) {
                return false;
            }
            i += n;
            
            String remainder = log.substring(i);
            if (remainder.length() <= 3) {
                if (countCharacterSequence(remainder, 'l', 0) == remainder.length()) {
                    return true;
                }
                if (countCharacterSequence(remainder, 'r', 0) == remainder.length()) {
                    return true;
                }
            }
            
            n = countCharacterSequence(log, opp, i);
            if (n != 1) {
                return false;
            }
            i += n;
        }
        return true;
    }
    
    private boolean possibleUntangle(String log) {
        if (log.length() == 0) {
            return false;
        }
        if (!(log.charAt(0) == 'u')) {
            return false;
        }
        if (log.contains("l") && log.contains("r")) {
            return false;
        }
        if (log.length() > 4) {
            return false;
        }
        int n = countCharacterSequence(log, 'l', 1);
        if (n > 0) {
            return n == log.length() - 1;
        } 
        n = countCharacterSequence(log, 'r', 1);
        if (n > 0) {
            return n == log.length() - 1;
        }
        return false;
    }
    
    private boolean possibleClean(String log) {
        if (log.length() == 0) {
            return false;
        }
        if (log.contains("l") && log.contains("r")) {
            return false;
        }
        if (log.length() > 8) {
            return false;
        }
        if (log.contains("ff")) {
            return false;
        }
	if (log.equals("f") && robot.tangleDetected()) {
            return true;
	}
        int n = countCharacter(log, 'l', 0);
        if (n > 4) return false;
        if (n > 0) {
            if (!robot.tangleDetected() && n != 4) return false;
            return countCharacter(log, 'f', 0) == log.length() - n;
        }
        n = countCharacter(log, 'r', 0);
        if (n > 4) return false;
        if (n > 0) {
            if (!robot.tangleDetected() && n != 4) return false;
            return countCharacter(log, 'f', 0) == log.length() - n;
        }
        return false;
    }
    
    public static int countCharacterSequence(String str, char c, int offset) {
        int count = 0;
        for (int i = offset; i < str.length(); ++i) {
            if (str.charAt(i) == c) {
                count++;
            } else {
                return count;
            }
        }
        return count;
    }
    
    public static int countCharacter(String str, char c, int offset) {
        int count = 0;
        for (int i = offset; i < str.length(); ++i) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

}
