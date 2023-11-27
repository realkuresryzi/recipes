package cz.muni.fi.pv168.project.utils.workers;

import cz.muni.fi.pv168.project.utils.importers.Importer;
import cz.muni.fi.pv168.project.utils.importers.formats.ImporterFormatType;

import javax.swing.*;

public class AsyncImporter implements Importer {

    private final Importer importer;
    private final Runnable onFinish;

    public AsyncImporter(Importer importer, Runnable onFinish) {
        this.importer = importer;
        this.onFinish = onFinish;
    }

    @Override
    public void importFile(String filePath, ImporterFormatType type) {
        var asyncWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                importer.importFile(filePath, type);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                onFinish.run();
            }
        };
        asyncWorker.execute();
    }
}
