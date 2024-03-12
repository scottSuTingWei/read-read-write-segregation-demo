package com.example.readreadwritesegregationdemo.service;


import com.example.readreadwritesegregationdemo.db.entity.Users;


public interface TestReadReplicaByCustomAopService {

    public Users findByUserId(int userId);
    
    public Users updateUserEmailByUserIdAndEmail(int userId, String email);
    
}
