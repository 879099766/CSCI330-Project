/**
 * Hui (Henry) Chen;	ID: 1242445
 * CSCI 330/ Fall 2019 – M03
 * Dr. Gass
 * Project – CPU Round Robin Scheduling
 * Dec 19, 2019
 * <p>
 * Clock.java
 */

package CSCI330.sample__2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

public class Clock {

  private int time;

  public Clock() {
    // initialize the clock timer for a process
    time = 0;
  }

  public void updater_TimeBeginRR(int CPUruntime) {
    /**
     * increment the beginning time of RR based
     * INPUT: CPU runtime
     * */
    time += CPUruntime;
  }

  public int get_RRStartTime() {
    return time;
  }

  public void updater_ArrivalTime_InReadyQueue(ArrayList<Process> Processes, int CPUruntime) {
    /**
     * nonrecursive algorithm to update the arrival time of the processes in the ready queue
     * INPUT: processes and CPU runtime
     * */
    for (int ii = 0; ii < Processes.size(); ii++) {
      Process pr = Processes.get(ii);
      pr.updater_AT(CPUruntime);
    }

  }

  public void updater_WaitingTime_InReadyQueue(Queue<Process> ready_Q, int CPUruntime, Process processes) {
    /**
     * nonrecursive algorithm to update the waiting time of the processes in the ready queue
     * INPUT: reqdy queue, CPU runtime, and processes
     * */
    Iterator<Process> iter = ready_Q.iterator();

    while (iter.hasNext()) {

      Process pr = iter.next();

      if (pr != processes)
        pr.setWaitTime(CPUruntime);

    }
  }

}
