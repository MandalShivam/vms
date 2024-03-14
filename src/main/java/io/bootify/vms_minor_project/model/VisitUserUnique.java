package io.bootify.vms_minor_project.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.vms_minor_project.service.VisitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the id value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = VisitUserUnique.VisitUserUniqueValidator.class
)
public @interface VisitUserUnique {

    String message() default "{Exists.visit.user}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class VisitUserUniqueValidator implements ConstraintValidator<VisitUserUnique, Long> {

        private final VisitService visitService;
        private final HttpServletRequest request;

        public VisitUserUniqueValidator(final VisitService visitService,
                final HttpServletRequest request) {
            this.visitService = visitService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(visitService.get(Long.parseLong(currentId)).getUser())) {
                // value hasn't changed
                return true;
            }
            return !visitService.userExists(value);
        }

    }

}
