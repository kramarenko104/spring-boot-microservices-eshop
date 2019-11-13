package com.gmail.kramarenko104.userservice.repositories;

import com.gmail.kramarenko104.userservice.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends CrudRepository <User, Long> {

    User findByLogin(String login);

    @Modifying
    @Query("update User u set u.login = :login, u.password = :password, u.name= :name, u.address = :address, " +
            "u.comment = :comment where u.user_id = :user_id")
    User updateUser(@Param("login") String login,
                    @Param("password") String password,
                    @Param("name") String name,
                    @Param("address") String address,
                    @Param("comment") String comment,
                    @Param("user_id") Long user_id);
}
