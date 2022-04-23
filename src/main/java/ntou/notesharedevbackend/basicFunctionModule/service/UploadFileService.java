package ntou.notesharedevbackend.basicFunctionModule.service;

import ntou.notesharedevbackend.basicFunctionModule.config.*;
import ntou.notesharedevbackend.repository.*;
import org.apache.tomcat.util.http.fileupload.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

@Service
public class UploadFileService {
    public String[] uploadImage(MultipartFile[] request) {
        //save files to temporary directory
        UploadConfig.saveFile(request);

        String uploadedFileName = Arrays.stream(request).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(","));
        List<String> responseURL = new ArrayList<>();
        String[] response = new String[0];
        try {
            for (String file : uploadedFileName.split(","))
                responseURL.add("https://www.googleapis.com/drive/v3/files/" + UploadConfig.uploadFile(file));
            response = responseURL.toArray(new String[0]);

            //delete temporary files
            FileUtils.cleanDirectory(new File("files"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
