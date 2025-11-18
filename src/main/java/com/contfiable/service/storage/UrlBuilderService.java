package com.contfiable.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlBuilderService {

    @Value("${app.base-url}")
    private String baseUrl;

    public String buildAbsoluteUrl(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            return null;
        }
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath;
        }
        return baseUrl + (relativePath.startsWith("/") ? relativePath : "/" + relativePath);
    }
}
