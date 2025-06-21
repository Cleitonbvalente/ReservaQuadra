package com.reservaquadraoficial.reservaquadrateste.model.dao;

import com.reservaquadraoficial.reservaquadrateste.db.DB;
import com.reservaquadraoficial.reservaquadrateste.model.dao.impl.ReservaDAOJDBC;


public class DaoFactory {
    public static ReservaDAO createReservaDAO() {
        return new ReservaDAOJDBC(DB.getConnection());
    }
}