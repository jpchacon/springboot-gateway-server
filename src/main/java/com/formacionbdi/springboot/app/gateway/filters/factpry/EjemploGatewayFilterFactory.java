package com.formacionbdi.springboot.app.gateway.filters.factpry;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Log
@Component
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion> {

    public EjemploGatewayFilterFactory() {
        super(Configuracion.class);
    }

    @Override
    public GatewayFilter apply(Configuracion config) {
        return (exchange, chain) ->{
            log.info("Ejecutando pre gateway filter factory " + config.getMensaje());

            return chain.filter(exchange).then(Mono.fromRunnable(() ->{
                Optional.ofNullable(config.getCookieValor()).ifPresent(cookie -> {
                    exchange.getResponse().addCookie(ResponseCookie.from(config.getCookieNombre(), cookie).build());
                });
                log.info("Ejecutando post gateway filter factory: " + config.getMensaje());
            }));
        };
    }

    @Getter @Setter
    public static class Configuracion{
        private String mensaje;
        private String cookieValor;
        private String cookieNombre;
    }
}