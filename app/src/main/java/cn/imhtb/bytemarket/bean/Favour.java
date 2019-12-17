package cn.imhtb.bytemarket.bean;


import com.google.gson.annotations.SerializedName;

public class Favour {

    private Integer id;

    @SerializedName("goods")
    private Goods goods;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
