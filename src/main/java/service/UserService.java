package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {

    public static UserService userService;

    /* хранилище данных */
    private Map<Long, User> dataBase = Collections.synchronizedMap(new HashMap<>());
    /* счетчик id */
    private AtomicLong maxId = new AtomicLong(0);
    /* список авторизованных пользователей */
    private Map<Long, User> authMap = Collections.synchronizedMap(new HashMap<>());

    private UserService() {
    }

    public static UserService instance() {
        if (userService == null)
            userService = new UserService();
        return userService;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        list.addAll(dataBase.values());
        return  list;
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        for (Map.Entry<Long,User> entry: dataBase.entrySet()) {
            if(entry.getValue().getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        user.setId(maxId.getAndIncrement());
        if (dataBase.put(user.getId(),user)==null) {
            return false;
        }
        return true;
    }

    public void deleteAllUser() {
        dataBase.clear();
    }

    public boolean isExistsThisUser(User user) {
        for (Map.Entry<Long,User> entry: dataBase.entrySet()) {
            if(entry.getValue().getEmail().equals(user.getEmail())) {
                if(entry.getValue().getPassword().equals(user.getPassword())){
                    user.setId(entry.getKey());
                    return true;
                }
            }
        }
        return false;
    }

    public List<User> getAllAuth() {
        List<User> list = new ArrayList<>();
        list.addAll(authMap.values());
        return  list;
    }

    public boolean authUser(User user) {
        if (isExistsThisUser(user)) {
            if (!isUserAuthById(user.getId())) {
                authMap.put(user.getId(), user);
                return true;
            }
        }
        return false;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        if (authMap.get(id)==null) {
            return false;
        }
        return true;
    }

}
