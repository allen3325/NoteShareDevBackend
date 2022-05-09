package ntou.notesharedevbackend.basicFunctionModule.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.springframework.web.multipart.*;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

/* class to demonstrate use of Drive files list API */
public class Upload {
    /** Application name. */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String FOLDER_ID = "1rvVKJuMKgGf8N9RpD34QNNYczvtQ7ixc";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Upload.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    //TODO: GET uploaded file/image
    public static void getFile(String fileID) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        File file = service.files().get(fileID).setFields("files(id,name,thumbnailLink)").execute();

    }

    public static void setPermission(String fileID) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        com.google.api.services.drive.model.Permission permission = new com.google.api.services.drive.model.Permission();
        permission.setType("anyone");
        permission.setRole("reader");

        service.permissions().create(fileID, permission).execute();
    }

    public static String uploadImage(String imageName) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        String[] fileSplit = imageName.split("\\.");
        String fileType = "";
        if (fileSplit[1].equals("jpg")) {
            fileType = "image/jpeg";
        } else if (fileSplit[1].equals("png")) {
            fileType = "image/png";
        }

        File fileMetadata = new File();
        fileMetadata.setName(imageName);
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        java.io.File filePath = new java.io.File("files/" + imageName);
        FileContent mediaContent = new FileContent(fileType, filePath);
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();

        setPermission(file.getId());
        return file.getId();
    }

    public static String uploadFile(String fileName) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        String[] fileSplit = fileName.split("\\.");
        String fileType = "application/pdf";

        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));

        java.io.File filePath = new java.io.File("files/" + fileName);
        FileContent mediaContent = new FileContent(fileType, filePath);
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();

        setPermission(file.getId());
        return file.getId();
    }

    public static void saveFile(MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                Path path = Paths.get("files", file.getOriginalFilename());
                OutputStream os = Files.newOutputStream(path);
                os.write(file.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}