// Tom Pavier
// 20th Oct 2021

import java.io.File;  // For reading files
import java.util.Scanner; // For reading the text file line by line
import java.util.Hashtable; // Handles variables in the code

public class App {
  public static String inputCode = "";
  public static String[] allCode;
  public static Hashtable<String, Integer> variablesTable = new Hashtable<String, Integer>();

  public static void main(String[] args) {
    System.out.println("Running program.txt");
    System.out.println("");

    try {
      // Get program file and write to a variable with all indentation removed
      File fileObj = new File("./bin/program.txt");
      Scanner fileReader = new Scanner(fileObj);

      while (fileReader.hasNextLine()) {
        String currentLine = fileReader.nextLine();
        inputCode = inputCode + currentLine.trim();
      }

      // Close the file
      fileReader.close();
    } catch (Exception e) {
      System.err.println("Error reading code from file");
      System.exit(0);
    }

    // Divide up lines using semicolon
    allCode = inputCode.split(";");

    // Handle the code from first line
    CodeHandler(0);

    System.out.println();
    System.out.println("Execution Complete");
    System.out.println();
  }

  public static Integer CodeHandler(Integer currentLineNum) {
    System.out.println("Code handler started on line " + currentLineNum);
    System.out.println();
    Boolean runningSection = true;
    String currentLineProcess;
    Integer currentLineNumLoop;
    Integer trueLineNum;

    // Loop through all lines of code from start line parameter until the end of the program
    while (runningSection && currentLineNum < allCode.length) {
      // Get the current line of code
      currentLineProcess = allCode[currentLineNum];
      
      // Output all variables from the hashtable and the current line of code
      trueLineNum = currentLineNum + 1;
      System.out.println("#### Running Line " + trueLineNum + " ####");
      System.out.println("Code: " + currentLineProcess);
      System.out.println(variablesTable);
      System.out.println();
      
      // Split the line into an array where spaces are
      String[] currentLineSplit = currentLineProcess.split(" ");

      // Check if a while loop has been initiated
      if (currentLineSplit[0].equals("while")) {
        System.out.println("#### While Loop Init ####");
        currentLineNumLoop = currentLineNum;
        while (variablesTable.get(currentLineSplit[1]) != 0) {
          // currentLineNum will contain the line number to continue executing code form after the loop
          // Use recursion to run through loops
          currentLineNum = CodeHandler(currentLineNumLoop+1) - 1;
        }
        System.out.println("#### End of While Loop ####");
      } else if (currentLineSplit[0].equals("end")) {
        // Stop running code when end is reached
        runningSection = false;
      } else {
        // Handle the line of code in all other cases
        App.HandleLine(currentLineNum);
      }
      currentLineNum += 1;
    }
    System.out.println("#### End of Section ####");
    // Return the line number of the next instruction
    return currentLineNum;
  }

  static void HandleLine(Integer currentLineNum) {
    Integer currentVarVal;
    if (allCode[currentLineNum].split(" ")[0].equals("incr")) {
      // Increase variable value by 1 or set to 1 if it's not currently set
      currentVarVal = variablesTable.get(allCode[currentLineNum].split(" ")[1]);
      if (currentVarVal == null) {
        variablesTable.put(allCode[currentLineNum].split(" ")[1], 1);
        currentVarVal = 1;
      } else {
        variablesTable.put(allCode[currentLineNum].split(" ")[1], currentVarVal + 1);
        currentVarVal += 1;
      }
    } else if (allCode[currentLineNum].split(" ")[0].equals("decr")) {
      // Decrease variable value by 1 if it's greater than 0
      // If it is not set the value will become 0
      currentVarVal = variablesTable.get(allCode[currentLineNum].split(" ")[1]);
      if (currentVarVal == null) {
        variablesTable.put(allCode[currentLineNum].split(" ")[1], 0);
        currentVarVal = 0;
      } else {
        if (currentVarVal != 0) {
          variablesTable.put(allCode[currentLineNum].split(" ")[1], currentVarVal - 1);
          currentVarVal -= 1;
        }
      }
    } else if (allCode[currentLineNum].split(" ")[0].equals("clear")) {
      // Set the current value of the variable to 0
      variablesTable.put(allCode[currentLineNum].split(" ")[1], 0);
    } else {
      // Print error if command doesn't exist
      System.err.println("Command `" + allCode[currentLineNum].split(" ")[0] + "` does not exist");
    }
  }
}
