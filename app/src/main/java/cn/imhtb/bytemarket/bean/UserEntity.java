package cn.imhtb.bytemarket.bean;

import java.io.Serializable;

public class UserEntity implements Serializable {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                '}';
    }

    public static UserEntity getInstance(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("人称江湖梁总");
        return userEntity;
    }
}
