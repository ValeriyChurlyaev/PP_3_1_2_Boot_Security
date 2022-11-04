package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImp(UserDAO userDAO, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }


    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User findUserById(long id) {
        return userDAO.findUserById(id);
    }

    @Override
    @Transactional
    public void saveUser(User user, String[] roles) {
        List<Role> role = new ArrayList<>();
        role.add(roleService.getAllRoles().get(1));
        for (String s : roles) {
            if (s.equals("ROLE_ADMIN")) {
                role.add(roleService.getAllRoles().get(0));
            }
        }
        user.setRoles(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.saveUser(user);
    }

    @Override
    @Transactional
    public void updateUser(long id, User user, String[] roles) {
        List<Role> role = new ArrayList<>();
        role.add(roleService.getAllRoles().get(1));
        for (String s : roles) {
            if (s.equals("ROLE_ADMIN")) {
                role.add(roleService.getAllRoles().get(0));
            }
        }
        user.setRoles(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.updateUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userDAO.deleteUser(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> user = userDAO.findByUsername(username);
        return user.get(0);
    }
}
