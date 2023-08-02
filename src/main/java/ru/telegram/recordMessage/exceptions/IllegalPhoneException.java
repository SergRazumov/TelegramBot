package ru.telegram.recordMessage.exceptions;

/**
 * Исключение, пробрасываемое в случае получения некорректного телефонного номера
 */
public class IllegalPhoneException extends IllegalArgumentException {

    public IllegalPhoneException(String s) {
        super(s);
    }
}