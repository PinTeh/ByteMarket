package cn.imhtb.bytemarket.common;

public interface ICallBackHandler<T> {

    void onSuccess(ServerResponse<T> response);
}
