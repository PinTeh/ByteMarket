package cn.imhtb.bytemarket.bean;

import com.google.gson.annotations.SerializedName;

public class Campus {

    private Integer id;

    private String name;

    private String number;

    private String avatar;

    @SerializedName("description")
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

    public static Campus newInstance(){
        Campus campus = new Campus();
        campus.setId(1);
        campus.setName("桂林电子科技大学");
        campus.setAvatar("http://image.imhtb.cn/guet_logo.jpg");
        campus.setNumber("一共有38件商品正在出售");
        campus.setDescribe("欢迎加入桂林电子科技大学二手字节交易市场");
        return campus;
    }

    public static Campus newInstanceAlpha(){
        Campus campus = new Campus();
        campus.setId(2);
        campus.setName("广西师范大学");
        campus.setAvatar("http://image.imhtb.cn/gxsf_logo.jpg");
        campus.setNumber("一共有22件商品正在出售");
        campus.setDescribe("欢迎加入广西师范大学二手字节交易市场");
        return campus;
    }

    public static Campus newInstanceBeta(){
        Campus campus = new Campus();
        campus.setId(3);
        campus.setName("广西大学");
        campus.setAvatar("http://image.imhtb.cn/gxdx_logo.jpg");
        campus.setNumber("一共有18件商品正在出售");
        campus.setDescribe("欢迎加入广西大学二手字节交易市场");
        return campus;
    }
}
