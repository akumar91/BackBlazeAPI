package BackBlazeCloud.controller;

import BackBlazeCloud.exception.ValidationException;
import BackBlazeCloud.model.DownloadFileModel;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import BackBlazeCloud.service.DownloadFilesFromBucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/backBlaze")
public class BackBlazeController {

    @Autowired
    private DownloadFilesFromBucketService downloadFilesFromBucketService;

    // PUT request to update the price of the product based on product id
    @PostMapping(value = "DownloadFilesFromBucket", consumes = "application/json")
    public ResponseEntity<String> SaveFiles(@Valid @RequestBody DownloadFileModel downloadFileModel,
                                            BindingResult validationResult)
            throws InvalidFormatException {
        if (validationResult.hasErrors()) {
            StringBuilder fields = new StringBuilder();
            for (FieldError error: validationResult.getFieldErrors()) {
                if (error.getDefaultMessage() != null && !error.getDefaultMessage().isEmpty()) {
                    fields.append(error.getField()).append(" because ")
                            .append(error.getDefaultMessage()).append(" , ");
                } else {
                    fields.append(error.getField()).append(" , ");
                }

            }
            throw new ValidationException("Invalid request fields: " + fields.toString());
        }
        downloadFilesFromBucketService.downloadFilesFromBucket(downloadFileModel);

        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NO_CONTENT);
    }
}
