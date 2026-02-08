package Password;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Hash a plain text password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    
    // Verify a password against a hash
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        // catch illegal argument exception
        } 
        catch (IllegalArgumentException e) { return false; }
    }
}
