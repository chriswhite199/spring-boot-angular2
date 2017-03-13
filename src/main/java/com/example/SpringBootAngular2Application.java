package com.example;

import org.apache.catalina.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

@SpringBootApplication
public class SpringBootAngular2Application implements EmbeddedServletContainerCustomizer {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootAngular2Application.class, args);
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		if (container instanceof TomcatEmbeddedServletContainerFactory) {
			// disable Cookies, as the app is stateless
			((TomcatEmbeddedServletContainerFactory) container).addContextCustomizers(new TomcatContextCustomizer() {
				@Override
				public void customize(Context context) {
					context.setCookies(false);
				}
			});
		}
	}
}
