package kg.bilem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileEmptyException extends RuntimeException{
    private final String message;
}
