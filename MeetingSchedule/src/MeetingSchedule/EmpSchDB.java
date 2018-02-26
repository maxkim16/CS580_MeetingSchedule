/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MeetingSchedule;

/**
 *
 * @author maxkim
 */

/*
    Each EmpSchDB object represents each row retrieved from `empSchedule` table in database.
*/
public class EmpSchDB {

    private String ID;
    private String username;
    private String date;
    private String startTime;
    private String endTime;
    private String task;
    private String visibility;

    public EmpSchDB( String id, String um, String date, String st, String et, String task, String visb) {
        this.ID = id;
        username = um;
        this.date = date;
        startTime = st;
        endTime = et;
        this.task = task;
        visibility = visb;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getID() {
        return ID;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTask() {
        return task;
    }

    public String getVisibility() {
        return visibility;
    }

}
         

