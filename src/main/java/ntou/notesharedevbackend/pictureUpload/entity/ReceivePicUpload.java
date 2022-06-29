package ntou.notesharedevbackend.pictureUpload.entity;

public class ReceivePicUpload {
    private DataPic data;
    private Boolean success;
    private Integer status;

    public ReceivePicUpload(String jsonData) {
    }

    public DataPic getData() {
        return data;
    }

    public void setData(DataPic data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
