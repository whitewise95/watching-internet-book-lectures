package com.jojoldu.book.springboot.web.dto;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class HelloResponseDto {

    private final String name;
    private final int amount;

}
