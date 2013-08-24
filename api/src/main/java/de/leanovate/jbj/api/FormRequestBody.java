package de.leanovate.jbj.api;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Form request body.
 * <p/>
 * Use this in for content type "application/form-url-encoded".
 */
public interface FormRequestBody extends RequestBody {
    @Nonnull
    Map<String, List<String>> getFormData();
}
