package lk.ijse.dep8.orm;

import lk.ijse.dep8.orm.annotations.Entity;
import lk.ijse.dep8.orm.exception.InvalidTableException;

import java.util.ArrayList;
import java.util.List;

public class DepSessionFactory {

    private final List<Class<?>> entityClassList = new ArrayList<>();

    public DepSessionFactory addAnnotatedClass(Class<?> entityClass) {
        if (entityClass.getDeclaredAnnotation(Entity.class) == null) {
            throw new InvalidTableException("Invalid Entity Class");
        }
        entityClassList.add(entityClass);
        return this;
    }

}
