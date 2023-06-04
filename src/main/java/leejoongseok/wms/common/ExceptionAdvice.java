package leejoongseok.wms.common;

import leejoongseok.wms.item.exception.AlreadyExistsItemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        final List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .toList();
        log.warn("MethodArgumentNotValidException: {}", errors);
        return new ErrorResponse(errors);
    }

    @ExceptionHandler(AlreadyExistsItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAlreadyExistsItemException(
            final AlreadyExistsItemException e) {
        final List<String> errors = List.of(e.getMessage());
        log.warn("AlreadyExistsItemException: {}", errors);
        return new ErrorResponse(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(
            final NotFoundException e) {
        final List<String> errors = List.of(e.getMessage());
        log.warn("NotFoundException: {}", errors);
        return new ErrorResponse(errors);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(
            final BadRequestException e) {
        final List<String> errors = List.of(e.getMessage());
        log.warn("BadRequestException: {}", errors);
        return new ErrorResponse(errors);
    }

    record ErrorResponse(
            List<String> errors) {
    }
}
