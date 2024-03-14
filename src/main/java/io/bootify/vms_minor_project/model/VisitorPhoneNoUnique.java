package io.bootify.vms_minor_project.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.vms_minor_project.service.VisitorService;
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
 * Validate that the phoneNo value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = VisitorPhoneNoUnique.VisitorPhoneNoUniqueValidator.class
)
public @interface VisitorPhoneNoUnique {

    String message() default "{Exists.visitor.phoneNo}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class VisitorPhoneNoUniqueValidator implements ConstraintValidator<VisitorPhoneNoUnique, String> {

        private final VisitorService visitorService;
        private final HttpServletRequest request;

        public VisitorPhoneNoUniqueValidator(final VisitorService visitorService,
                final HttpServletRequest request) {
            this.visitorService = visitorService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(visitorService.get(Long.parseLong(currentId)).getPhoneNo())) {
                // value hasn't changed
                return true;
            }
            return !visitorService.phoneNoExists(value);
        }

    }

}
