
package _edu.kit.informatik;

import static _edu.kit.informatik.Assert.*;
import static _edu.kit.informatik.Util.*;

import java.util.Iterator;
import java.util.Set;

import de.b_privat.testframework.DisplayName;
import de.b_privat.testframework.Expected;
import de.b_privat.testframework.Msg;
import de.b_privat.testframework.Param;
import de.b_privat.testframework.Test;
import edu.kit.informatik.StringUtility;

public final class TaskC {

    public static final class Public {

	    @Test(1)
        @DisplayName("reverse(String)")
        @Msg(
            {
                "String res = StringUtility.reverse(\"a\");",
                "assertEquals(res, \"a\");",
            }
        )
        public static void reverse() {
            String res = StringUtility.reverse("a");
            assertEquals(res, "a");
        }
        
        @Test(2)
        @DisplayName("isPalindrome(String)")
        @Msg(
            {
                "boolean res = StringUtility.isPalindrome(\"a\");",
                "assertEquals(res, true);",
            }
        )
        public static void isPalindrome() {
            boolean res = StringUtility.isPalindrome("a");
            assertEquals(res, true);
        }
        
        @Test(3)
        @DisplayName("removeCharacter(String, int)")
        @Msg(
            {
                "String res = StringUtility.removeCharacter(\"a\", 0);",
                "assertEquals(res, \"\");",
            }
        )
        public static void removeCharacter() {
            String res = StringUtility.removeCharacter("a", 0);
            assertEquals(res, "");
        }
        
        @Test(4)
        @DisplayName("isAnagram(String, String)")
        @Msg(
            {
                "boolean res = StringUtility.isAnagram(\"a\", \"a\");",
                "assertEquals(res, true);",
            }
        )
        public static void isAnagram() {
            boolean res = StringUtility.isAnagram("a", "a");
            assertEquals(res, true);
        }

        @Test(5)
        @DisplayName("capitalize(String)")
        @Msg(
            {
                "String res = StringUtility.capitalize(\"a\");",
                "assertEquals(res, \"A\");",
            }
        )
        public static void capitalize() {
            String res = StringUtility.capitalize("a");
            assertEquals(res, "A");
        }

        @Test(6)
        @DisplayName("countCharacter(String, char)")
        @Msg(
            {
                "int res = StringUtility.countCharacter(\"a\", 'a');",
                "assertEquals(res, 1);",
            }
        )
        public static void countCharacter() {
            int res = StringUtility.countCharacter("a", 'a');
            assertEquals(res, 1);
        }
       
    }

    public static final class Rev {
        @Test(1)
        @DisplayName("reverse easy")
        @Msg(
            {
                "String res = StringUtility.reverse(\"abc\");",
                "assertEquals(res, \"cba\");",
            }
        )
        public static void easy() {
            String res = StringUtility.reverse("abc");
            assertEquals(res, "cba");
        }
        
        @Test(2)
        @DisplayName("reverse with space")
        @Msg(
            {
                "String res = StringUtility.reverse(\"abc def\");",
                "assertEquals(res, \"abc def\");",
            }
        )
        public static void withSpaces() {
            String res = StringUtility.reverse("abc def");
            assertEquals(res, "fed cba");
        }
        
        @Test(3)
        @DisplayName("reverse with space at beginning")
        @Msg(
            {
                "String res = StringUtility.reverse(\" abc\");",
                "assertEquals(res, \" cba\");",
            }
        )
        public static void withSpaceBeginning() {
            String res = StringUtility.reverse(" abc");
            assertEquals(res, "cba ");
        }
        
        @Test(4)
        @DisplayName("reverse with space at end")
        @Msg(
            {
                "String res = StringUtility.reverse(\"abc \");",
                "assertEquals(res, \"cba \");",
            }
        )
        public static void withSpaceEnd() {
            String res = StringUtility.reverse("abc ");
            assertEquals(res, " cba");
        }
    }
    
    public static final class Pal {
        @Test(1)
        @DisplayName("palindrome")
        @Msg(
            {
                "boolean res = StringUtility.isPalindrome(\"anna\");",
                "assertEquals(res, true);",
            }
        )
        public static void palindrome() {
            boolean res = StringUtility.isPalindrome("anna");
            assertEquals(res, true);
        }
        
        @Test(2)
        @DisplayName("palindrome with special characters")
        @Msg(
            {
                "boolean res = StringUtility.isPalindrome(\"#*#\");",
                "assertEquals(res, true);",
            }
        )
        public static void palindromeSpecial() {
            boolean res = StringUtility.isPalindrome("#*#");
            assertEquals(res, true);
        }
        
        @Test(3)
        @DisplayName("not palindrome")
        @Msg(
            {
                "boolean res = StringUtility.isPalindrome(\"hallo\");",
                "assertEquals(res, false);",
            }
        )
        public static void palindromeNot() {
            boolean res = StringUtility.isPalindrome("hallo");
            assertEquals(res, false);
        }
        
        @Test(4)
        @DisplayName("not palindrome 2")
        @Msg(
            {
                "boolean res = StringUtility.isPalindrome(\"hanna\");",
                "assertEquals(res, false);",
            }
        )
        public static void palindromeNot2() {
            boolean res = StringUtility.isPalindrome("hanna");
            assertEquals(res, false);
        }
        
