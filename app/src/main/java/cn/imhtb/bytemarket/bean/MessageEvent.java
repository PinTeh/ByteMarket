package cn.imhtb.bytemarket.bean;

public class MessageEvent {

    private String message;

    private String data;

    public  MessageEvent(){
    }

    public  MessageEvent(String message){
        this.message=message;
    }

    public  MessageEvent(String message,String data){
        this.message=message;
        this.data = data;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
