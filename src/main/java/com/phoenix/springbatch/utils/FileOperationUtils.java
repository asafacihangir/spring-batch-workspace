package com.phoenix.springbatch.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

public class FileOperationUtils {

  public static FileSystemResource getFileResource(String path) {
    try {
      return new FileSystemResource(ResourceUtils.getFile(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getFilePathForDifferentDirectory(File file, String newDirectory) {
    String name = file.getName();
    return FilenameUtils.concat(FilenameUtils.normalizeNoEndSeparator(newDirectory), name);
  }

  public static File moveFileToDirectory(File file, String directory) {
    String copyFilePath = getFilePathForDifferentDirectory(file, directory);
    File newFile = new File(copyFilePath);
    try {
      FileUtils.moveFile(file, newFile);
      return newFile;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void deleteFile(String inputFile) {
    FileUtils.deleteQuietly(new File(inputFile));
  }

  public static String getWorkDirPath(String subDirectory, String filename)  {
    return FilenameUtils
        .concat(System.getProperty("user.dir"), subDirectory)
        .concat(filename);
  }

  public static String getWorkDirSubDirectory(String subDirectory)  {
    return FilenameUtils.concat(System.getProperty("user.dir"), subDirectory);
  }

}