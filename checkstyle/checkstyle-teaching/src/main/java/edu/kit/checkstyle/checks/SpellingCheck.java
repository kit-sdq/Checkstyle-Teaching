package edu.kit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class SpellingCheck extends AbstractCheck {

    private static final String DICT_FILENAME = "aspell.dict";
    private static final String CUSTOM_DICT_FILENAME = "custom.dict";
    private Set dictionary = new HashSet();

    private void add(String word) {
        this.dictionary.add(word.trim().toLowerCase());
    }

    public void setAllowedWords(String words) {
        String[] var5;
        int var4 = (var5 = words.split(",")).length;

        for (int var3 = 0; var3 < var4; ++var3) {
            String word = var5[var3];
            this.add(word);
        }
    }

    public void init() {
        File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        String dictPath = jarFile.getParent() + File.separator + "aspell.dict";
        String customDictPath = jarFile.getParent() + File.separator + "custom.dict";

        try {
            this.populateDictionaryFromFile(dictPath);
        } catch (FileNotFoundException var6) {
            throw new RuntimeException("Could not find dictionary file " + dictPath);
        }

        try {
            this.populateDictionaryFromFile(customDictPath);
        } catch (FileNotFoundException var5) {
            ;
        }
    }

    private void populateDictionaryFromFile(String pathToDictionary) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathToDictionary)));

        String line;
        try {
            while ((line = br.readLine()) != null) {
                this.add(line);
            }
        } catch (IOException var6) {
            throw new RuntimeException("Dictionary not readable", var6);
        }

        try {
            br.close();
        } catch (IOException var5) {
            throw new RuntimeException("Dictionary not closeable", var5);
        }
    }

    public int[] getDefaultTokens() {
        return new int[]{9, 10, 14, 15, 154, 155, 21, 16, 157, 161};
    }

    @Override
    public int[] getRequiredTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    private DetailAST identifier(DetailAST ast) {
        for (DetailAST current = ast.getFirstChild(); current != null; current = current.getNextSibling()) {
            if (current.getType() == 58) {
                return current;
            }
        }

        return null;
    }

    private static String capitalized(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private static boolean isAttributeDefinition(DetailAST ast) {
        return ast.getType() == 10 && ast.getParent().getParent().getType() == 14;
    }

    private static String definitionType(DetailAST ast) {
        if (isAttributeDefinition(ast)) {
            return "Attribute";
        } else {
            String type = ast.getText().toLowerCase();
            if (ast.getType() != 16) {
                type = type.substring(0, type.length() - 4);
            }

            return capitalized(type).replace("_", " ");
        }
    }

    private static String[] splitCamelCase(String camelCase) {
        return camelCase.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
    }

    private boolean isKnownWord(String candidate) {
        String normalized = candidate.trim().toLowerCase();
        return this.dictionary.contains(normalized);
    }

    private boolean isKnownWithCamelCase(String candidate) {
        String[] var5;
        int var4 = (var5 = splitCamelCase(candidate)).length;

        for (int var3 = 0; var3 < var4; ++var3) {
            String word = var5[var3];
            if (!this.isKnownWord(word)) {
                return false;
            }
        }

        return true;
    }

    private boolean isKnownWithUnderscores(String candidate) {
        String[] var5;
        int var4 = (var5 = candidate.split("_")).length;

        for (int var3 = 0; var3 < var4; ++var3) {
            String word = var5[var3];
            if (!this.isKnownWord(word)) {
                return false;
            }
        }

        return true;
    }

    public void visitToken(DetailAST ast) {
        DetailAST ident = this.identifier(ast);
        if (ident != null) {
            String id = ident.getText().replaceAll("[0-9]*$", "");
            if (!this.isKnownWithCamelCase(id)) {
                if (!this.isKnownWithUnderscores(id)) {
                    if (ast.getType() != 10 || isAttributeDefinition(ast) || id.length() > 3) {
                        this.log(ast.getLineNo(), ast.getColumnNo(), "spelling", new Object[]{definitionType(ast), id});
                    }
                }
            }
        }
    }
}
