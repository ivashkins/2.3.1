package ivashproject.dao;

import ivashproject.model.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
@Component
public class UserDaoImpl implements UserDao {

    @PersistenceContext(unitName = "emf")
    private EntityManager manager;

    @Transactional
    public void addUser(User user) {
        manager.persist(user);
    }

    @Transactional
    public void deleteUser(User user){
        manager.remove(manager.contains(user) ? user : manager.merge(user));
    }

    @Transactional
    public void updateUser(long id,User updateUser){
        updateUser.setId(id);
        manager.merge(updateUser);
    
    }

    @Transactional(readOnly = true)
    public List<User> userList() {
        return manager.createQuery("select u from User u",User.class).getResultList();
    }
@Transactional(readOnly = true)
    public User show(long id) {
        return manager.find(User.class,id) ;
    }

}
