package core.mvc;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private View view;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView(View view) {
        this.view = view;
    }
    public ModelAndView addObject(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public View getView() {
        return view;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
