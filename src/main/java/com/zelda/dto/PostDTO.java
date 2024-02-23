package com.zelda.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PostDTO {
    @NotEmpty(message = "Tiêu đề không hợp lệ")
    private String title;

//    @Email(message = "Email không hợp lệ")
    @NotEmpty(message = "Mô tả không hợp lệ")
    private String content;

//    @NotEmpty(message = "Thiếu password")
//    @Min(value = 8, message = "Password phải từ 8 kí tự trở lên")
//    private String password;
    private int status;

    @NotEmpty(message = "Nội dung bài viết không hợp lệ")
    private String detail;

    @NotEmpty(message = "Thể loại bài viết không hợp lệ")
    private String category;

    @NotEmpty(message = "Phạm vi bài viết không hợp lệ")
    private String description;
}
