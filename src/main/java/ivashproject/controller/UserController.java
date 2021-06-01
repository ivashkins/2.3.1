package ivashproject.controller;

import ivashproject.dao.UserDao;
import ivashproject.dao.UserDaoImpl;
import ivashproject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping()
    public String getUsers(ModelMap map) {
        map.addAttribute("users", userDao.userList());
        return "users";
    }
    @GetMapping("/{id}")
    public String showUser(@PathVariable long id, ModelMap map){
        map.addAttribute("user", userDao.show(id));
        return "singleUser";
    }

    @GetMapping("/new")
    public String newUser(ModelMap map){
        map.addAttribute("user",new User());
        return "newUser";
    }


    @PostMapping()
    public String create(@ModelAttribute ("user") User user){
        userDao.addUser(user);
        return "redirect:/user";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id,ModelMap map){
        map.addAttribute("user", userDao.show(id));
        return "edit";
    }
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id){
        userDao.deleteUser(userDao.show(id));
        return "redirect:/user";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user")User user, @PathVariable("id") long id){
        userDao.updateUser(id,user);
        return "redirect:/user";
    }

}
