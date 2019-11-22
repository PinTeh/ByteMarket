package cn.imhtb.bytemarket.entity;

import java.math.BigDecimal;


public class GoodsEntity {

    private int imageId;

    private String title;

    private BigDecimal price;

    private UserEntity author;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public static GoodsEntity newInstance(){
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setTitle("商品标题");
        goodsEntity.setPrice(new BigDecimal("9.9"));
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("人称江湖梁总");
        goodsEntity.setAuthor(userEntity);
        return goodsEntity;
    }
}
