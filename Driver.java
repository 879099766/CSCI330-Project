/**
 * Hui (Henry) Chen;	ID: 1242445
 * CSCI 330/ Fall 2019 – M03
 * Dr. Gass
 * Project – CPU Round Robin Scheduling
 * Dec 19, 2019
 * <p>
 * Driver.java
 */

package CSCI330.sample__2;

import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Driver {

  public static void main(String[] args) {
    try {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter time quantum for processes: ");
      int TQ = input.nextInt(); // time quantum

      System.out.print("Enter file name: ");
      ArrayList<Process> file_path = new ArrayList<Process>();

      String path = input.next();
      Scanner scanner = new Scanner(new File(path));

      while (scanner.hasNext()) {
        Scanner line = new Scanner(scanner.nextLine()).useDelimiter(",");

        int procNum = line.nextInt();
        int procAT = line.nextInt();
        int procBT = line.nextInt();

        file_path.add(new Process(procNum, procAT, procBT, TQ));
      }

      RR rrObj = new RR(file_path, TQ);

      // start the scheduling
      rrObj.execute();

      // output the results
      System.out.println(rrObj.display_result());
    } catch (Exception err) {
      System.out.println("As error has occurred due to: " + err.getLocalizedMessage());
    }

  }
}