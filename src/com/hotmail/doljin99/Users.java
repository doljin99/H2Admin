/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotmail.doljin99;

import java.util.ArrayList;

/**
 *
 * @author dolji
 */
public class Users extends ArrayList<User> {

    public Users() {
        super();
    }

    public boolean addUser(User candidate) {
        if (addable(candidate)) {
            return super.add(candidate);
        }
        return false;
    }

    private boolean addable(User candidate) {
        if (candidate == null) {
            return false;
        }
        String userName = candidate.getUserName();
        if (userName == null || userName.isEmpty()) {
            return false;
        }
        for (User user : this) {
            if (user.getUserName().equalsIgnoreCase(candidate.getUserName())) {
                return false;
            }
        }

        return true;
    }

    public boolean update(User candidate) {
        if (candidate == null) {
            return false;
        }
        String userName = candidate.getUserName();
        if (userName == null || userName.isEmpty()) {
            return false;
        }
        for (User user : this) {
            if (user.getUserName().equalsIgnoreCase(candidate.getUserName())) {
                remove(user);
                add(candidate);
            }
        }

        return false;
    }

    public boolean existId(String id) {
        for (User user : this) {
            if (user.getUserName().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public User getUser(String userName) {
        for (User user : this) {
            if (user.getUserName().equalsIgnoreCase(userName)) {
                return user;
            }
        }

        return null;
    }

    public void remove(String userName) {
        for (User user : this) {
            if (user.getUserName().equalsIgnoreCase(userName)) {
                remove(user);
                return;
            }
        }
    }
}
