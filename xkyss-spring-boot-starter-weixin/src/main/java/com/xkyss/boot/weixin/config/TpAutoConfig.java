package com.xkyss.boot.weixin.config;

import com.xkyss.boot.weixin.constant.TpConsts;
import com.xkyss.weixin.common.property.WxErrorCodeProperties;
import com.xkyss.weixin.common.service.WxCacheService;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.common.service.impl.WxCacheServiceImpl;
import com.xkyss.weixin.common.service.impl.WxHttpClientServiceImpl;
import com.xkyss.weixin.tp.property.TpAppProperties;
import com.xkyss.weixin.tp.property.TpContactProperties;
import com.xkyss.weixin.tp.property.TpProperties;
import com.xkyss.weixin.tp.service.*;
import com.xkyss.weixin.tp.service.impl.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TpAutoConfig implements TpConsts {

    @Bean
    @ConfigurationProperties(prefix = TP_CONFIG_PREFIX)
    public TpProperties tpConfig(TpContactProperties contact, TpAppProperties app) {
        return TpProperties.builder()
                .contact(contact)
                .mainApp(app)
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = TP_CONFIG_PREFIX + ".contact")
    public TpContactProperties tpContactProperties() {
        return new TpContactProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = TP_CONFIG_PREFIX + ".main-app")
    public TpAppProperties tpAppProperties() {
        return new TpAppProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = WX_CONFIG_PREFIX + ".error-code")
    public WxErrorCodeProperties wxErrorCodeProperties() {
        return new WxErrorCodeProperties();
    }

    @Bean
    public WxHttpClientService wxHttpClientService(
            WxCacheService wxCacheService,
            WxErrorCodeProperties errorCodes
    ) {
        return new WxHttpClientServiceImpl(errorCodes, wxCacheService);
    }

    @Bean
    public WxCacheService wxCacheService() {
        return new WxCacheServiceImpl();
    }

    @Bean(TP_APP_CONTACT)
    public TpContactService contactService(TpProperties properties, WxCacheService cacheService, WxHttpClientService httpClientService) {
        return new TpContactServiceImpl(properties, cacheService, httpClientService);
    }

    @Bean(TP_APP_MAIN_APP)
    public TpAppService mainAppService(TpProperties properties, WxCacheService cacheService, WxHttpClientService httpClientService) {
        return new TpMainAppServiceImpl(properties, cacheService, httpClientService);
    }

    @Bean
    public TpBatchService batchService(TpContactService contactService, WxHttpClientService httpClientService) {
        return new TpBatchServiceImpl(contactService, httpClientService);
    }

    @Bean
    public TpDeptService deptService(TpContactService contactService, WxHttpClientService httpClientService) {
        return new TpDeptServiceImpl(contactService, httpClientService);
    }

    @Bean
    public TpMediaService mediaService(TpContactService contactService, WxHttpClientService httpClientService) {
        return new TpMediaServiceImpl(contactService, httpClientService);
    }

    @Bean
    public TpUserService userService(
            @Qualifier(TP_APP_MAIN_APP) TpAppService mainAppService,
            @Qualifier(TP_APP_CONTACT) TpContactService contactService,
            WxHttpClientService httpClientService) {

        return new TpUserServiceImpl(mainAppService, contactService, httpClientService);
    }
}
