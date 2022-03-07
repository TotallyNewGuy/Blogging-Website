package com.project.myBlog.config;

import com.project.myBlog.handler.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Autowired
	private JwtAuthenticationFilter filter;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
//				.route("articles", r -> r.path("/articles/**").uri("lb://article-service"))
//				.route("comments", r -> r.path("/comments/**").uri("lb://article-service"))
//				.route("upload", r -> r.path("/upload/**").uri("lb://article-service"))
//				.route("category", r -> r.path("/categorys/**").uri("lb://category-service"))
//				.route("tags", r -> r.path("/tags/**").uri("lb://category-service"))
//				.route("login", r -> r.path("/login/**").uri("lb://user-service"))
//				.route("users", r -> r.path("/users/**").uri("lb://user-service"))
				.route("comment", r -> r.path("/comments/create/change/**").filters(f -> f.filter(filter)).uri("lb://article-service"))
				.route("publish", r -> r.path("/articles/publish/**").filters(f -> f.filter(filter)).uri("lb://article-service")).build();
	}

}