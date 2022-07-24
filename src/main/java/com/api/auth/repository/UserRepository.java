package com.api.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.auth.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    
    // @Query(
    //     "select um from UserModel um" + 
    //     " inner join where" + 
    //     " um.userName = :userName")
    Optional<UserModel> findByUserName(@Param("userName") String userName);
}
