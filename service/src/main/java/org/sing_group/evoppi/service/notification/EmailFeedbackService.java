/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Noé Vázquez González
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
import javax.inject.Inject;

import org.sing_group.evoppi.service.notification.entity.Feedback;
import org.sing_group.evoppi.service.spi.notification.FeedbackService;
import org.sing_group.evoppi.service.spi.notification.Mailer;

public class EmailFeedbackService implements FeedbackService {
  @Inject
  private Mailer mailer;

  @Resource(name = "java:global/evoppi/email/from")
  private String emailFrom;

  @Resource(name = "java:global/evoppi/feedback/targets")
  private String feedbackEmails;

  @Resource(name = "java:global/evoppi/feedback/subject")
  private String feedbackSubject;

  @Resource(name = "java:global/evoppi/feedback/message")
  private String feedbackMessage;

  @Override
  public void notifyFeedback(Feedback feedback) {
    final String[] toEmails = this.feedbackEmails.split(",");

    this.mailer.sendEmail(this.emailFrom, toEmails, this.feedbackSubject, this.composeEmail(feedback));
  }

  private String composeEmail(Feedback feedback) {
    return this.feedbackMessage
      .replaceAll("\\[AUTHOR_EMAIL\\]", feedback.getEmail())
      .replaceAll("\\[SUBJECT\\]", feedback.getSubject())
      .replaceAll("\\[MESSAGE\\]", feedback.getMessage());
  }

}
