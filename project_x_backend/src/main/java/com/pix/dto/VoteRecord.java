package com.pix.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteRecord {
    private String unitName;
    private Long voteCount;
}
