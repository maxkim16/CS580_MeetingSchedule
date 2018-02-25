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
    private String name;
    private String pswd;
    
    public EmpDB( String name, String pswd) {
        this.name = name;
        this.pswd = pswd;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return pswd;
    }
}
