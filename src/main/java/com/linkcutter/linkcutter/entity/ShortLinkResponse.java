package com.linkcutter.linkcutter.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShortLinkResponse {
    private String shortLink;
}
