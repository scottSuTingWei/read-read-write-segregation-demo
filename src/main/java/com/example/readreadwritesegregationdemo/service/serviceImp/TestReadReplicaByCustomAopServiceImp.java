package com.example.readreadwritesegregationdemo.service.serviceImp;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.annotation.DataSource;
import com.example.readreadwritesegregationdemo.db.entity.Users;
import com.example.readreadwritesegregationdemo.db.primary.dao.UsersRepository;
import com.example.readreadwritesegregationdemo.service.TestReadReplicaByCustomAopService;

// AOPを開けるため、カスタムのアノテーションを設置する。
@DataSource
@Service
public class TestReadReplicaByCustomAopServiceImp implements TestReadReplicaByCustomAopService{

    private final UsersRepository usersRepository;

    public TestReadReplicaByCustomAopServiceImp(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    // method名はspring.datasource.replica.patternにマッチするので、リードレプリカDBに接続する。
    @Override
    public Users findByUserId(int userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    // method名はspring.datasource.patternにマッチするので、Primary DBに接続する。
    @Override
    @Transactional()
    public Users updateUserEmailByUserIdAndEmail(int userId, String email) {
        usersRepository.updateUserEmailFoAOPTest(userId, email);
        return usersRepository.findById(userId).orElse(null);
    }
    
}
