package com.example.readreadwritesegregationdemo.service;

import org.springframework.stereotype.Service;

import com.example.readreadwritesegregationdemo.db.entity.Users;


@Service
public interface TestReadReplicaByTranactionalService {

    public Users findByUserId(int userId);

    public Users updateUserEmailByUserIdAndEmail(int userId, String email);

    public Users findByUserId_testAOP(int userId);
    
    public Users updateUserEmailByUserIdAndEmail_testAOP(int userId, String email);

}
