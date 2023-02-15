package com.rdbprojects.cryptowallet.dao;

import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsersDao {
    @Autowired
    UsersRepository usersRepository;

    public Users addUser(Users user) {
        try {
            Users savedUser = usersRepository.save(user);
            return savedUser;
        } catch (Exception e) {
            return null;
        }

    }

    public Users findUserByEmail(Users user) {
        try {
            Users findedUser = usersRepository.findUserByEmail(user.getEmail());
            return findedUser;
        } catch (Exception e) {
            return null;
        }

    }

    public Users updateUser(Users user) {
        try {
            Users updatedUser = usersRepository.save(user);
            return updatedUser;
        } catch (Exception e) {
            return null;
        }

    }

    public Users deleteUser(Users user) {
        try {
            usersRepository.delete(user);
            return user;
        } catch (Exception e) {
            return null;
        }

    }

    public List<Users> findAll() {
        try {
            List<Users> findedUsers = usersRepository.findAll();
            return findedUsers;
        } catch (Exception e) {
            return null;
        }

    }

}
