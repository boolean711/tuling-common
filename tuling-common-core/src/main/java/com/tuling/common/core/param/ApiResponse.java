package com.tuling.common.core.param;

import lombok.*;
import lombok.experimental.Accessors;


@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    private boolean showMessage = false;
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "success");
    }


    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }


    public static <T> ApiResponse<T> error(int code) {
        return error(code, "error");
    }

    public static <T> ApiResponse<T> successNoData() {
        return ApiResponse.<T>builder()
                .code(200)
                .build();
    }
}