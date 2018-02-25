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
public class EmpDB {
    private int id;
    private String name;
    private String pswd;
    
    public EmpDB( int id, String name, String pswd) {
        this.id = id;
        this.name = name;
        this.pswd = pswd;
    }
    
    public int getID() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return pswd;
    }
}
