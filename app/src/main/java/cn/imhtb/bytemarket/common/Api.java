package cn.imhtb.bytemarket.common;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.imhtb.bytemarket.bean.BannerEntity;
import cn.imhtb.bytemarket.bean.Campus;
import cn.imhtb.bytemarket.bean.Category;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.bean.GoodsEntity;
import cn.imhtb.bytemarket.bean.UserEntity;

public class Api {

    public static final String BASE = "http://134.175.118.250:8080";

    public static final String URL_DETAIL_HTML = BASE + "/product/html/";

    public static final String URL_TEST = "http://www.mockhttp.cn/mock/foo/bar";

    public static final Type TYPE_TEST = new TypeToken<ServerResponse<UserEntity>>() {}.getType();

    public static final String URL_TEST2 = "http://www.mockhttp.cn/mock/foo/bar2";

    public static final Type TYPE_TEST2 =  new TypeToken<ServerResponse<List<GoodsEntity>>>() {}.getType();

    public static final String URL_GET_BANNER = BASE + "/banner/list";

    public static final Type TYPE_BANNER = new TypeToken<ServerResponse<List<BannerEntity>>>() {}.getType();

    public static final String URL_GET_CATEGORY = BASE + "/category/list";

    public static final Type TYPE_CATEGORY = new TypeToken<ServerResponse<List<Category>>>() {}.getType();

    public static final String URL_GET_GOODS = BASE + "/product/list";

    public static final Type TYPE_GOODS = new TypeToken<ServerResponse<List<Goods>>>() {}.getType();

    public static final String URL_SEARCH_GOODS = BASE + "/product/search";

    public static final String URL_GET_CAMPUS = BASE + "/school/list";

    public static final String URL_UPLOAD_IMAGES = BASE + "/upload/images";
    //public static final String URL_UPLOAD_IMAGES = "http://10.21.122.95:8080/upload/images";

    public static final Type TYPE_CAMPUS = new TypeToken<ServerResponse<List<Campus>>>() {}.getType();

    public static final String URL_LOGIN = BASE + "/user/login";
}
