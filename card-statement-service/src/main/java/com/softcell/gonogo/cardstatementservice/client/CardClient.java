package com.softcell.gonogo.cardstatementservice.client;

import com.softcell.gonogo.cardstatementservice.service.CardService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "creditcard-service")
public interface CardClient extends CardService {


}
