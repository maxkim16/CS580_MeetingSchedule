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
public class RoomDB {
    
    private int number;
    private int size;
    
    public RoomDB( int number, int size) {
        this.number = number;
        this.size = size;
    }
    
    public int getNumber() {
        return number;
    }
    
    public int getSize() {
        return size;
    }
    
}
