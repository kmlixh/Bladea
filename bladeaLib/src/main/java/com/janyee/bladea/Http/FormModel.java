package com.janyee.bladea.Http;

import java.io.File;
import java.util.Map;

/**
 * Created by kmlixh on 14/11/1.
 */
public class FormModel {
    Map<String, Object> formArea;

    public void putObject(String key, Object obj) {
        if (!(obj instanceof String) && (obj instanceof File)) {
            throw new NTHttpException("Object is not a String or a File");
        } else if (null == key || key.equals("")) {
            throw new NTHttpException("key could not be null or empty");
        } else if (null == obj) {
            throw new NTHttpException("value could not be null");
        } else if (obj instanceof String && obj.equals("")) {
            throw new NTHttpException("obj could not be empty");
        } else if (obj instanceof File && !((File) obj).exists()) {
            throw new NTHttpException(((File) obj).getPath() + " does not exist");
        } else {
            formArea.put(key, obj);
        }
    }

    public void clear() {
        formArea.clear();
    }

    public void remove(String key) {
        formArea.remove(key);
    }

    public Map<String, Object> getFormArea() {
        return formArea;
    }
}
