package com.wgmf.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@ComponentScan(basePackages ={"com.wgmf.web.controller"}) 
public class SwaggerConfig {

	



	private ApiInfo testApiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"管易接入服务",// 大标题
				"EHR Platform's REST API, all the applications could access the Object model data via JSON.",// 小标题
				"0.1",// 版本
				"NO terms of service", "yangzhilin",// 作者
				"The Apache License, Version 1.0",// 链接显示文字
				"http://www.apache.org/licenses/LICENSE-2.0.html"// 网站链接
		);

		return apiInfo;
	}

	private ApiInfo demoApiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"Electronic Health Record(EHR) Platform API",// 大标题
				"EHR Platform's REST API, for system administrator",// 小标题
				"1.0",// 版本
				"NO terms of service", "365384722@qq.com",// 作者
				"The Apache License, Version 2.0",// 链接显示文字
				"http://www.apache.org/licenses/LICENSE-2.0.html"// 网站链接
		);

		return apiInfo;
	}
}