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
    private String username;
    private String pswd;
    
    
    public EmpDB( String name, String username, String pswd) {
        this.name = name;
        this.username = username;
        this.pswd = pswd;
    }
    
        public String getUsername() {
        return username;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword() {
        return pswd;
    }
}
