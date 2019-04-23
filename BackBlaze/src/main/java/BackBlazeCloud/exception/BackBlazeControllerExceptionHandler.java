package BackBlazeCloud.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class BackBlazeControllerExceptionHandler {

    // exception thrown when there is no information for the productId in the database
    @ExceptionHandler(BucketOrFileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public BackBlazeControllerExceptionResponse ProductDetailsNotFound (BucketOrFileNotFoundException ex){
        BackBlazeControllerExceptionResponse response = new BackBlazeControllerExceptionResponse();
        response.setErrorMessage(ex.getMessage());
        return response;
    }

    // exception thrown when there is Mismatch in the input
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BackBlazeControllerExceptionResponse NumberFormatException (NumberFormatException ex){
        BackBlazeControllerExceptionResponse response = new BackBlazeControllerExceptionResponse();
        response.setErrorMessage("Invalid input format, " + ex.getMessage() + ". It should be an integer");
        return response;
    }

    // exception thrown for invalid format
    @ExceptionHandler(FileWriteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BackBlazeControllerExceptionResponse InputFormatException (FileWriteException ex){
        BackBlazeControllerExceptionResponse response = new BackBlazeControllerExceptionResponse();
        response.setErrorMessage(ex.getMessage());
        return response;
    }

    // exception thrown when wrong data format is provided in json body for put request
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BackBlazeControllerExceptionResponse InvalidFormatException (InvalidFormatException ex){
        BackBlazeControllerExceptionResponse response = new BackBlazeControllerExceptionResponse();
        response.setErrorMessage("Please check the datatype of supplied body.");
        return response;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BackBlazeControllerExceptionResponse ValidationException (ValidationException ex){
        BackBlazeControllerExceptionResponse response = new BackBlazeControllerExceptionResponse();
        response.setErrorMessage(ex.getMessage());
        return response;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BackBlazeControllerExceptionResponse HttpClientError (HttpClientErrorException ex){
        BackBlazeControllerExceptionResponse response = new BackBlazeControllerExceptionResponse();
        response.setErrorMessage(ex.getMessage());
        return response;
    }

    @ExceptionHandler(JsonParsingException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public BackBlazeControllerExceptionResponse JsonParsingException (JsonParsingException ex){
        BackBlazeControllerExceptionResponse response = new BackBlazeControllerExceptionResponse();
        response.setErrorMessage(ex.getMessage());
        return response;
    }

}
