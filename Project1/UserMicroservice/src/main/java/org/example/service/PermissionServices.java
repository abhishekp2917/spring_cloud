package org.example.service;

import org.example.model.UtbPermission;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.Query;
import java.util.List;

@Service
public class PermissionServices {

    @Autowired
    SessionFactory sessionFactory;

    public List<UtbPermission> getPermissions() {
        List<UtbPermission> permissions = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("FROM UtbPermission");
        permissions = query.getResultList();
        transaction.commit();
        session.close();
        return permissions;
    }
}
