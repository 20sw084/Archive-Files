package archive_files_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
  public static void main(String[] args) {
    // Provide the path to the folder containing the files
    String folderPath =
        "C:/Users/junAI/eclipse-workspace/archive_files_project/files";
    // Call the method to list files in the folder
    listFilesInFolder(folderPath);
  }

  public static void listFilesInFolder(String folderPath) {
    File folder = new File(folderPath);

    // Check if the provided path is a directory
    if (folder.isDirectory()) {
      // Get list of files in the directory
      File[] files = folder.listFiles();

      // Iterate through each file and print its name
      if (files != null) {
        for (File file : files) {
          if (file.isFile()) {
            // Specify the directory where you want to create or find the folder
            String directoryPath =
                "C:/Users/junAI/eclipse-workspace/archive_files_project/files/";

            // Specify the name of the folder
            String folderName = "archived";

            String filess[] = {file.getName()};

            if (!isFileTwoYearsOld(file.getName())) {
              String directoryToCreate =
                  "C:/Users/junAI/eclipse-workspace/archive_files_project/files/temp/";
              createDirectory(directoryToCreate);
              try {
                copyFiles(directoryPath, directoryToCreate, filess);
              } catch (IOException e) {
                e.printStackTrace();
              }

              // Call the method to create or find the folder and do work inside
              // it
              doWorkInFolder(directoryPath, folderName);
            } else {
              try {
                copyFiles(
                    directoryPath, directoryPath + "\\" + folderName, filess);
                try {
					deleteFile(directoryPath + file.getName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }
        }
        try {
          String tempFolderPath =
              "C:/Users/junAI/eclipse-workspace/archive_files_project/files/temp";
          String outputFile = "bulk_04-2024.csv";
          readTempFiles(tempFolderPath, outputFile);
          try {
            deleteTempDirectory(tempFolderPath);
          } catch (Exception e) {
            e.printStackTrace();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("Folder is empty or does not exist.");
      }
    } else {
      System.out.println("Provided path is not a directory.");
    }
  }
  public static void deleteFile(String filePath) throws Exception {
      // Create a File object for the file
      File file = new File(filePath);

      // Check if the file exists and is a regular file
      if (!file.exists() || !file.isFile()) {
          System.out.println("File does not exist or is not a regular file.");
          return;
      }

      // Attempt to delete the file
      boolean deleted = file.delete();

      // Check if the file deletion was successful
      if (!deleted) {
          throw new Exception("Failed to delete file: " + filePath);
      }

      System.out.println("File deleted successfully.");
  }
  public static boolean isFileTwoYearsOld(String filename) {
    // Get current date
    Calendar currentDate = Calendar.getInstance();
    // Subtract 2 years from the current date
    currentDate.add(Calendar.YEAR, -2);

    // Parse the filename to extract month and year
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
    Date fileDate;
    try {
      fileDate = dateFormat.parse(filename);
    } catch (ParseException e) {
      System.out.println("Invalid filename format.");
      return false;
    }

    // Convert file date to Calendar object
    Calendar fileCalendar = Calendar.getInstance();
    fileCalendar.setTime(fileDate);

    // Compare the file date with the current date
    return fileCalendar.compareTo(currentDate) <= 0;
  }
  public static void doWorkInFolder(String directoryPath, String folderName) {
    // Create a File object for the directory
    File directory = new File(directoryPath);

    // Check if the directory exists
    if (!directory.exists()) {
      // If the directory doesn't exist, print an error message
      System.out.println("Directory does not exist.");
      return;
    }

    // Create a File object for the folder inside the directory
    File folder = new File(directory, folderName);

    // Check if the folder exists
    if (!folder.exists()) {
      // If the folder doesn't exist, create it
      boolean created = folder.mkdir();
      if (created) {
        //                System.out.println("Folder created successfully.");
      } else {
        System.out.println("Failed to create folder.");
        return;
      }
    }
  }

  public static void copyFiles(String sourceDirectory,
      String destinationDirectory, String[] filesToCopy) throws IOException {
    // Create File objects for source and destination directories
    File sourceDir = new File(sourceDirectory);
    File destinationDir = new File(destinationDirectory);

    // Check if source directory exists
    if (!sourceDir.exists() || !sourceDir.isDirectory()) {
      System.out.println(
          "Source directory does not exist or is not a directory.");
      return;
    }

    // Check if destination directory exists; if not, create it
    if (!destinationDir.exists()) {
      boolean created = destinationDir.mkdirs();
      if (!created) {
        System.out.println("Failed to create destination directory.");
        return;
      }
    }

    // Copy each file to the destination directory
    for (String fileName : filesToCopy) {
      File sourceFile = new File(sourceDir, fileName);
      File destinationFile = new File(destinationDir, fileName);

      // Check if source file exists
      if (!sourceFile.exists() || !sourceFile.isFile()) {
        System.out.println("Source file '" + fileName
            + "' does not exist or is not a regular file.");
        continue;
      }

      // Perform the copy operation
      Path sourcePath = sourceFile.toPath();
      Path destinationPath = destinationFile.toPath();
      Files.copy(
          sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

      //            System.out.println("File '" + fileName + "' copied
      //            successfully.");
    }
  }

  public static boolean createDirectory(String directoryPath) {
    // Create a File object for the directory
    File directory = new File(directoryPath);

    // Check if the directory already exists
    if (directory.exists()) {
      //            System.out.println("Directory already exists.");
      return false;
    }

    // Attempt to create the directory
    boolean created = directory.mkdir();

    // Return true if the directory was created successfully, false otherwise
    return created;
  }
  public static void readTempFiles(String tempFolderPath, String outputFile)
      throws IOException {
    // Create a FileWriter for the output file
    FileWriter writer =
        new FileWriter(outputFile, true); // true for append mode

    // Create a File object for the temp folder
    File tempFolder = new File(tempFolderPath);

    // Check if the temp folder exists and is a directory
    if (!tempFolder.exists() || !tempFolder.isDirectory()) {
      System.out.println("Temp folder does not exist or is not a directory.");
      return;
    }

    // Get list of files in the temp folder
    File[] files = tempFolder.listFiles();

    // Read content of each file and append it to the output file
    if (files != null) {
      for (File file : files) {
        if (file.isFile()) {
          // Create a BufferedReader for reading the file
          BufferedReader reader = new BufferedReader(new FileReader(file));

          // Read file content line by line and append it to the output file
          String line;
          while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.write(System.lineSeparator()); // Add newline character
          }

          // Close the BufferedReader
          reader.close();
        }
      }
    } else {
      System.out.println("Temp folder is empty or does not exist.");
    }

    // Close the FileWriter
    writer.close();
  }

  public static void deleteTempDirectory(String tempDirectoryPath)
      throws Exception {
    // Create a File object for the temp directory
    File tempDirectory = new File(tempDirectoryPath);

    // Check if the temp directory exists and is a directory
    if (!tempDirectory.exists() || !tempDirectory.isDirectory()) {
      System.out.println(
          "Temp directory does not exist or is not a directory.");
      return;
    }

    // Delete the temp directory recursively
    deleteDirectory(tempDirectory);

            System.out.println("Everything done successfully.");
  }

  private static void deleteDirectory(File directory) throws Exception {
    // Get list of files and directories in the directory
    File[] files = directory.listFiles();

    // Delete all files and directories inside the directory
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          // Recursively delete subdirectories
          deleteDirectory(file);
        } else {
          // Delete files
          boolean deleted = file.delete();
          if (!deleted) {
            throw new Exception(
                "Failed to delete file: " + file.getAbsolutePath());
          }
        }
      }
    }

    // Delete the directory itself
    boolean deleted = directory.delete();
    if (!deleted) {
      throw new Exception(
          "Failed to delete directory: " + directory.getAbsolutePath());
    }
  }
}