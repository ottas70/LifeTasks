package ottas70.lifetasks;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Ottas on 31.8.2015.
 */
public class User {

     int id;
     String username;
     String email;
     String password;
     int points;
     int level;
     ArrayList<Task> finishedTasks = new ArrayList<>();
     ArrayList<Task> tasksInProgress = new ArrayList<>();
     ArrayList<Task> usersTasks = new ArrayList<>();
     Location lastLocation = null;
     String realName;
     String adress;
     String work;
     String profilePhotoName;
     String accountPhotoName;

    public User(String email, String password){
        this.email = email;
        this.password = password;
        username = "";
    }

    public User(int id,String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(int id,String username, String email, String password,int points, int level,String realName,String adress,String work,String profilePhotoName,String accountPhotoName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.points = points;
        this.level = level;
        this.realName = realName;
        this.adress = adress;
        this.work = work;
        this.profilePhotoName = profilePhotoName;
        this.accountPhotoName = accountPhotoName;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Task> getFinishedTasks() {
        return finishedTasks;
    }

    public void setFinishedTasks(ArrayList<Integer> finishedTasks) {
        for (Task t : LifeTasks.instance.tasks.getTasks()){
            if(finishedTasks.contains(t.getId())){
                t.setDone(true);
                this.finishedTasks.add(t);
            }else{
                t.setDone(false);
            }
        }
    }

    public void removeFinishedTasks(ArrayList<Integer> ids){
        ArrayList<Task> array = new ArrayList<>();
        for(Task t : finishedTasks){
            if(ids.contains(t.getId()) && t != LifeTasks.instance.dailyTask){
                array.add(t);
            }
        }
        finishedTasks.removeAll(array);
    }

    public ArrayList<Task> getTasksInProgress() {
        return tasksInProgress;
    }

    public void setTasksInProgress(ArrayList<Integer> tasksInProgress) {
        for (Task t : LifeTasks.instance.tasks.getTasks()){
            if(tasksInProgress.contains(t.getId())){
                t.setInProgress(true);
                this.tasksInProgress.add(t);
            }else{
                t.setInProgress(false);
            }
        }
    }


    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getProfilePhotoName() {
        return profilePhotoName;
    }

    public void setProfilePhotoName(String profilePhotoName) {
        this.profilePhotoName = profilePhotoName;
    }

    public String getAccountPhotoName() {
        return accountPhotoName;
    }

    public void setAccountPhotoName(String accountPhotoName) {
        this.accountPhotoName = accountPhotoName;
    }

    public ArrayList<Task> getUsersTasks() {
        return usersTasks;
    }

    public void setUsersTasks() {
        usersTasks.clear();
        for (Task t : LifeTasks.instance.tasks.getTasks()){
            if(t.getOwnersName().equals(LifeTasks.instance.getUser().getUsername())){
                usersTasks.add(t);
            }
        }
    }
}

