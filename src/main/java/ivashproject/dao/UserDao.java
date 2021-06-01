package ivashproject.dao;

import ivashproject.model.User;

import java.util.List;

public interface UserDao {
    void addUser(User user);
    void deleteUser(User user);
    void updateUser(long id,User updateUser);
    List<User> userList();
    User show(long id);
}
