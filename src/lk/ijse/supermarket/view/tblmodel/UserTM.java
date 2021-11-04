package lk.ijse.supermarket.view.tblmodel;



public class UserTM {
     private int userID;
     private String userName;
     private String password;
     private String activeState;
     private String userType;

    public UserTM() {
    }

    public UserTM(int userID, String userName, String password, String activeState, String userType) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.activeState = activeState;
        this.userType = userType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActiveState() {
        return activeState;
    }

    public void setActiveState(String activeState) {
        this.activeState = activeState;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "UserTM{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", activeState='" + activeState + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
