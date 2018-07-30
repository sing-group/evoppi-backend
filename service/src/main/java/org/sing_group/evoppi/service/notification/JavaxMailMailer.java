/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
package org.sing_group.evoppi.service.notification;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.sing_group.evoppi.service.spi.notification.Mailer;

@Default
public class JavaxMailMailer implements Mailer {

  @Resource(name = "java:/evoppi/mail")
  private Session mailSession;
  
  @Override
  public void sendEmail(String from, String to, String subject, String message) {
    try {
      final MimeMessage messageBody = new MimeMessage(mailSession);

      messageBody.setFrom(new InternetAddress(from));
      messageBody.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      
      messageBody.setSubject(subject);
      
      messageBody.setContent(message, "text/html; charset=utf-8");
      
      Transport.send(messageBody);
    } catch (MessagingException e) {
      throw new RuntimeException("Error sending email notification", e);
    }
  }

}
