package lk.ijse.dep8.orm;

import lk.ijse.dep8.orm.annotations.Entity;
import lk.ijse.dep8.orm.annotations.Id;
import lk.ijse.dep8.orm.exception.InvalidPrimaryKeyException;
import lk.ijse.dep8.orm.exception.InvalidTableException;
import lk.ijse.dep8.orm.exception.NoConnectionException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepSessionFactory {

    private final List<Class<?>> entityClassList = new ArrayList<>();
    private Connection connection;
    private List<Field>  columns = null;
    private String tableName;
    private String primaryKey = null;

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
            tableName = entity.getDeclaredAnnotation(Entity.class).value();
                if (tableName.trim().isEmpty()) {
                    tableName = entity.getSimpleName();
                    if (!tableName.matches("[A-Za-z0-9_]")) {
                        throw new InvalidTableException("Invalid table name");
                    }
                }
                columns = new ArrayList<>();


                Field[] fields = entity.getDeclaredFields();
                for (Field field:fields) {
                    Id primaryKeyField = field.getDeclaredAnnotation(Id.class);
                    if (primaryKeyField!=null) {
                        primaryKey = primaryKeyField.value();
                        if (primaryKey.trim().isEmpty()) {
                            primaryKey=field.getName();
                            if (!primaryKey.matches("[A-Za-z0-9_]")){
                                throw new InvalidPrimaryKeyException("Invalid primary key name");
                            }
                            continue;
                        }
                    }
                    columns.add(field);
                }
                if (primaryKey==null) {
                    throw new InvalidPrimaryKeyException("Entity without a primary key");
                }
            }
            executeSQL();
        }

    private void executeSQL(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(");
        for (Field column:columns) {
            String type = "";
            switch (column.getType().getTypeName()) {
                case "java.lang.String":
                    type = "VARCHAR(255)";
                    break;
                case "java.math.BigDecimal":
                    type = "DECIMAL";
                    break;
                case "java.lang.Integer":
                    type = "INT";
                default:
                    type="VARCHAR(255)";
            }
            sb.append(column).append(" "+type+", ");
            sb.append(primaryKey).append(" VARCHAR(255) PRIMARY KEY)");
            System.out.println(sb);
            try {
                Statement stm = connection.createStatement();
                stm.execute(sb.toString());
            } catch (SQLException e) {
                throw new RuntimeException("Unable to create the table");
            }

        }
    }

}
