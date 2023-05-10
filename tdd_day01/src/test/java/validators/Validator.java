package validators;

public interface Validator<T> extends RequiredCheckValidator, LengthCheckValidator {
    void check(T t);
}
