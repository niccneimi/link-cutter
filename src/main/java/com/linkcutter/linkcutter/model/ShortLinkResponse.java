package com.linkcutter.linkcutter.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShortLinkResponse {
    private String shortLink;
}
