package cz.muni.fi.pv168.project.utils.exporters;

import cz.muni.fi.pv168.project.utils.DataFileObject;
import cz.muni.fi.pv168.project.utils.exporters.formats.ExporterFormatType;

public interface Exporter {
    void exportFile(String filePath, ExporterFormatType formatType);
}
