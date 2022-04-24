/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2017 - 2022 Noé Vázquez González, Miguel Reboiro-Jato, Jorge Vieira, Hugo López-Fernández, 
 * 		and Cristina Vieira
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.evoppi.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.apache.commons.fileupload.MultipartStream;

@Consumes(MediaType.MULTIPART_FORM_DATA)
public abstract class MultipartMessageBodyReader<T> implements MessageBodyReader<T> {

  protected byte[] toByteArray(InputStream stream) throws IOException {
    byte[] buffer = new byte[1024];
    int readed = -1;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while ((readed = stream.read(buffer)) != -1) {
      baos.write(buffer, 0, readed);
    }
    return baos.toByteArray();
  }


  @Override
  public boolean isReadable(
    Class<?> type,
    Type genericType,
    Annotation[] annotations,
    MediaType mediaType
    ) {
    try {
      return Class.forName(
        genericType.getTypeName()
        )
        .isAssignableFrom(type);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private byte[] findBoundary(MultivaluedMap<String, String> httpHeaders) {
    int boundaryPos = httpHeaders.get("Content-Type").get(0).indexOf("boundary=");
    if (boundaryPos != -1) {
      return
        httpHeaders.get("Content-Type").get(0).substring(boundaryPos + "boundary=".length()).trim().getBytes();
    } else {
      return null;
    }
  }
  @Override
  public T readFrom(
    Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
    MultivaluedMap<String, String> httpHeaders, InputStream entityStream
    )
    throws IOException, WebApplicationException {

    this.init();

    try {
      byte[] boundary = findBoundary(httpHeaders);
      if (boundary != null) {
        
        MultipartStream multipartStream = new MultipartStream(entityStream, boundary, 8192, null);
        
        boolean nextPart = multipartStream.skipPreamble();
        while (nextPart) {
          String header = multipartStream.readHeaders();
          String name = getName(header);
          
          if (header.contains("filename=")) {
            // it is a file, copy it to a temp file and open an stream to in that when it is closed, the file is 
            // removed
            Path tempFile = Files.createTempFile("http_file_uploaded", "data");
            OutputStream fout = new FileOutputStream(tempFile.toFile());
            multipartStream.readBodyData(fout);
            fout.close();

            this.add(name, tempFile.toFile());

          } else {
            // it is not a file
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            multipartStream.readBodyData(baos);
            this.add(name, new String(baos.toByteArray()));
          }
          
          
          nextPart = multipartStream.readBoundary();
        }
      } else {
        throw new IllegalArgumentException("No boundary found in this multipart request");
      }
        
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return this.build();
  }

  private String getName(String header) {
    Pattern pattern = Pattern.compile(".*?name=\"([^;]*)\".*");
    Matcher matcher = pattern.matcher(header);
    String name = "";
    if (matcher.find()) {
      name = matcher.group(1);
    }
    return name;
  }

  // Template methods
  protected abstract void init();

  protected abstract void add(String name, String value);
  protected abstract void add(String name, File uploadedFile);

  protected abstract T build();
}
