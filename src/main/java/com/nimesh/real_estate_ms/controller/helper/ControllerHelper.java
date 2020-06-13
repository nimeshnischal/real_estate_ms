package com.nimesh.real_estate_ms.controller.helper;

import com.nimesh.real_estate_ms.repository.util.CustomPage;

import java.util.HashMap;
import java.util.Map;

public class ControllerHelper {
    public static Map<String, Object> pageToMap(CustomPage page, String key) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, page.getContent());
        map.put("offset", page.getOffset());
        map.put("page_size", page.getPageSize());
        map.put("total_count", page.getTotalSize());
        return map;
    }
}
