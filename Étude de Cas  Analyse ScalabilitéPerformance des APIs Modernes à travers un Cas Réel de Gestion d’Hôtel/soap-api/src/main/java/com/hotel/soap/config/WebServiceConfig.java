package com.hotel.soap.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Web Services configuration for SOAP API.
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    public static final String NAMESPACE_URI = "http://hotel.com/soap/reservation";

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/soap/*");
    }

    @Bean(name = "reservation")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema reservationSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ReservationPort");
        wsdl11Definition.setLocationUri("/soap");
        wsdl11Definition.setTargetNamespace(NAMESPACE_URI);
        wsdl11Definition.setSchema(reservationSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema reservationSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/reservation.xsd"));
    }
}
