package com.linkcutter.linkcutter.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LinkServiceProperties {
    private final int shortLinkIdLength;
    private final String appDomainName;
}
