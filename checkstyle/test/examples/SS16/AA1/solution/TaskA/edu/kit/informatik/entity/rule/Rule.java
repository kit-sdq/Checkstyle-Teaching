package edu.kit.informatik.entity.rule;

import java.util.ArrayList;
import java.util.stream.Stream;

import edu.kit.informatik.exception.SyntaxException;

// these are predefined rules which can be passed as cli arguments
// note that they are mapped to the internal concept of rules, called Strategies!
public enum Rule {
    BACKWARD, 
    BARRIER, 
    NOJUMP,
    TRIPLY;
    
    public static final Rule getRuleByName(String pName) throws SyntaxException {
        return Stream.of(Rule.values()).filter(p -> p.toString().equals(pName)).findAny().orElseThrow(
                SyntaxException.createInstance("Error, a rule with the given name '" + pName + "' does not exist."));
    }
    
    public String getName() {
        return toString();
    }
    
    @Override
    public String toString() {
        return name().toUpperCase();
    }

    public static final Rule[] parseRules(String[] pRuleNames) throws SyntaxException {
        ArrayList<Rule> rules = new ArrayList<>();
        
        for(int i = 0; i < pRuleNames.length; i++) {
            try {
                Rule cRule = Rule.getRuleByName(pRuleNames[i]);
                
                /* remove duplicates */
                if(!rules.contains(cRule)) {
                    rules.add(cRule);
                }
            } catch(SyntaxException e) {
                /* rethrow more detailed */
                throw new SyntaxException("Error, the given string '" + pRuleNames[i] + "' could not be matched "
                        + "against a predefined rule neither it could be identified as a position identifier string "
                        + "at this argument index (" + i + "). Remember to place the positions as the last argument!");
            }
        }
        
        return rules.toArray(new Rule[rules.size()]);        
    }
    
    public static final Rule[] fromCliArgument(String[] arguments) throws SyntaxException {
        if(arguments.length == 0) {
            throw new SyntaxException("Error, need a string of identifiers to parse.");
        }
        
        String[] arr = new String[arguments.length - 1];
        System.arraycopy(arguments, 0, arr, 0, arguments.length - 1);
        
        return parseRules(arr);
    }
}
