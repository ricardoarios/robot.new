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
// OVERVIEW: MonitoredHashTags.java
// ========
// Class definition for monitored hashtag.
//
// Authors: Ricardo Rios
// Last revision: 15/04/2017

package com.tsviz.robot;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.tsviz.twitterdata.Hashtag;


/////////////////////////////////////////////////////////////////////
//
// MonitoredHashTags: monitored hashtags class
// =================
public class MonitoredHashTags
{
  private ArrayList<Hashtag> hashtags;
  static final String CLASS_NAME = MonitoredHashTags.class.getName();
  static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

  public MonitoredHashTags(ArrayList<Hashtag> hashtags) {
    super();
    this.hashtags = hashtags;
    LOGGER.debug("Creating " + CLASS_NAME + " object");
  }

  public MonitoredHashTags() {
    super();
    this.hashtags = new ArrayList<>();
    LOGGER.debug("Creating " + CLASS_NAME + " object");
  }

  public ArrayList<Hashtag> getHashtags() {
    LOGGER.debug("Getting hashtag list");
    return hashtags;
  }

  public void setHashtags(ArrayList<Hashtag> hashtags) {
    LOGGER.debug("Setting hashtag list");
    this.hashtags = hashtags;
  }

  public void addHashtags(Hashtag hashtag) {
    if (!hashtags.contains(hashtag)) {
      LOGGER.debug("Starting monitoring the hashtag: " + hashtag);
      this.hashtags.add(hashtag);
    } else {
      LOGGER.warn("This hashtag " + hashtag + "has been already monitored");
    }
  }

  public boolean removeHashtags(Hashtag hashtag) {
    LOGGER.trace("Stopping monitoring the hashtag: " + hashtag);
    if (hashtags.contains(hashtag)) {
      LOGGER.debug("Hashtag removed: " + hashtag);
      return this.hashtags.remove(hashtag);
    } else {
      LOGGER.warn("This hashtag " + hashtag + " has not been monitored");
      return false;
    }
  }

  @Override
  public String toString() {
    LOGGER.debug("Printing the monitored hashtag list");
    return ("List of monitored hashtag:" + this.hashtags);
  }

  public Hashtag contains(String name) {
    for (Hashtag ht : this.getHashtags()) {
      if (ht.getHashtag().equals(name)) {
        return ht;
      }
    }
    return null;
  }

} // MonitoredHashtags
