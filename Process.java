/**
 * Hui (Henry) Chen;	ID: 1242445
 * CSCI 330/ Fall 2019 – M03
 * Dr. Gass
 * Project – CPU Round Robin Scheduling
 * Dec 19, 2019
 * <p>
 * Process.java
 */

package CSCI330.sample__2;

public class Process {

  private int processNum;
  private int arrival_time;
  private int burst_time;
  private int TQ;
  private int contSwitch;
  private int waitTime;
  private int turnaround_time;
  private int AT_initial;
  private int BT_initial;

  public Process(int proc_num, int AT, int BT, int TQ) {
    /**
     * initialize the process info
     * INPUT: process number, arrival time, burst time, and time quantum for the RR scheduling
     * */
    this.processNum = proc_num;
    this.arrival_time = AT;
    this.burst_time = BT;
    this.TQ = TQ;
    this.contSwitch = 0;
    this.waitTime = 0;
    this.turnaround_time = 0;
    this.AT_initial = 0;
    this.BT_initial = 0;
  }

  public void updater_AT(int CPUruntime) {
    /**
     * algorithm to update arrival time
     * INPUT: CPU runtime
     * */
    arrival_time -= CPUruntime;

    if (arrival_time < 0)
      arrival_time = 0;

  }

  public void updater_BT_Left() {
    // get the reminding BT

    burst_time -= TQ;
    // get the left over burst time of a process
    // formula: burst time = burst time - time quantum

    if (burst_time < 0)
      // the process complete its execution, so no need to run anymore
      burst_time = 0;

  }

  public int verify_proceFinished() {
    /**
     * scheduling helper to check if the process is complete the execution
     * OUTPUT: return [1] process completes its execution; [0] process not complete execution
     * NOTE: we must execute the processes based on their arrival time ASCENDING ORDER, otherwise we will not get the desirable results
     * */
    if (getBT_Left() == 0)
      return 1;
    else
      return 0;
  }

  public boolean verify_ready2Execute() {
    /**
     * scheduling helper to check if the process is still need to be executed
     * OUTPUT: return [true] the process gets execute first; return [false] it's not this process's turn to be executed
     * */
    if (getAT() == 0)
      return true;
    else
      return false;
  }

  // =========== getters and setters ==========

  public int getProcessNum() {
    return processNum;
  }

  public void setAT_initial() {
    AT_initial = arrival_time;
  }

  public int getAT_initial() {
    return AT_initial;
  }

  public int getAT() {
    return arrival_time;
  }

  public void setBT_initial() {
    BT_initial = burst_time;
  }

  public int getBT_initial() {
    return BT_initial;
  }

  public void setWaitTime(int CPUruntime) {
    waitTime += CPUruntime;
  }

  public int getWaitTime() {
    return waitTime;
  }

  public int getBT_Left() {
    return burst_time;
  }

  public void updater_contSwitch() {
    contSwitch++;
  }

  public int getContSwitch() {
    return contSwitch;
  }

  public void setTurnAroundTime() {
    turnaround_time = this.getWaitTime() + this.getBT_initial();
  }

  public int getTurnaround_time() {
    return turnaround_time;
  }

}
