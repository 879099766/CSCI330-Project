/**
 * Hui (Henry) Chen;	ID: 1242445
 * CSCI 330/ Fall 2019 – M03
 * Dr. Gass
 * Project – CPU Round Robin Scheduling
 * Dec 19, 2019
 * <p>
 * CPU.java
 */

package CSCI330.sample__2;

public class CPU {

  private int timeQuantum;
  private int noOfCompletedProcesses;

  public CPU(int tq) {
    // initialize the things for the CPU
    timeQuantum = tq;
    noOfCompletedProcesses = 0;
  }

  public int Run(Process pr) {
    /**
     * nonrecursive algorithm to run the CPU simulation
     * INPUT: processes
     * OUTPUT: return the time quantum/ burst time of processes
     * */
    if (pr.getBT_Left() > timeQuantum) {
      pr.updater_BT_Left();
      return timeQuantum;
    } else if (pr.getBT_Left() < timeQuantum) {
      int run = pr.getBT_Left();
      pr.updater_BT_Left();
      noOfCompletedProcesses++;
      return run;
    } else {
      pr.updater_BT_Left();
      noOfCompletedProcesses++;
      return timeQuantum;
    }

  }

  public int get_Completed_Proc() {
    // return the # of completed process in the CPU
    return noOfCompletedProcesses;
  }

}
