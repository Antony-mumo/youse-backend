package com.company.youse.utils;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/**
 * @author Austin Oyugi
 * @since  2/17/2022
 * mail austinoyugi@gmail.com
 */

public class AppUtils {

    public static class Objects{
        public static boolean isEmpty(Collection<?> c) {
            return c == null || c.isEmpty();
        }

        public static boolean isEmpty(String s) {
            return s == null || s.trim().isEmpty();
        }

        public static boolean isPresent(Collection<?> c) {
            return !isEmpty(c);
        }
        public static boolean hasValue(String s) {
            return !isEmpty(s);
        }
    }

    public static class StringUtils {
        public static boolean isNullOrEmpty(String string){
            if (java.util.Objects.isNull(string)) return true;
            return string.equals("");
        }
    }

    public static class FileUtils{

        @SneakyThrows
        public static String getResourceString(String name){
            Resource resource = new ClassPathResource(name);
            InputStream inputStream = resource.getInputStream();
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
