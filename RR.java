/**
 * Hui (Henry) Chen;	ID: 1242445
 * CSCI 330/ Fall 2019 – M03
 * Dr. Gass
 * Project – CPU Round Robin Scheduling
 * Dec 19, 2019
 * <p>
 * RR.java
 */

package CSCI330.sample__2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class RR {

  private ArrayList<Process> procArrList;
  private ArrayList<String> seq = new ArrayList<>();
  private int TQ;     // time quantum
  private CPU cpuObj;
  private Queue<Process> ready_Q;     // ready Queue for the processes
  private Clock clockObj;
  private ArrayList<Process> proc_completed;
  private static int sysProcNum;      // sys process #
  private int ttl_procWT = 0;        // sum of waiting time of all processes
  private int ttl_procTT = 0;        // sum of turnaround time of all processes
  private int ttl_procContSwitch = 0;        // sum of # of context switch of all processes
  private int criticalTime = 0;        // critical time to terminate the process(es)
  private double avg_WT = 0.0;        // average wait time
  private double avg_TT = 0.0;        // average turnaround time
  private double final_throughput = 0.0;      // throughput for the program --> gonna use it when comes to termination
  private double temp = 0.0;      // program termination helper
  private double utilization = 0.0;       // cpu utilization --> gonna use it when comes to output the program result
  private ArrayList<String> responseT;
  private ArrayList<String> GT;

  // initialize the things
  public RR(ArrayList<Process> pr, int tq) {
    this.procArrList = pr;
    this.TQ = tq;
    this.cpuObj = new CPU(TQ);
    this.ready_Q = new LinkedList<Process>();
    this.clockObj = new Clock();
    this.proc_completed = new ArrayList<Process>();
    this.sysProcNum = 4;        // assume we have 4 processes in this project and this is changeable variable
    this.responseT = new ArrayList<>();
    this.GT = new ArrayList<>();

    for (int x = 0; x < pr.size(); x++) {
      // iteratively assign the processes to the arraylist
      pr.get(x).setAT_initial();
      pr.get(x).setBT_initial();
    }

  }

  // run the algorithm
  public void execute() {
    creater_proc(null);
    RoundRobin();
  }

  public void creater_proc(Process proc) {
    /**
     * non recursive algorithm to create processes
     * INPUT: processes need to be handled
     * */

    for (int i = 0; i < procArrList.size(); i++) {
      Process pr = procArrList.get(i);
      // use arraylist in order to iteratively to get the process object info easily
      // NOTE: other data structures could be used here, too!

      if (pr.verify_ready2Execute() == true && pr.verify_proceFinished() == 0 && (pr != proc) && !ready_Q.contains(pr)) {
        // check if the process is valid to process before add to the ready queue
        ready_Q.add(pr);
      }

    }
  }

  public void RoundRobin() {
    /**
     * non recursive algorithm to run cpu round robin scheduling simulation
     * Steps:
     *  1) get the header process from process ready queue
     *  2.1) if current process is completed --> update two scheduling helper: update time begin process && arrival time
     *  2.2) else run the scheduling algorithm
     *      a) capture current process run time, the very beginning run time, current process arrival time, current process waiting time
     *      b) create a process based on the above info
     *      c) verify if the current process is completed
     *          i) if completed: remove from process arraylist & add to ready queue
     *          ii) check if all processes are completed: if so terminate the program and end the while loop
     *          iii) if not completed: update the context switch,
     *               (remove current process from the ready queue and then add back to the ready queue),
     *               and create a new process in order to continue the program
     *
     * */
    boolean flag = false;
    int response_temper = 0;


    // run while there's process
    while (!flag) {
      // flag will be false when comes to program termination

      // select the header from the ready queue which gives the current process that needs to be process
      Process proc_current = ready_Q.peek();

      // add the first response time to the response time sequence
      if (responseT.size() < 1) {
        // initialize the first arrival time of the processes
        responseT.add(Integer.toString(proc_current.getAT()));
        response_temper += proc_current.getAT();
      }

      if (proc_current != null) {
        // more processes need to run

        // update the clock values regards to the current process
        int runTimeOfProcessOnCPU = cpuObj.Run(proc_current);       // capture current process run time
        clockObj.updater_TimeBeginRR(runTimeOfProcessOnCPU);        // capture the very beginning program run time
        clockObj.updater_ArrivalTime_InReadyQueue(procArrList, runTimeOfProcessOnCPU);      // capture current process arrival time in the ready queue
        clockObj.updater_WaitingTime_InReadyQueue(ready_Q, runTimeOfProcessOnCPU, proc_current);        // capture current process waiting time in the ready queue

        response_temper += runTimeOfProcessOnCPU;
        responseT.add(Integer.toString(response_temper));

        // create current process in the ready queue based on the above given info
        creater_proc(proc_current);

        // end the current process && remove it && add to ready queue
        if (proc_current.verify_proceFinished() == 1) {
          // current process ends its execution

          GT.add(Integer.toString(proc_current.getProcessNum()));

          procArrList.remove(proc_current);
          proc_completed.add(ready_Q.remove());

          // end the scheduling
          if (proc_completed.size() == sysProcNum) {
            this.terminate();
            flag = true;
          }

        } else {

          // end the current process && remove it && add to ready queue
          proc_current.updater_contSwitch();      // update the context switch
          GT.add(Integer.toString(proc_current.getProcessNum()));

          ready_Q.remove();       // remove current process from the ready queue
          ready_Q.add(proc_current);      // add the current process to the end of the ready queue

          // create a new process
          creater_proc(proc_current);
        }

      } else {
        GT.add(Integer.toString(proc_current.getProcessNum()));
        // no more process need to run
        clockObj.updater_TimeBeginRR(TQ);
        clockObj.updater_ArrivalTime_InReadyQueue(procArrList, TQ);
        // update the process helper

        creater_proc(null);
        // create a null process so that the algorithm will now it's the time to terminate everything
      }
    }

  }

  public void terminate() {
    /**
     * nonrecursive algorithm to terminate the cpu RR schedule simulation
     * also, do the result calculation in this method
     * NOTE: most of the calculation goes here
     * */

    // loop to initialize the things in order to terminate the processes
    for (int x = 0; x < proc_completed.size(); x++)
      proc_completed.get(x).setTurnAroundTime();

    // assign the turnaround time of each process
    int proc1_TT = proc_completed.get(0).getTurnaround_time();
    int proc2_TT = proc_completed.get(1).getTurnaround_time();
    int proc3_TT = proc_completed.get(2).getTurnaround_time();
    int proc4_TT = proc_completed.get(3).getTurnaround_time();

    // sum turnaround time
    ttl_procTT = proc1_TT + proc2_TT + proc3_TT + proc4_TT;

    for (int y = 0; y < proc_completed.size(); y++)
      criticalTime = criticalTime + proc_completed.get(y).getWaitTime();

    for (int z = 0; z < proc_completed.size(); z++) {
      ttl_procWT = criticalTime;
      ttl_procContSwitch = ttl_procContSwitch + proc_completed.get(z).getContSwitch();
    }

    // calculate the both average waiting and turnaround time
    avg_WT = ttl_procWT / sysProcNum;       // formula: total waiting time/ # of processes
    avg_TT = ttl_procTT / sysProcNum;       // formula: total turnaround time/ # of processes

    // calculate the final throughput
    final_throughput = ((double) cpuObj.get_Completed_Proc() / clockObj.get_RRStartTime()) * 100.0;

    // calculate the final utilization of the simulation
    temp = clockObj.get_RRStartTime() - ttl_procContSwitch;
    utilization = (temp / clockObj.get_RRStartTime()) * 100.0;

  }

  private String chart_responseT() {
    /**
     * iteratively loop responseT to form a response time sequence
     * OUTPUT: return a string response time sequence
     * */

    String result = "";
    for (int x = 0; x < proc_completed.size(); x++)
      result += "[" + responseT.get(x) + "] -> ";

    return result;
  }

  private String chart_GT() {
    /**
     * iteratively loop responseT to form a Gantt sequence
     * OUTPUT: return a string response Gantt sequence
     * */

    String result = "";
    for (int x = 0; x < GT.size(); x++)
      result += "[" + GT.get(x) + "] -> ";

    return result;
  }

  public String display_result() {
    // return the outcome of the program
    String result = "\n" + "\n========== RESULTS ==========" +
        "\nGantt sequence: " + chart_GT().substring(0, chart_GT().length() - 4) +
        "\nResponse time sequence: " + chart_responseT().substring(0, chart_responseT().length() - 4) +
        "\n\nAverage wait time: " + avg_WT +
        "\nAverage turnaround (complete) time: " + avg_TT +
        "\nThroughput: " + final_throughput + "%" +
        "\nCPU utilization: " + utilization + "%";
    return result;
  }

}
