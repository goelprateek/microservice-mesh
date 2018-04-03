package com.softcell.gonogo.gateway.config.apidocs;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class PageableParameterBuilderPlugin implements OperationBuilderPlugin {

    private final TypeNameExtractor nameExtractor;
    private final TypeResolver resolver;
    private final ResolvedType pageableType;

    public PageableParameterBuilderPlugin(TypeNameExtractor nameExtractor, TypeResolver resolver) {
        this.nameExtractor = nameExtractor;
        this.resolver = resolver;
        this.pageableType = resolver.resolve(Pageable.class, new Type[0]);
    }

    public boolean supports(DocumentationType delimiter) {
        return DocumentationType.SWAGGER_2.equals(delimiter);
    }

    public void apply(OperationContext context) {
        List<ResolvedMethodParameter> methodParameters = context.getParameters();
        List<Parameter> parameters = Lists.newArrayList();
        Iterator var4 = methodParameters.iterator();

        while(var4.hasNext()) {
            ResolvedMethodParameter methodParameter = (ResolvedMethodParameter)var4.next();
            ResolvedType resolvedType = methodParameter.getParameterType();
            if (this.pageableType.equals(resolvedType)) {
                ParameterContext parameterContext = new ParameterContext(methodParameter, new ParameterBuilder(), context.getDocumentationContext(), context.getGenericsNamingStrategy(), context);
                Function<ResolvedType, ? extends ModelReference> factory = this.createModelRefFactory(parameterContext);
                ModelReference intModel = (ModelReference)factory.apply(this.resolver.resolve(Integer.TYPE, new Type[0]));
                ModelReference stringModel = (ModelReference)factory.apply(this.resolver.resolve(List.class, new Type[]{String.class}));
                parameters.add((new ParameterBuilder()).parameterType("query").name("page").modelRef(intModel).description("Page number of the requested page").build());
                parameters.add((new ParameterBuilder()).parameterType("query").name("size").modelRef(intModel).description("Size of a page").build());
                parameters.add((new ParameterBuilder()).parameterType("query").name("sort").modelRef(stringModel).allowMultiple(true).description("Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.").build());
                context.operationBuilder().parameters(parameters);
            }
        }

    }

    private Function<ResolvedType, ? extends ModelReference> createModelRefFactory(ParameterContext context) {
        ModelContext modelContext = ModelContext.inputParam(context.getGroupName(), context.resolvedMethodParameter().getParameterType(), context.getDocumentationType(), context.getAlternateTypeProvider(), context.getGenericNamingStrategy(), context.getIgnorableParameterTypes());
        return ResolvedTypes.modelRefFactory(modelContext, this.nameExtractor);
    }
}
