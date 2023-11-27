package cz.muni.fi.pv168.project.ui.model;

import java.util.List;

public interface RepositoryWrapper<M> {

    public void addRow(M model);

    public void updateRow(M model);

    public void deleteRow(int rowIndex);

    public M getEntity(int rowIndex);

    public List<M> getEntities();

    public M getEntityByName(String name);

    void refresh();
}
