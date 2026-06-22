package foodcourt.in.restaurant.enums;

public enum Role {
    USER,
    RESTAURANT,
    DELIVERY_AGENT,
    ADMIN;
    public static boolean exists(String r)
    {
        for(Role role:Role.values())
        {
            if(r.equals(role.toString())) return true;
        }
        return false;
    }
}
