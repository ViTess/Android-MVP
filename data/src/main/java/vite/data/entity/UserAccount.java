package vite.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * just test
 * Created by trs on 17-5-27.
 */
@Entity
public class UserAccount {
    @Id(autoincrement = true)
    Long id;
    String userName;
    String password;
    @Generated(hash = 845072373)
    public UserAccount(Long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
    @Generated(hash = 1029142458)
    public UserAccount() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
