package com.library.feign

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class NaverErrorDecoderTest {

    @Mock
    lateinit var objectMapper: ObjectMapper

    @InjectMocks
    lateinit var naverErrorDecoder: NaverErrorDecoder

}