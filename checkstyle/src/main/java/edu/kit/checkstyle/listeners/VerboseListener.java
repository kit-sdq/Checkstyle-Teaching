package edu.kit.checkstyle.listeners;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class VerboseListener extends AutomaticBean implements AuditListener {

    private PrintWriter writer;
    private boolean closeOut;
    private int totalErrors;
    private int fileErrors;

    public VerboseListener() {
        this.writer = new PrintWriter(System.out);
        this.closeOut = false;
    }

    @Override
    protected void finishLocalSetup() {
    }

    public void auditStarted(AuditEvent e) {
        this.totalErrors = 0;
        this.writer.println("Audit started.\n");
    }

    public void fileStarted(AuditEvent e) {
        this.fileErrors = 0;
        this.writer.println("Started checking file \'" + e.getFileName() + "\'.");
    }

    public void auditFinished(AuditEvent e) {
        this.writer.println("Audit finished. Total errors: " + this.totalErrors);
        this.writer.flush();
        if (this.closeOut) {
            this.writer.close();
        }
    }

    public void fileFinished(AuditEvent e) {
        this.writer.println("Finished checking file \'" + e.getFileName() + "\'. Errors: " + this.fileErrors);
        this.writer.println();
    }

    public void addError(AuditEvent e) {
        this.printEvent(e);
        if (SeverityLevel.ERROR.equals(e.getSeverityLevel())) {
            ++this.fileErrors;
            ++this.totalErrors;
        }
    }

    public void setFile(String fileName) throws FileNotFoundException {
        this.writer = new PrintWriter(new FileOutputStream(fileName));
        this.closeOut = true;
    }

    public void addException(AuditEvent e, Throwable aThrowable) {
        System.out.println("An exception occured!");
        aThrowable.printStackTrace(System.out);
        ++this.fileErrors;
        ++this.totalErrors;
    }

    private void printEvent(AuditEvent e) {
        String msg = this.errorMsg(e);
        this.writer.println(msg);
    }

    private String errorMsg(AuditEvent e) {
        try {
            String[] ex = e.getFileName().split("/");
            int line = e.getLine();
            String source = line != 0 ? (String) FileUtils.readLines(new File(e.getFileName())).get(line - 1) : "<?>";
            String indent = StringUtils.leftPad("", e.getColumn() - 1);
            String var10000;
            if (ex.length > 0) {
                var10000 = ex[0];
            } else {
                var10000 = "";
            }

            List parts = Arrays.asList(
                    new String[]{ex[ex.length - 1], ":", Integer.toString(line), ": ", e.getSeverityLevel().toString(),
                            ": ", e.getMessage(), ";\n\t", source, "\n\t", indent, "^"});
            return StringUtils.join(parts.iterator(), "");
        } catch (IOException var8) {
            var8.printStackTrace();
            return "";
        }
    }
}
