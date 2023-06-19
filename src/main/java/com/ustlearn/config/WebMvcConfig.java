package com.ustlearn.config;

import com.ustlearn.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
//configuration 配置类,项目启动时就会调用下面的方法
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     *
     * @param registry
     */
    //增加资源处理器，前端访问页面默认在static目录下，此处没放在static目录下，因此设置静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        //将前端请求的路径/backend/**全部映射到resources下的backend目录.classpath指的resources目录
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 扩展mvc消息转换器对象
     * 把result返回的结果转为json格式,mvc框架有自带的消息转换器,这里为扩展.解决long类型的id在前端页面
     * 丢失精度的问题.转为string类型.
     *
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置转换器对象,底层使用Jackson将java转为json,为common中添加的通用类
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架转换器的集合中.
        //0索引将该转换器放到前面优先使用,当完不成一些转换还会使用其它转换器.
        converters.add(0, messageConverter);

    }
}
