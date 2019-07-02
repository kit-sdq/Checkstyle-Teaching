package edu.kit.checkstyle.listeners;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ListListener extends AutomaticBean implements AuditListener {
    private String packageName;
    private boolean closeOut = false;
    private PrintWriter writer = new PrintWriter(System.out);

    @Override
    protected void finishLocalSetup() {
    }

    @Override
    public void auditStarted(final AuditEvent event) {
    }

    @Override
    public void fileStarted(final AuditEvent event) {
        this.packageName = "";
    }

    @Override
    public void fileFinished(final AuditEvent event) {
        this.packageName = null;
    }

    @Override
    public void auditFinished(final AuditEvent event) {
        writer.flush();

        if (closeOut) {
            writer.close();
        }
    }

    @Override
    public void addError(final AuditEvent event) {
        switch (event.getSourceName()) {
            case "edu.kit.checkstyle.checks.lists.PackageListCheck":
                packageName = event.getMessage() + ".";
                writer.println("package: " + event.getMessage());
                break;
            case "edu.kit.checkstyle.checks.lists.ImportListCheck":
                writer.println("import: " + event.getMessage());
                break;
            case "edu.kit.checkstyle.checks.lists.ClassListCheck":
                writer.println("class: " + this.packageName + event.getMessage());
                break;
            case "edu.kit.checkstyle.checks.lists.EnumListCheck":
                writer.println("enum: " + this.packageName + event.getMessage());
                break;
            case "edu.kit.checkstyle.checks.lists.MethodListCheck":
                writer.println("method: " + this.packageName + event.getMessage());
                break;
            case "edu.kit.checkstyle.checks.lists.IdentifierListCheck":
                writer.println("identifier: " + event.getMessage());
                break;
            default:
                writer.println("unsupported: " + event.getMessage());
                break;
        }
    }

    @Override
    public void addException(final AuditEvent e, final Throwable throwable) {
        throwable.printStackTrace(System.out);
    }

    public void setFile(final String fileName) throws FileNotFoundException {
        writer = new PrintWriter(new FileOutputStream(fileName));
        closeOut = true;
    }
}
