package cn.imhtb.bytemarket.bean;


import java.math.BigDecimal;
import java.util.List;


public class GoodsEntity {

    private int imageId;

    private String title;

    private String describe;

    private BigDecimal price;

    private UserEntity author;

    private String images;

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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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
