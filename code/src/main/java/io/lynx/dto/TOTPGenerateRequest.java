package io.lynx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class TOTPGenerateRequest {
    private String accountName;
}
