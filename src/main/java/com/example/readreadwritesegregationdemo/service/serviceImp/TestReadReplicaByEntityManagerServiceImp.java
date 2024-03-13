package com.example.readreadwritesegregationdemo.service.serviceImp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.readreadwritesegregationdemo.db.entity.Users;
import com.example.readreadwritesegregationdemo.service.TestReadReplicaByEntityManagerService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;


@Service
public class TestReadReplicaByEntityManagerServiceImp implements TestReadReplicaByEntityManagerService{

    @PersistenceContext(unitName = "primary")
    private EntityManager primaryEntityManager;

    @PersistenceContext(unitName = "replica")
    private EntityManager replicaEntityManager;


    @Override
    public Users findUserById(int id) {
        return replicaEntityManager.find(Users.class, id);
    }

    @Override
    @Transactional
    public Users updateUser(int id, String newName) {
        Users user = primaryEntityManager.find(Users.class, id);
        if (user != null) {
            user.setName(newName);
            primaryEntityManager.merge(user);
        }
        return user;
    }

    // 複雑な条件でテータを取得する方法：
    // 1. JPQL
    // 2.Criteria API
    // 3. Native SQL
    @Override
    public List<Users> findUsersWithNameJPQL(String name) {
        String jpql = "SELECT u FROM Users u WHERE u.name = :name";
        return replicaEntityManager.createQuery(jpql, Users.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<Users> findUsersWithNameCriteria(String name) {
        CriteriaBuilder cb = replicaEntityManager.getCriteriaBuilder();
        CriteriaQuery<Users> query = cb.createQuery(Users.class);
        Root<Users> user = query.from(Users.class);
        query.select(user).where(cb.equal(user.get("name"), name));
        return replicaEntityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Users> findUsersWithNameNative(String name) {
        String sql = "SELECT * FROM users WHERE name = ?";
        return replicaEntityManager.createNativeQuery(sql, Users.class)
                .setParameter(1, name)
                .getResultList();
    }
}
