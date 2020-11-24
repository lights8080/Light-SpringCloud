package com.light.dto.common;

import com.light.enums.UserModeEnum;
import lombok.Data;

/**
 * user information
 *
 * @author lihaipeng
 * @date 2020-09-22
 */
@Data
public class LoginUserInfo {
    private String loginName;
    private UserModeEnum userMode = UserModeEnum.NORMAL;
}
