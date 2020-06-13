package com.nimesh.real_estate_ms.repository.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPage<T> {
    private List<T> content;
    private Long offset;
    private Integer pageSize;
    private Long totalSize;
}
