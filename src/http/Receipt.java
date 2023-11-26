package http;

public class Receipt {

    private String result;

    private String message;

    public Receipt(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
