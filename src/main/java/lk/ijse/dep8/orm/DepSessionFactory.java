package lk.ijse.dep8.orm;

import lk.ijse.dep8.orm.annotations.Entity;
import lk.ijse.dep8.orm.exception.InvalidTableException;
import lk.ijse.dep8.orm.exception.NoConnectionException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DepSessionFactory {

    private final List<Class<?>> entityClassList = new ArrayList<>();
    private Connection connection;

    public DepSessionFactory addAnnotatedClass(Class<?> entityClass) {
        if (entityClass.getDeclaredAnnotation(Entity.class) == null) {
            throw new InvalidTableException("Invalid Entity Class");
        }
        entityClassList.add(entityClass);
        return this;
    }

    public DepSessionFactory setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public DepSessionFactory build(){
        if (this.connection == null) {
            throw new NoConnectionException("Failed to build without a connection");
        }
        return this;
    }
}
