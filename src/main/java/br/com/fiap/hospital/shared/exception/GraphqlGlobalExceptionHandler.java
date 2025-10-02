package br.com.fiap.hospital.shared.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorException;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Map;

@ControllerAdvice
public class GraphqlGlobalExceptionHandler {

    @GraphQlExceptionHandler(ResourceNotFoundException.class)
    public GraphQLError handleNotFound(ResourceNotFoundException ex, DataFetchingEnvironment env) {
        return GraphqlErrorException.newErrorException()
                .errorClassification(ErrorType.NOT_FOUND)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath().toList())
                .extensions(Map.of("status", 404, "code", "RESOURCE_NOT_FOUND"))
                .build();
    }

    @GraphQlExceptionHandler(ValidateConsultationException.class)
    public GraphQLError handleValidation(ValidateConsultationException ex, DataFetchingEnvironment env) {
        return GraphqlErrorException.newErrorException()
                .errorClassification(ErrorType.BAD_REQUEST)   // semântica; status nas extensions
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath().toList())
                .extensions(Map.of("status", 409, "code", "SCHEDULE_CONFLICT"))
                .build();
    }

}
