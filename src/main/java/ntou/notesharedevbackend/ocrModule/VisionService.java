package ntou.notesharedevbackend.ocrModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class VisionService {
    @Autowired
    private ResourceLoader resourceLoader;

    // [START spring_vision_autowire]
    @Autowired private CloudVisionTemplate cloudVisionTemplate;
    // [END spring_vision_autowire]

    public String getTextFromURL(String imageUrl){
        String textFromImage =
                this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageUrl));
        return textFromImage;
    }
}
