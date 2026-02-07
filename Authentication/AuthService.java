/*
Super basic auth layer. might expand later
 */

package Authentication;

import Exceptions.AuthenticationExceptions.InvalidCredentialsException;
import Password.PasswordUtil;
import Role.RoleUtil;
import User.User;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private final Map<String, User> storeUser;

    public AuthService() {
        this.storeUser = new ConcurrentHashMap<>();
        initDefaultUsers();
    }

    public User login(String username, String password) throws Exception {
        // Check if user is present in map
        User user = storeUser.get(username);

        // Check if user exists
        if (user == null) { return null; }

        // enforce login limits
        int totalUsernameAttemepts = 5;
        int totalPasswordAttemepts = 5;

        try {
            if ((totalUsernameAttemepts > 5) && (totalPasswordAttemepts > 5)) {
                throw new InvalidCredentialsException("Username or password limit reached");
            }
        } catch (InvalidCredentialsException e) {
            throw new Exception(e);
        }

        // Check password match
        if (user.checkPassword(password)) { return user; }
        return null;
    }
    // might change uNames later
    private void initDefaultUsers() {
       User admin = new User("123admin123", PasswordUtil.hashPassword("admin"), RoleUtil.Role.ADMIN, "0451");
       storeUser.put(admin.getUsername(), admin);

       User teacher = new User("123teacher123", PasswordUtil.hashPassword("teacher"), RoleUtil.Role.TEACHER, "Dr. Smith");
       storeUser.put(teacher.getUsername(), teacher);

       User student = new User("123student123", PasswordUtil.hashPassword("student"), RoleUtil.Role.STUDENT, "Alice Johnson");
       storeUser.put(student.getUsername(), student);

       User parent = new User("123parent123", PasswordUtil.hashPassword("parent"), RoleUtil.Role.PARENT, "Alice Johnson");
       storeUser.put(parent.getUsername(), parent);
    }

    public boolean registerUser(String username, String plainPassword, RoleUtil.Role role, String assocID) {
        // if username already exists return false, else return true and add user to hashmap
        if(storeUser.containsKey(username)) { return false; }

        // Hash the password before creating user
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        User newUser = new User(username, hashedPassword, role, assocID);
        storeUser.put(username, newUser);
        return true;
    }

    public Collection<User> getAllUsers() { return storeUser.values(); }
}