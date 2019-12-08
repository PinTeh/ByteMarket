package cn.imhtb.bytemarket.bean;

public class CampusEntity {

    private Integer id;

    private String name;

    private String number;

    private String avatar;

    private String describe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
        campusEntity.setId(1);
        campusEntity.setName("桂林电子科技大学");
        campusEntity.setAvatar("http://image.imhtb.cn/guet_logo.jpg");
        campusEntity.setNumber("一共有38件商品正在出售");
        campusEntity.setDescribe("欢迎加入桂林电子科技大学二手字节交易市场");
        return campusEntity;
    }

    public static CampusEntity newInstanceAlpha(){
        CampusEntity campusEntity = new CampusEntity();
        campusEntity.setId(2);
        campusEntity.setName("广西师范大学");
        campusEntity.setAvatar("http://image.imhtb.cn/gxsf_logo.jpg");
        campusEntity.setNumber("一共有22件商品正在出售");
        campusEntity.setDescribe("欢迎加入广西师范大学二手字节交易市场");
        return campusEntity;
    }

    public static CampusEntity newInstanceBeta(){
        CampusEntity campusEntity = new CampusEntity();
        campusEntity.setId(3);
        campusEntity.setName("广西大学");
        campusEntity.setAvatar("http://image.imhtb.cn/gxdx_logo.jpg");
        campusEntity.setNumber("一共有18件商品正在出售");
        campusEntity.setDescribe("欢迎加入广西大学二手字节交易市场");
        return campusEntity;
    }
}
