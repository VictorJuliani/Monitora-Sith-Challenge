package com.monitoratec.victor.configs;

import com.monitoratec.victor.models.IVersioned;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IVersionAdvice {
    Pattern apiPatter = Pattern.compile("^(/api/v)([0-9]+)(/.*)?$");
    default boolean hasVersionedInterface(Class<?> clazz) {
        return Arrays.asList(clazz.getInterfaces()).contains(IVersioned.class);
    }

    default int getVersion(String path) {
        Matcher versionMatcher = this.apiPatter.matcher(path);
        return versionMatcher.find()
                ? Integer.parseInt(versionMatcher.group(2))
                : IVersioned.LATEST;
    }
}
