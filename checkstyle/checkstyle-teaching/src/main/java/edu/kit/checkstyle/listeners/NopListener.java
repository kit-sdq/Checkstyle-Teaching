package edu.kit.checkstyle.listeners;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;

public class NopListener extends AutomaticBean implements AuditListener {
    @Override
    protected void finishLocalSetup() {
    }

    @Override
    public void auditStarted(final AuditEvent event) {
    }

    @Override
    public void fileStarted(final AuditEvent event) {
    }

    @Override
    public void auditFinished(final AuditEvent event) {
    }

    @Override
    public void fileFinished(final AuditEvent event) {
    }

    @Override
    public void addError(final AuditEvent event) {
    }

    @Override
    public void addException(final AuditEvent event, final Throwable e) {
    }
}