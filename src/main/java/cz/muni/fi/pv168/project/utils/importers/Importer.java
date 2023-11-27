package cz.muni.fi.pv168.project.utils.importers;

import cz.muni.fi.pv168.project.utils.importers.formats.ImporterFormatType;

public interface Importer {

    void importFile(String filePath, ImporterFormatType formatType);
}
