package ntou.notesharedevbackend.pictureUpload.service;

import com.google.gson.Gson;
import ntou.notesharedevbackend.pictureUpload.entity.DataPic;
import ntou.notesharedevbackend.pictureUpload.entity.PicDTO;
import ntou.notesharedevbackend.pictureUpload.entity.ReceivePicUpload;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PictureUploadService {

    public ReceivePicUpload uploadPicture(PicDTO requestBase) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        String base64 = requestBase.getBase64();
        if (base64.contains("data:image")) {
            String newBase64 = base64.substring(base64.indexOf(",")+1);
            requestBase.setBase64(newBase64);
        }
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", requestBase.getBase64())
                .addFormDataPart("type", "base64")
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .method("POST", body)
                .addHeader("Authorization", "Client-ID 6f50af9944719d4")
                .build();
        try {
            Response res = client.newCall(request).execute();
            String jsonData = res.body().string();
            Gson gson = new Gson();
            ReceivePicUpload responseResult = gson.fromJson(jsonData, ReceivePicUpload.class);
            return responseResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
