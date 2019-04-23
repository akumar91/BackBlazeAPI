package BackBlazeCloud.service;

import BackBlazeCloud.exception.BucketOrFileNotFoundException;
import BackBlazeCloud.exception.FileWriteException;
import BackBlazeCloud.exception.JsonParsingException;
import BackBlazeCloud.model.DownloadFileModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DownloadFilesFromBucketServiceImpl implements DownloadFilesFromBucketService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpHeaders headers;

    @Autowired
    ObjectMapper mapper;

    ResponseEntity<String> response;
    String apiUrl;
    String auth;
    String downloadUrl;
    List<String> FilesToDownload = new ArrayList<>();

    public void downloadFilesFromBucket(DownloadFileModel downloadFileModel) {

        try {
            // find buckets authorized for the user
            for (JsonNode bucket : getBuckets(downloadFileModel.getAccountId(), downloadFileModel.getApplicationKey())) {
                // check if the requested bucketName is visible to the user
                if (bucket.findValue("bucketName").asText().equals(downloadFileModel.getBucketName())) {
                    headers = new HttpHeaders();
                    headers.set("Authorization", auth);

                    Map<String, String> body = new HashMap<>();
                    body.put("bucketId", bucket.findValue("bucketId").asText());

                    HttpEntity<?> request = new HttpEntity<Object>(body, headers);
                    String filesInBucketUrl = apiUrl + "/b2api/v2/b2_list_file_names";

                    // get the list of files in the specified bucket
                    response = restTemplate.exchange(filesInBucketUrl, HttpMethod.POST, request, String.class);

                    JsonNode filesInBucket = mapper.readTree(response.getBody()).get("files");

                    // extract the fileNames to be downloaded from the bucket
                    for (JsonNode fileInBucket: filesInBucket) {
                        FilesToDownload.add(fileInBucket.get("fileName").asText());
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new JsonParsingException("Error parsing the json");
        }

        // In case bucket does not exit or there are no files
        if (FilesToDownload.size() == 0) {
            throw new BucketOrFileNotFoundException("Bucket or File not found");
        }

        // download the files to specified directory from a bucket
        downloadFilesToDestinationDirectory(FilesToDownload, downloadFileModel.getBucketName(),
                downloadFileModel.getDestinationDirectory());

    }

    private void downloadFilesToDestinationDirectory(List<String> filesInBucketToDownload, String bucketName,
                                                    String destinationDirectory) {

        // iterate over files in the bucket to download and save it to the specified directory
        for (String fileInBucketToDownload : filesInBucketToDownload) {
            String downloadFileUrl = downloadUrl + "/file/" + bucketName + "/" + fileInBucketToDownload;
            headers = new HttpHeaders();
            headers.set("Authorization", auth);

            HttpEntity<String> request = new HttpEntity<>("parameters", headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    downloadFileUrl, HttpMethod.GET, request, byte[].class);

            List<String> fileName = response.getHeaders().get("x-bz-file-name");

            try {
                Files.write(Paths.get(destinationDirectory + "/" + fileName.get(0)), response.getBody());
            } catch (Exception e) {
                throw new FileWriteException("Error writing file to the destination directory");
            }
        }
    }

    // call the authorize account API to fetch access token and other essential details
    private String authorizeAccount(String accountId, String applicationKey) {
        String url = "https://api.backblazeb2.com/b2api/v2/b2_authorize_account";
        headers.setBasicAuth(accountId, applicationKey);
        HttpEntity<String> params = new HttpEntity<>("parameters", headers);

        // Call service
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, params, String.class);

        return response.getBody();
    }

    private JsonNode getBuckets(String accountId, String applicationKey) throws IOException {
        accountId = ExtractFromAuthorizeAccount(accountId, applicationKey);

        String listBucketsUrl = apiUrl + "/b2api/v2/b2_list_buckets";

        headers = new HttpHeaders();
        headers.set("Authorization", auth);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map= new HashMap<>();
        map.put("accountId", accountId);

        HttpEntity<?> request = new HttpEntity<Object>(map, headers);

        response = restTemplate.exchange(listBucketsUrl, HttpMethod.POST, request, String.class);

        return mapper.readTree(response.getBody()).get("buckets");
    }

    private String ExtractFromAuthorizeAccount(String accountId, String applicationKey) {
        JsonNode node = null;
        try {
            node = mapper.readTree(authorizeAccount(accountId, applicationKey));
        } catch (IOException e) {
            e.printStackTrace();
        }
        apiUrl = node.findValue("apiUrl").asText();
        auth = node.findValue("authorizationToken").asText();
        accountId = node.findValue("accountId").asText();
        downloadUrl = node.findValue("downloadUrl").asText();
        return accountId;
    }

}
