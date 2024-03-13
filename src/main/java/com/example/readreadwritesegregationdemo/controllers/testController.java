package com.example.readreadwritesegregationdemo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.readreadwritesegregationdemo.db.entity.Users;
import com.example.readreadwritesegregationdemo.service.TestReadReplicaByCustomAopService;
import com.example.readreadwritesegregationdemo.service.TestReadReplicaByEntityManagerService;
import com.example.readreadwritesegregationdemo.service.TestReadReplicaByTranactionalService;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class testController {

    private final TestReadReplicaByEntityManagerService testReadReplicaByEntityManagerService;
    
    private final TestReadReplicaByTranactionalService testReadReplicaByTranactionalService;

    private final TestReadReplicaByCustomAopService testReadReplicaByCustomAopService;

    public testController(TestReadReplicaByTranactionalService testReadReplicaByTranactionalService, 
                          TestReadReplicaByCustomAopService testReadReplicaByCustomAopService,
                          TestReadReplicaByEntityManagerService testReadReplicaByEntityManagerService){
        this.testReadReplicaByEntityManagerService = testReadReplicaByEntityManagerService;
        this.testReadReplicaByTranactionalService = testReadReplicaByTranactionalService;
        this.testReadReplicaByCustomAopService = testReadReplicaByCustomAopService;
    }

    @GetMapping("/separateByEntityManager/readFromReplica")
    public String readFromReplicaSeparateByEntityManager(){
        Users user1 = testReadReplicaByEntityManagerService.findUserById(1);
        Users user2 = testReadReplicaByEntityManagerService.findUsersWithNameCriteria("user1_replica").get(0);
        Users user3 = testReadReplicaByEntityManagerService.findUsersWithNameJPQL("user1_replica").get(0);
        Users user4 = testReadReplicaByEntityManagerService.findUsersWithNameNative("user1_replica").get(0);
        String returnValue = String.format("findByEntityManager: %s, findByCriteria: %s, findByJPQL: %s, findByNativeSQL: %s.",
                                            user1.getName(), 
                                            user2.getName(), 
                                            user3.getName(), 
                                            user4.getName());
        return returnValue;
    }

    @GetMapping("/separateByEntityManager/updateToPrimary")
    public String updateToPrimarySeparateByEntityManager(){
        Users user = testReadReplicaByEntityManagerService.updateUser(1, "user1");
        return user.getName();
    }
    
    @GetMapping("/separateByTransactional/readFromReplica")
    public String readFromReplicaSeparateByTransactional(){
        Users user = testReadReplicaByTranactionalService.findByUserId(1);
        return user.getName();
    }

    @GetMapping("/separateByTransactional/updateToPrimary")
    public String updateToPrimarySeparateByTransactional(){
        Users user = testReadReplicaByTranactionalService.updateUserEmailByUserIdAndEmail(1, "aaaaaaa");
        return user.getName();
    }

    @GetMapping("/separateByAOP/readFromReplica")
    public String readFromReplicaSeparateByAOP(){
        Users user = testReadReplicaByCustomAopService.findByUserId(1);
        return user.getName();
    }

    @GetMapping("/separateByAOP/updateToPrimary")
    public String updateToPrimarySeparateByAOP(){
        Users user = testReadReplicaByCustomAopService.updateUserEmailByUserIdAndEmail(1, "aaaaaaa");
        return user.getName();
    }
}
