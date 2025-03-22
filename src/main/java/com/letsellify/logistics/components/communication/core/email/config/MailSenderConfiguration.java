package com.letsellify.logistics.components.communication.core.email.config;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:21:10
 */

//@Configuration
//public class MailSenderConfiguration {
//    private static final String MAIL_PROPERTIES_PREFIX = "spring.mail.properties";
//
//    private final Environment environment;
//
//    public MailSenderConfiguration(final Environment environment) {
//        this.environment = environment;
//    }
//
//    /*
//    * default primary mailSender bean
//    * */
//    @Bean
//    @ConfigurationProperties(prefix = "spring.mail")
//    public JavaMailSender mailSender() {
//        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setJavaMailProperties(this.getPropertiesByPrefix(MAIL_PROPERTIES_PREFIX));
//        javaMailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
//        return javaMailSender;
//    }
//
//    private Properties getPropertiesByPrefix(final String prefix) {
//        final Properties result = new Properties();
//        this.environment.getProperty(prefix, "").lines().forEach(line -> {
//            if (line.startsWith(prefix)) {
//                final String key = line.substring(prefix.length() + 1); // Strip prefix and dot
//                result.setProperty(key, this.environment.getProperty(line));
//            }
//        });
//        return result;
//    }
//}
