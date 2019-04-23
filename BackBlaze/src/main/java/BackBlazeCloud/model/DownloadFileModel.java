package BackBlazeCloud.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class DownloadFileModel {

    @NotEmpty(message = "Destination directory cannot be null or empty")
    @JsonProperty("destinationDirectory")
    private String destinationDirectory;

    @NotEmpty(message = "Bucket name cannot be null or empty")
    @JsonProperty("bucketName")
    private String bucketName;

    @NotEmpty(message = "accountId cannot be null or empty")
    @JsonProperty("accountId")
    private String accountId;

    @NotEmpty(message = "applicationKey cannot be null or empty")
    @JsonProperty("applicationKey")
    private String applicationKey;

    public String getDestinationDirectory() {
        return destinationDirectory;
    }

    public void setDestinationDirectory(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getApplicationKey() {
        return applicationKey;
    }

    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }
}
