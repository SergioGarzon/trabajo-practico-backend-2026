package com.catedra.backend.compraventa.client;

import com.catedra.backend.compraventa.dto.BilleteraOperacionRequestDto;
import com.catedra.backend.compraventa.dto.BilleteraOperacionResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "billetera-service", url = "${servicios.usuarios.url}")
public interface BilleteraClient {

    @PutMapping("/api/billetera/operacion/bloquear")
    BilleteraOperacionResponseDto bloquearFondos(@RequestBody BilleteraOperacionRequestDto requestDto);

    @PutMapping("/api/billetera/operacion/resolver")
    BilleteraOperacionResponseDto resolverFondos(@RequestBody BilleteraOperacionRequestDto requestDto);
}
