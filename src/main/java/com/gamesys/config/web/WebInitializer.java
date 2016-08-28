package com.gamesys.config.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Code-based configuration is detected and automatically used to initialize any Servlet 3 container.
 */

@EnableWebMvc
@Configuration
public class WebInitializer implements WebApplicationInitializer {


    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        
        final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.gamesys.*");
        servletContext.addListener(new ContextLoaderListener(context));
        final ServletRegistration.Dynamic dispatcherServlet = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/*");
        dispatcherServlet.setAsyncSupported(true);
    }
    

}