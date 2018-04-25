package utils.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class User {
    private final static String NAME = "name";
    private final String name;
    private final static String ROLE = "role";
    private final UserRole role;

    public User(String name, UserRole role) {
        this.name = name;
        this.role = role;
    }

    public User(JSONObject jsonObject) {
        try {
            name = jsonObject.getString(NAME);
            role = UserRole.valueOf(jsonObject.getString(ROLE));
        } catch (JSONException e) {
            throw new IllegalStateException("No such field: name/role");
        }
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }

    public JSONObject toJSONObject() {
        try {
            JSONObject jsonObject = new JSONObject().put(NAME, name).put(ROLE, role);
            return jsonObject;
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, role);
    }
}
