package cz.muni.fi.pv168.project.utils.workers;


import cz.muni.fi.pv168.project.utils.exporters.Exporter;
import cz.muni.fi.pv168.project.utils.exporters.formats.ExporterFormatType;

import javax.swing.*;

public class AsyncExporter implements Exporter {
    private final Exporter exporter;
    private Runnable onFinish;

    public AsyncExporter(Exporter exporter, Runnable onFinish) {
        this.exporter = exporter;
        this.onFinish = onFinish;
    }

    @Override
    public void exportFile(String filePath, ExporterFormatType type) {
        var swingWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                exporter.exportFile(filePath, type);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                onFinish.run();
            }
        };
        swingWorker.execute();
    }
}
