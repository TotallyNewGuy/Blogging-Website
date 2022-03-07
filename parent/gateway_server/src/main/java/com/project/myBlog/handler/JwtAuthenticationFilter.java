package com.project.myBlog.handler;

import com.project.myBlog.dao.pojo.SysUser;
import com.project.myBlog.service.TokenService;
import com.project.myBlog.utils.UserThreadLocal;
import com.project.myBlog.vo.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class JwtAuthenticationFilter implements GatewayFilter {

	@Autowired
	private TokenService tokenService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

		if (!request.getHeaders().containsKey("Authorization")) {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return response.setComplete();
		}

		String token = request.getHeaders().getOrEmpty("Authorization").get(0);
		SysUser sysUser = tokenService.checkToken(token);

		if (StringUtils.isBlank(token) || sysUser == null) {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.BAD_REQUEST);
//			response.setStatusCode(HttpStatus.EXPECTATION_FAILED);
			return response.setComplete();
		}

		UserThreadLocal.put(sysUser);
		return chain.filter(exchange);
	}

}