package cn.imhtb.bytemarket.common;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cn.imhtb.bytemarket.bean.Address;
import cn.imhtb.bytemarket.bean.BannerEntity;
import cn.imhtb.bytemarket.bean.Campus;
import cn.imhtb.bytemarket.bean.Category;
import cn.imhtb.bytemarket.bean.CloudUserInfo;
import cn.imhtb.bytemarket.bean.Favour;
import cn.imhtb.bytemarket.bean.Goods;

public class Api {

    private static final String BASE = "http://134.175.118.250:8080";

    public static final String URL_DETAIL_HTML = BASE + "/product/html/";

    public static final String URL_GET_BANNER = BASE + "/banner/list";

    public static final String URL_GET_CATEGORY = BASE + "/category/list";

    public static final String URL_GET_GOODS = BASE + "/product/list?limit=10";

    public static final String URL_SEARCH_GOODS = BASE + "/product/search";

    public static final String URL_GET_CAMPUS = BASE + "/school/list";

    public static final String URL_UPLOAD_IMAGES = BASE + "/upload/images";

    public static final String URL_LOGIN = BASE + "/user/login";

    public static final String URL_ADD_PRODUCT = BASE + "/product/";

    public static final String URL_GET_ADDRESS = BASE + "/address/list";

    public static final String URL_GET_COLLECT = BASE + "/store/collect";

    public static final String URL_GET_HISTORY = BASE + "/store/history";

    public static final String URL_ADDRESS_ADD = BASE + "/address/";

    public static final String URL_ADDRESS_DELETE = BASE + "/address/";

    public static final String URL_FAVOUR_DELETE = BASE + "/store/";

    public static final String URL_GET_RONG_USERINFO= BASE + "/user/v1/getUserInfo";

    public static final Type TYPE_RONG_USERINFO = new TypeToken<ServerResponse<CloudUserInfo>>() {}.getType();

    public static final Type TYPE_BANNER = new TypeToken<ServerResponse<List<BannerEntity>>>() {}.getType();

    public static final Type TYPE_CAMPUS = new TypeToken<ServerResponse<List<Campus>>>() {}.getType();

    public static final Type TYPE_ADDRESS = new TypeToken<ServerResponse<List<Address>>>() {}.getType();

    public static final Type TYPE_GOODS = new TypeToken<ServerResponse<List<Goods>>>() {}.getType();

    public static final Type TYPE_SINGLE_GOODS = new TypeToken<ServerResponse<Goods>>() {}.getType();

    public static final Type TYPE_CATEGORY = new TypeToken<ServerResponse<List<Category>>>() {}.getType();

    public static final Type TYPE_FAVOUR = new TypeToken<ServerResponse<List<Favour>>>() {}.getType();

}
