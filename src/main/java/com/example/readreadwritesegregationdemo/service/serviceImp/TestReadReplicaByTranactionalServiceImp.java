package com.example.readreadwritesegregationdemo.service.serviceImp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.readreadwritesegregationdemo.byDynamicDataSourceAndCustomAOP.annotation.DataSource;
import com.example.readreadwritesegregationdemo.db.entity.Users;
import com.example.readreadwritesegregationdemo.db.primary.dao.UsersRepository;
import com.example.readreadwritesegregationdemo.service.TestReadReplicaByTranactionalService;

@DataSource
@Service
public class TestReadReplicaByTranactionalServiceImp implements TestReadReplicaByTranactionalService{

    private final UsersRepository usersRepository;

    public TestReadReplicaByTranactionalServiceImp(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    // @TransactionalにreadOnly=trueを設定すると、リードレプリカ DBに接続する。
    @Override
    @Transactional(readOnly = true)
    public Users findByUserId(int userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    // @TransactionalにreadOnly=trueを設定しないまま、Primary DBに接続する。
    @Override
    @Transactional()
    public Users updateUserEmailByUserIdAndEmail(int userId, String email) {
        usersRepository.updateUserEmail(userId, email);
        return usersRepository.findById(userId).orElse(null);
    }
    

    // method名はspring.datasource.replica.patternにマッチするので、リードレプリカ DBに接続する。
    @Override
    public Users findByUserId_testAOP(int userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    // method名はspring.datasource.patternにマッチするので、Primary DBに接続する。
    @Override
    public Users updateUserEmailByUserIdAndEmail_testAOP(int userId, String email) {
        usersRepository.updateUserEmail(userId, email);
        return usersRepository.findById(userId).orElse(null);
    }
}
