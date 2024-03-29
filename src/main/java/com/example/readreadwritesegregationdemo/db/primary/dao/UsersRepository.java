package com.example.readreadwritesegregationdemo.db.primary.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.readreadwritesegregationdemo.db.entity.Users;



public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Modifying
    @Query("update Users set email = ?2 where id = ?1")
    int updateUserEmail(Integer id, String email);

    @Modifying
    @Query("update Users set email = ?2 where id = ?1")
    int updateUserEmailFoAOPTest(Integer id, String email);
}
