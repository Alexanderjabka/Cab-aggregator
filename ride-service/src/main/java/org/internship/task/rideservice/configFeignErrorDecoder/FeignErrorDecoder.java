package org.internship.task.rideservice.configFeignErrorDecoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.internship.task.rideservice.exceptions.ErrorResponse;
import org.internship.task.rideservice.exceptions.feignExceptions.FeignClientException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    @SneakyThrows
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String responseBody = readResponseBody(response);
        System.out.println("FeignErrorDecoder: raw response body -> " + responseBody); // Логируем JSON

        ErrorResponse exceptionDto = objectMapper.readValue(responseBody, ErrorResponse.class);

        System.out.println("FeignErrorDecoder: parsed ErrorResponse -> " + exceptionDto);

        return new FeignClientException(exceptionDto);
    }

    @SneakyThrows
    private String readResponseBody(Response response) {
        if (Objects.nonNull(response.body())) {
            @Cleanup InputStreamReader inputStreamReader = new InputStreamReader(response.body()
                    .asInputStream(), StandardCharsets.UTF_8);
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
        return "";
    }
}
