package com.nju.test;

import com.nju.model.AuthenticationAccessToken;

/**
 * Created by xiaojuzhang on 2016/3/24.
 */
public class TestToken {

    public static AuthenticationAccessToken getToken() {
        AuthenticationAccessToken accessToken = new AuthenticationAccessToken();
        accessToken.setTokenId("rqPYuXw1J86qSHsj5IzxymUI9jwXpRVo");
        accessToken.setUserId(1);
        accessToken.setDiviceId("NtL2JVVgHIZMPEvv9Gd3Fd03Q6n8SVhh");
        accessToken.setAppId("sdQBTAE6uNpjSvcx8kd2RCxMTBxtMTMM");
        return accessToken;
    }

}
