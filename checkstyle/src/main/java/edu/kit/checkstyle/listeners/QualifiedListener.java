package edu.kit.checkstyle.listeners;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import edu.kit.checkstyle.util.StringUtil;

public class QualifiedListener extends NopListener {
    @Override
    public void addError(final AuditEvent event) {
        System.out.println(StringUtil
                .join(":", event.getSourceName(), event.getFileName(), Integer.toString(event.getLine()),
                        Integer.toString(event.getColumn()), event.getMessage()));
    }
}
