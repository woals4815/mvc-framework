package next.controller;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/", new HomeController());
        controllers.put("/users/login", new LoginController());
        controllers.put("/users/loginForm", new LoginController());
        controllers.put("/users/logout", new LogoutController());
        controllers.put("/users/form", new CreateUserController());
        controllers.put("/users/create", new CreateUserController());
        controllers.put("/users", new ListUserController());
    }

    public static Controller getController(String url) {
        return controllers.get(url);
    }
}
