package edu.kit.checkstyle.util;

import java.io.File;
import java.util.List;

public final class FileUtil {
    // todo: unnecessary as of java 8, use Files#walk instead
    public static void addFilesRecursively(final File file, final List<File> files) {
        if (!file.canRead()) return;

        final File[] children = file.listFiles();

        if (file.isDirectory() && children != null) {
            for (final File child : children) {
                addFilesRecursively(child, files);
            }
        } else if (file.isFile()) {
            files.add(file);
        }
    }

    private FileUtil() {
        throw new IllegalStateException();
    }
}