package com.king.assessment.utils;

import java.nio.file.Files;

import org.springframework.util.ResourceUtils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtils {

    @SneakyThrows
    public static String loadTextFileFromResources(String fileName) {
        return new String(Files.readAllBytes(ResourceUtils.getFile("classpath:" + fileName)
                                                          .toPath()));
    }
}
