package com.example.cbr__fitness.data;

import java.util.ArrayList;

/**
 * @author Jobst-Julius Bartels
 */

// Datenklasse für die User-Liste
public class UserList extends ArrayList<User>{

    // Default-Konstruktor.
    public UserList() {

    }


    // Methode zur Überprüfung der Eingaben des Users.
    public boolean checkUser(String username, String userPassword) {
        boolean check = false;
        for (int i = 0; i < this.size(); i++){
            if(this.get(i).getUsername().matches(username) && this.get(i).getUserPassword().matches(userPassword)) {
                check = true;
            }
        }
        return check;
    }

    // Methode zur Ermittlung des eingeloggten Users.
    public User getLoggedUser(String username, String userPassword) {
        User user = new User();
        for (int i = 0; i < this.size(); i++){
            if(this.get(i).getUsername().matches(username) && this.get(i).getUserPassword().matches(userPassword)) {
                user =this.get(i);
                this.remove(this.get(i));
            }
        }
        return user;
    }

    // Methode zur Ermittlung eines bestimmten Users.
    public User getCertainUser(String name) {
        User user = new User();
        for(int i = 0; i < this.size(); i++) {
            if(this.get(i).getUsername().equalsIgnoreCase(name)) {
                user = this.get(i);
            }
        }
        return user;
    }

    // Methode die einen bestimmten User entfernt.
    public void removeUser(String username) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getUsername().matches(username)) {
                this.remove(this.get(i));
            }
        }
    }

    // Methode zur Überprüfung, ob ein User existiert.
    public boolean userExists(String username) {
        boolean exists = false;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getUsername().equalsIgnoreCase(username)) {
                exists = true;
            }
        }
        return exists;
    }

    // Print-Methode der userList
    public String UserListToString() {
        String userListString = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            sb.append("User [username="+ this.get(i).getUsername() + "]");
            sb.append("[userPassword=" + this.get(i).getUserPassword() +"]");
            sb.append("[age=" + this.get(i).getAge() +"]");
            sb.append("[gender=" + this.get(i).getGender() +"]");
            sb.append("[workType=" + this.get(i).getWorktype() +"]");
            sb.append("[bodyType=" + this.get(i).getBodyType() +"]");
            sb.append("[duration=" + this.get(i).getDuration() +"]");
            sb.append("[res=" + this.get(i).getRes() +"]");
            sb.append("[pPath=" + this.get(i).getPathData() +"]");
            sb.append("\n");
        }
        userListString = sb.toString();
        return userListString;
    }
}
