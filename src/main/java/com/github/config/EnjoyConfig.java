package com.github.config;

import com.jfinal.core.JFinal;
import com.jfinal.template.ext.spring.JFinalViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnjoyConfig {
	@Bean(value = "jfinalViewResolver")
	public JFinalViewResolver getJFinalViewResolver() {
		JFinalViewResolver jf = new JFinalViewResolver();
		jf.setDevMode(true);
		jf.setCache(false);
		// jf.setSourceFactory(new ClassPathSourceFactory());
		jf.setPrefix("/WEB-INF/_views/");
		// jf.setSuffix(".html");
		jf.setContentType("text/html;charset=UTF-8");
		jf.setOrder(0);
		jf.addSharedObject("ctxPath", JFinal.me().getContextPath());
//		jf.addSharedMethod(new ShareUtils());
		return jf;
	}

}
