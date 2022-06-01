package lk.ijse.dep8.orm;

import lk.ijse.dep8.orm.annotations.Entity;
import lk.ijse.dep8.orm.annotations.Id;
import lk.ijse.dep8.orm.exception.InvalidPrimaryKeyException;
import lk.ijse.dep8.orm.exception.InvalidTableException;
import lk.ijse.dep8.orm.exception.NoConnectionException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DepSessionFactory {

    private final List<Class<?>> entityClassList = new ArrayList<>();
    private Connection connection;
    Field[] fields = null;

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

    public void bootstrap() {
        if (entityClassList.isEmpty()) {
            throw new InvalidTableException("Select an entity class to continue");
        }
        for (Class<?> entity : entityClassList) {
            String tableName = entity.getDeclaredAnnotation(Entity.class).value();
                if (tableName.trim().isEmpty()) {
                    tableName = entity.getSimpleName();
                    if (!tableName.matches("[A-Za-z0-9_]")) {
                        throw new InvalidTableException("Invalid table name");
                    }
                }
                List<String> columns = new ArrayList<>();
                String primaryKey = null;

                fields = entity.getDeclaredFields();
                for (Field field:fields) {
                    Id primaryKeyField = field.getDeclaredAnnotation(Id.class);
                    if (primaryKeyField!=null) {
                        primaryKey = primaryKeyField.value();
                        if (primaryKey.trim().isEmpty()) {
                            primaryKey=field.getName();
                            if (!tableName.matches("[A-Za-z0-9_]")){
                                throw new InvalidPrimaryKeyException("Invalid primary key name");
                            }
                            continue;
                        }
                    }
                    columns.add(field.getName());
                }
                if (primaryKey==null) {
                    throw new InvalidPrimaryKeyException("Entity without a primary key");
                }
            }

        }

    /*private String createSQL(){
        for (Field field:fields) {

        }
    }*/

}
