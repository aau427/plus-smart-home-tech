package andrey.controller;

import andrey.exception.base.BaseGlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler extends BaseGlobalExceptionHandler {
}
