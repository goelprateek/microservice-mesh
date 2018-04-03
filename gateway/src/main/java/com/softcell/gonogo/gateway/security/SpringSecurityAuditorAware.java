package com.softcell.gonogo.gateway.security;

import com.softcell.gonogo.gateway.constants.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Created By - prateek - 3/10/17
 */

/**
 * Implementation of AuditorAware based on Spring Security.
 */

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        return userName != null ? userName : Constants.SYSTEM_ACCOUNT;
    }
}
