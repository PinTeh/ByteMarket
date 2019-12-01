package cn.imhtb.bytemarket.common;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.imhtb.bytemarket.bean.BannerEntity;
import cn.imhtb.bytemarket.bean.CategoryEntity;
import cn.imhtb.bytemarket.bean.GoodsEntity;
import cn.imhtb.bytemarket.bean.UserEntity;

public class Api {

    public static final String URL_TEST = "http://www.mockhttp.cn/mock/foo/bar";

    public static final Type TYPE_TEST = new TypeToken<ServerResponse<UserEntity>>() {}.getType();

    public static final String URL_TEST2 = "http://www.mockhttp.cn/mock/foo/bar2";

    public static final Type TYPE_TEST2 =  new TypeToken<ServerResponse<List<GoodsEntity>>>() {}.getType();

    public static final String URL_GET_BANNER = "/banner/list";

    public static final Type TYPE_BANNER = new TypeToken<ServerResponse<List<BannerEntity>>>() {}.getType();

    public static final String URL_GET_CATEGORY = "/category/list";

    public static final Type TYPE_CATEGORY = new TypeToken<ServerResponse<List<CategoryEntity>>>() {}.getType();

    public static final String URL_GET_GOODS = "/product/list";

    public static final Type TYPE_GOODS = new TypeToken<ServerResponse<List<GoodsEntity>>>() {}.getType();
}
