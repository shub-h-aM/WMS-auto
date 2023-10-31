package Utilities;

public class TestData {

    private String usecase;
    private String payload;
    private String url;

    public TestData(String usecase, String payload, String url) {
        this.usecase = usecase;
        this.payload = payload;
        this.url = url;
    }

    public String getUsecase() {
        return usecase;
    }

    public String getPayload() {
        return payload;
    }

    public String getUrl() {
        return url;
    }
}
