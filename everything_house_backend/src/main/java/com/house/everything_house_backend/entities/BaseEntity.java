package com.house.everything_house_backend.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 请求参数 */
    private Map<String, Object> params;
    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

}