        @Test(5)
        @DisplayName("not palindrome 3")
        @Msg(
            {
                "boolean res = StringUtility.isPalindrome(\"annah\");",
                "assertEquals(res, false);",
            }
        )
        public static void palindromeNot3() {
            boolean res = StringUtility.isPalindrome("annah");
            assertEquals(res, false);
        }
    }
    
    public static final class Rem {
        @Test(1)
        @DisplayName("remove at end")
        @Msg(
            {
                "String res = StringUtility.removeCharacter(\"abc\", 2);",
                "assertEquals(res, \"ab\");",
            }
        )
        public static void removeEnd() {
            String res = StringUtility.removeCharacter("abc", 2);
            assertEquals(res, "ab");
        }
        
        @Test(2)
        @DisplayName("remove at beginning")
        @Msg(
            {
                "String res = StringUtility.removeCharacter(\"abc\", 0);",
                "assertEquals(res, \"bc\");",
            }
        )
        public static void removeBegin() {
            String res = StringUtility.removeCharacter("abc", 0);
            assertEquals(res, "bc");
        }
        
        @Test(3)
        @DisplayName("remove in the center")
        @Msg(
            {
                "String res = StringUtility.removeCharacter(\"abc\", 1);",
                "assertEquals(res, \"ac\");",
            }
        )
        public static void removeCenter() {
            String res = StringUtility.removeCharacter("abc", 1);
            assertEquals(res, "ac");
        }
    }
    
    public static final class Ana {
        @Test(1)
        @DisplayName("anagram")
        @Msg(
            {
                "boolean res = StringUtility.isAnagram(\"hallo\", \"olahl\");",
                "assertEquals(res, true);",
            }
        )
        public static void anagram() {
            boolean res = StringUtility.isAnagram("hallo", "olahl");
            assertEquals(res, true);
        }
        
        @Test(2)
        @DisplayName("anagram 2")
        @Msg(
            {
                "boolean res = StringUtility.isAnagram(\"123456789\", \"876549321\");",
                "assertEquals(res, true);",
            }
        )
        public static void anagram2() {
            boolean res = StringUtility.isAnagram("123456789", "876549321");
            assertEquals(res, true);
        }
        
        @Test(3)
        @DisplayName("not anagram 1")
        @Msg(
            {
                "boolean res = StringUtility.isAnagram(\"123456789_\", \"876549321\");",
                "assertEquals(res, false);",
            }
        )
        public static void anagramNot() {
            boolean res = StringUtility.isAnagram("123456789_", "876549321");
            assertEquals(res, false);
        }
        
        @Test(4)
        @DisplayName("not anagram 2")
        @Msg(
            {
                "boolean res = StringUtility.isAnagram(\"123456a89\", \"876549321\");",
                "assertEquals(res, true);",
            }
        )
        public static void anagramNot2() {
            boolean res = StringUtility.isAnagram("123456a89", "876549321");
            assertEquals(res, false);
        }
    }
    
    public static final class Cap {
        @Test(1)
        @DisplayName("capitalize")
        @Msg(
            {
                "String res = StringUtility.capitalize(\"hallo\");",
                "assertEquals(res, \"Hallo\");",
            }
        )
        public static void capitalize() {
            String res = StringUtility.capitalize("hallo");
            assertEquals(res, "Hallo");
        }
        
        @Test(2)
        @DisplayName("capitalize already capitalized")
        @Msg(
            {
                "String res = StringUtility.capitalize(\"Hallo\");",
                "assertEquals(res, \"Hallo\");",
            }
        )
        public static void capitalize2() {
            String res = StringUtility.capitalize("Hallo");
            assertEquals(res, "Hallo");
        }
        
        @Test(3)
        @DisplayName("capitalize with spaces and capital letters in the center")
        @Msg(
            {
                "String res = StringUtility.capitalize(\"hAllo TeST\");",
                "assertEquals(res, \"HAllo TeST\");",
            }
        )
        public static void capitalize3() {
            String res = StringUtility.capitalize("hAllo TeST");
            assertEquals(res, "HAllo TeST");
        }
    }
    
    public static final class Cou {
        @Test(1)
        @DisplayName("countCharacter contained")
        @Msg(
            {
                "int res = StringUtility.countCharacter(\"hallo\", 'l');",
                "assertEquals(res, 2);",
            }
        )
        public static void countCharacterContained() {
            int res = StringUtility.countCharacter("hallo", 'l');
            assertEquals(res, 2);
        }
        
        @Test(2)
        @DisplayName("countCharacter not contained")
        @Msg(
            {
                "int res = StringUtility.countCharacter(\"hallo\", 'x');",
                "assertEquals(res, 0);",
            }
        )
        public static void countCharacterContainedNot() {
            int res = StringUtility.countCharacter("hallo", 'x');
            assertEquals(res, 0);
        }
        
        @Test(3)
        @DisplayName("countCharacter border")
        @Msg(
            {
                "int res = StringUtility.countCharacter(\"ohaoollo\", 'o');",
                "assertEquals(res, 4);",
            }
        )
        public static void countCharacterContained2() {
            int res = StringUtility.countCharacter("ohaoollo", 'o');
            assertEquals(res, 4);
        }
    }
}
