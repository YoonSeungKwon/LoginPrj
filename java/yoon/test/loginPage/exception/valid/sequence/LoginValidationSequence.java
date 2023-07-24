package yoon.test.loginPage.exception.valid.sequence;

import jakarta.validation.GroupSequence;

@GroupSequence({
        ValidationGroup.EmailNotFormat.class,
        ValidationGroup.EmailNotNull.class,
        ValidationGroup.PasswordNotNull.class
})
public interface LoginValidationSequence {
}
