package com.softcell.gonogo.cardstatementservice.client;

import com.softcell.gonogo.cardstatementservice.service.StatementService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("statement-service")
public interface StatementClient  extends StatementService{
}
