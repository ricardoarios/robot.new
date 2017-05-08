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
// OVERVIEW: Message.java
// ========
// Class definition for Twitter API message.
//
// Authors: Ricardo Rios
// Last revision: 15/04/2017

package com.tsviz.network;

import java.io.Serializable;


/////////////////////////////////////////////////////////////////////
//
// Message: Twitter API message class
// =======
public class Message implements Serializable {

  private static final long serialVersionUID = 1898361689851887657L;
  private int type;
  private String[] hashtags;

  public Message() {
    super();
  }

  public Message(int type, String[] hashtags) {
    super();
    this.type = type;
    this.hashtags = hashtags;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String[] getHashtags() {
    return hashtags;
  }

  public void setHashtags(String[] hashtags) {
    this.hashtags = hashtags;
  }

  @Override
  public String toString() {
    String ret = this.getClass().getName() + "\n\tType: " + this.type + "\n\tValues:";
    for (String value : this.hashtags) {
      ret += " " + value;
    }
    ret += "\n";
    return ret;
  }

} // Message
