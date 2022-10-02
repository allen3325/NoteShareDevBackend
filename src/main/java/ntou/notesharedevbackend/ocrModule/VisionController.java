/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ntou.notesharedevbackend.ocrModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/ocr", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisionController {
    @Autowired
    private VisionService visionService;

    @GetMapping("/getText")
    public ResponseEntity<Object> extractText(String imageUrl) {
        Map<String, Object> result = new HashMap<>();
        System.out.println(imageUrl);
        String text = visionService.getTextFromURL(imageUrl);
        result.put("res", text);
        return ResponseEntity.ok(result);
    }
}
