package ru.kata.spring.boot_security.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private PeopleRepository peopleRepository;

    @Autowired
    UserServiceImp(RoleRepository roleRepository
            , PasswordEncoder passwordEncoder
            , PeopleRepository peopleRepository) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.peopleRepository = peopleRepository;
    }

    @Override
    @Transactional
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        peopleRepository.save(user);
    }

    @Override
    @Transactional
    public User findById(Integer id) {
        return peopleRepository.findById(id).get();
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        return peopleRepository.findByEmail(email).get();
    }

    @Override
    @Transactional
    public Set<Role> findRolesByIds(List<Long> roleIds) {
         Set<Role> roles = roleIds.stream()
        				.map(roleId -> findRoleById(roleId))
        				.collect(Collectors.toSet());
         return roles;
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return peopleRepository.findAll();
    }

    @Override
    @Transactional
    public void update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        peopleRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        User user = findById(id);
        if (user != null) {
            peopleRepository.delete(user);
        }
    }

    @Override
    @Transactional
    public Role findRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
