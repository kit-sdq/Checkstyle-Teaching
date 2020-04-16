package edu.kit.checkstyle.listeners;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
import edu.kit.checkstyle.checks.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MetricListener extends AutomaticBean implements AuditListener {

    private PrintWriter writer;
    private boolean closeOut;
    private final Table metrics;
    private String currentFile;

    public MetricListener() {
        this.writer = new PrintWriter(System.out);
        this.closeOut = false;
        this.metrics = HashBasedTable.create();
        this.currentFile = "";
    }

    @Override
    protected void finishLocalSetup() {
    }

    public void auditStarted(AuditEvent e) {
    }

    public void auditFinished(AuditEvent e) {
        Iterator i$ = this.metrics.rowMap().entrySet().iterator();

        while (i$.hasNext()) {
            Entry row = (Entry) i$.next();
            Iterator i$1 = ((Map) row.getValue()).entrySet().iterator();

            while (i$1.hasNext()) {
                Entry column = (Entry) i$1.next();
                this.writer.println((String) row.getKey() + ":" + (String) column.getKey() + ":" + column.getValue());
            }
        }

        this.writer.flush();
        if (this.closeOut) {
            this.writer.close();
        }
    }

    public void fileStarted(AuditEvent e) {
        this.currentFile = e.getFileName();
    }

    public void fileFinished(AuditEvent e) {
    }

    public void addError(AuditEvent e) {
        if (SeverityLevel.INFO.equals(e.getSeverityLevel()) && e.getMessage().startsWith("metric:")) {
            String[] parts = e.getMessage().split(":");
            String metric = parts[1];
            Integer value = Integer.valueOf(parts[2]);
            List values = (List) this.metrics.get(this.currentFile, metric);
            if (values == null) {
                this.metrics.put(this.currentFile, metric, CollectionUtils.mkList(new Integer[]{value}));
            } else {
                values.add(value);
            }
        }
    }

    public void addException(AuditEvent e, Throwable throwable) {
    }

    public void setFile(String fileName) throws FileNotFoundException {
        this.writer = new PrintWriter(new FileOutputStream(fileName));
        this.closeOut = true;
    }
}
