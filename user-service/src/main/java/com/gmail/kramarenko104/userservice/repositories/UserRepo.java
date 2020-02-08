package com.gmail.kramarenko104.userservice.repositories;

import com.gmail.kramarenko104.userservice.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
public interface UserRepo extends CrudRepository <User, Long> {

    Optional<User> findByLogin(String login);

    @Modifying
    @Query("update User u set u.login = :login, u.name= :name, u.address = :address, " +
            "u.comment = :comment where u.user_id = :user_id")
    int updateUser(@Param("login") String login,
                    @Param("name") String name,
                    @Param("address") String address,
                    @Param("comment") String comment,
                    @Param("user_id") Long user_id);
}
