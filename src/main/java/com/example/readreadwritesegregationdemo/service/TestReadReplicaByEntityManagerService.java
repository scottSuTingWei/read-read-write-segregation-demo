package com.example.readreadwritesegregationdemo.service;

import java.util.List;

import com.example.readreadwritesegregationdemo.db.entity.Users;


public interface TestReadReplicaByEntityManagerService {

    public Users updateUser(int id, String newName);

    public Users findUserById(int i);

    public List<Users> findUsersWithNameJPQL(String name);
    
    public List<Users> findUsersWithNameCriteria(String name);
    
    public List<Users> findUsersWithNameNative(String name);
    
    
}

