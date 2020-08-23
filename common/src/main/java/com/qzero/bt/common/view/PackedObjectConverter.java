package com.qzero.bt.common.view;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Component
public class PackedObjectConverter implements HttpMessageConverter<PackedObject> {

    @Autowired
    private IPackedObjectFactory factory;


    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(MediaType.parseMediaType("application/json"));
    }

    @Override
    public PackedObject read(Class<? extends PackedObject> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        String json = StreamUtils.copyToString(inputMessage.getBody(), Charset.forName("UTF-8"));
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(json,factory.getPackedObject().getClass());
    }

    @Override
    public void write(PackedObject object, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        String json=mapper.writeValueAsString(object);
        outputMessage.getBody().write(json.getBytes("UTF-8"));
    }
}
