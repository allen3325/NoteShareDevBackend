package ntou.notesharedevbackend.basicFunctionModule.service;

import ntou.notesharedevbackend.basicFunctionModule.config.*;
import ntou.notesharedevbackend.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UploadFileService {
    public String[] uploadImage(String request) {
        List<String> responseURL = new ArrayList<>();
        String[] response = new String[0];
        try {
            for (String file : request.split(","))
                responseURL.add("https://www.googleapis.com/drive/v3/files/" + UploadConfig.uploadFile(file));

            response = responseURL.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
