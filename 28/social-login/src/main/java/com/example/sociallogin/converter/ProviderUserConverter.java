package com.example.sociallogin.converter;

public interface ProviderUserConverter<T, R> {

    R convert(T t);
}
