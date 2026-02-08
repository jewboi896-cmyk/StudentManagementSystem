package Role;

public class RoleUtil {
    public enum Role {
        ADMIN,
        TEACHER,
        STUDENT,
        PARENT
    }
    // Validate role string
    public static boolean isValidRole(String correctRole) {
        return parseRole(correctRole) != null;
    }
    // Parse role string to Role enum
    public static Role parseRole(String role) {
        for (Role roles: Role.values()) {
            if (roles.name().equalsIgnoreCase(role)) {
                return roles;
            }
        }
        return null;
    }
    // Get display name for role
    public static String getDisplayName(Role role) {
        return switch (role) {
            case ADMIN -> "Administrator";
            case TEACHER -> "Teacher";
            case STUDENT -> "Student";
            case PARENT -> "Parent";
        };
    }
}
