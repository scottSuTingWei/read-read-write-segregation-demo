package com.example.readreadwritesegregationdemo.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.readreadwritesegregationdemo.db.entity.Users;
import com.example.readreadwritesegregationdemo.service.TestReadReplicaByCustomAopService;
import com.example.readreadwritesegregationdemo.service.TestReadReplicaByTranactionalService;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class testController {
    
    private final TestReadReplicaByTranactionalService testReadReplicaByTranactionalService;

    private final TestReadReplicaByCustomAopService testReadReplicaByCustomAopService;

    public testController(TestReadReplicaByTranactionalService testReadReplicaByTranactionalService, 
                          TestReadReplicaByCustomAopService testReadReplicaByCustomAopService){
        this.testReadReplicaByTranactionalService = testReadReplicaByTranactionalService;
        this.testReadReplicaByCustomAopService = testReadReplicaByCustomAopService;
    }
    
    @GetMapping("/readFromReplica")
    public String readFromReplica(){
        Users user = testReadReplicaByTranactionalService.findByUserId(1);
        return user.getName();
    }

    @GetMapping("/updateToPrimary")
    public String updateToPrimary(){
        Users user = testReadReplicaByTranactionalService.updateUserEmailByUserIdAndEmail(1, "aaaaaaa");
        return user.getName();
    }

    @GetMapping("/readFromReplicaAOP")
    public String readFromReplica_testAOP(){
        Users user = testReadReplicaByCustomAopService.findByUserId(1);
        return user.getName();
    }

    @GetMapping("/updateToPrimaryAOP")
    public String updateToPrimary_testAOP(){
        Users user = testReadReplicaByCustomAopService.updateUserEmailByUserIdAndEmail(1, "aaaaaaa");
        return user.getName();
    }
}
