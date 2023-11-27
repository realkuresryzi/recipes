package cz.muni.fi.pv168.project.persistance.dao;


import cz.muni.fi.pv168.project.persistance.db.ConnectionHandler;

import java.util.function.Supplier;

@FunctionalInterface
public interface DaoSupplier<D> {
    DataAccessObject<D> get(Supplier<ConnectionHandler> connections);
}


