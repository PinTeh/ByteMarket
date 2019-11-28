package cn.imhtb.bytemarket.bean;

public class CampusEntity {

    private String name;

    private String number;

    private String avatar;

    private String describe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public static CampusEntity newInstance(){
        CampusEntity campusEntity = new CampusEntity();
        campusEntity.setName("桂林电子科技大学");
        campusEntity.setAvatar("http://image.imhtb.cn/guet_logo.jpg");
        campusEntity.setNumber("一共有32兆在这里卖64位的东西");
        campusEntity.setDescribe("欢迎加入桂林电子科技大学二手字节交易市场");
        return campusEntity;
    }

    public static CampusEntity newInstanceAlpha(){
        CampusEntity campusEntity = new CampusEntity();
        campusEntity.setName("广西师范大学");
        campusEntity.setAvatar("http://image.imhtb.cn/gxsf_logo.jpg");
        campusEntity.setNumber("一共有38个字节在这里卖2T东西");
        campusEntity.setDescribe("欢迎加入广西师范大学二手字节交易市场");
        return campusEntity;
    }

    public static CampusEntity newInstanceBeta(){
        CampusEntity campusEntity = new CampusEntity();
        campusEntity.setName("广西大学");
        campusEntity.setAvatar("http://image.imhtb.cn/gxdx_logo.jpg");
        campusEntity.setNumber("一共有2个字节在这里乱搞");
        campusEntity.setDescribe("欢迎加入广西大学二手字节交易市场");
        return campusEntity;
    }
}
