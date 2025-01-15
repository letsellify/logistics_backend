package com.letsellify.logistics.common.config.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Ahmad",
                        email = "ahmadbubacherryfield@gmail.com"
                ),
                description = "OpenApi documentation for Oasis Task Management Assessment Task",
                title = "OpenApi specification - Oasis TaskTodo App Assessment",
                version = "1.0"
        ),
        servers = {
                @Server(
                        url = "/"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

public class DocumentationConfiguration {

}
