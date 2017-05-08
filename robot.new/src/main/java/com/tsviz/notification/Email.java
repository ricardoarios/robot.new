//[]---------------------------------------------------------------[]
//|                                                                 |
//| Copyright (C) 2015-2017 TsViz Group.                            |
//|                                                                 |
//| This software is provided 'as-is', without any express or       |
//| implied warranty. In no event will the authors be held liable   |
//| for any damages arising from the use of this software.          |
//|                                                                 |
//| Permission is granted to anyone to use this software for any    |
//| purpose, including commercial applications, and to alter it and |
//| redistribute it freely, subject to the following restrictions:  |
//|                                                                 |
//| 1. The origin of this software must not be misrepresented; you  |
//| must not claim that you wrote the original software. If you use |
//| this software in a product, an acknowledgment in the product    |
//| documentation would be appreciated but is not required.         |
//|                                                                 |
//| 2. Altered source versions must be plainly marked as such, and  |
//| must not be misrepresented as being the original software.      |
//|                                                                 |
//| 3. This notice may not be removed or altered from any source    |
//| distribution.                                                   |
//|                                                                 |
//[]---------------------------------------------------------------[]
//
// OVERVIEW: Email.java
// ========
// Class definition for email.
//
// Authors: Ricardo Rios
// Last revision: 15/04/2017

package com.tsviz.notification;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/////////////////////////////////////////////////////////////////////
//
// Email: email class
// ====
public class Email {

  private Properties props;
  private final Session session;
  private final String email;

  // constructor using TLS
  public Email(final String username, final String password) {
    this.props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    session = Session.getInstance(props,
            new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
    this.email = username;
  }

  public void sendMessage(String subject, String content, String emailTo)
          throws MessagingException {
    Message message = new MimeMessage(session);

    message.setFrom(new InternetAddress(this.email));
    message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(emailTo));
    message.setSubject(subject);
    message.setText(content);

    Transport.send(message);
  }

} // Email
