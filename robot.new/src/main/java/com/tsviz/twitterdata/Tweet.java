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
// OVERVIEW: Tweet.java
// ========
// Class definition for twet.
//
// Authors: Ricardo Rios
// Last revision: 15/04/2017

package com.tsviz.twitterdata;

import java.util.*;
import twitter4j.GeoLocation;
import twitter4j.Status;
import com.tsviz.persistence.*;


/////////////////////////////////////////////////////////////////////
//
// Tweet: tweet class
// =====
public class Tweet
  implements Persistable
{
  private long tweet_id;
  private long user_id; // column name: id_user
  private long created_at;
  private String text;
  private String source;
  private String lang;
  private double coordinates_longitude;
  private double coordinates_latitude;
  private Set<Hashtag> hashtags;

  public long getTweet_id()
  {
    return tweet_id;
  }

  public void setTweet_id(long tweet_id)
  {
    this.tweet_id = tweet_id;
  }

  public long getUser_id()
  {
    return user_id;
  }

  public void setUser_id(long user_id)
  {
    this.user_id = user_id;
  }

  public long getCreated_at()
  {
    return created_at;
  }

  public void setCreated_at(long created_at)
  {
    this.created_at = created_at;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public String getSource()
  {
    return source;
  }

  public void setSource(String source)
  {
    this.source = source;
  }

  public String getLang()
  {
    return lang;
  }

  public void setLang(String lang)
  {
    this.lang = lang;
  }

  public double getCoordinates_longitude()
  {
    return coordinates_longitude;
  }

  public void setCoordinates_longitude(double coordinates_longitude)
  {
    this.coordinates_longitude = coordinates_longitude;
  }

  public double getCoordinates_latitude()
  {
    return coordinates_latitude;
  }

  public void setCoordinates_latitude(double coordinates_latitude)
  {
    this.coordinates_latitude = coordinates_latitude;
  }

  public void setTweet(Status status)
  {
    tweet_id = status.getId();
    user_id = status.getUser().getId();
    created_at = status.getCreatedAt().getTime();
    text = status.getText();
    source = status.getSource();
    lang = status.getLang();

    GeoLocation gl = status.getGeoLocation();

    if (gl != null)
    {
      coordinates_latitude = gl.getLatitude();
      coordinates_longitude = gl.getLongitude();
    }
  }

  @Override
  public String toString()
  {
    return "Tweet id: " + this.tweet_id +
      "\nUser id: " + this.user_id +
      "\nCreated at: " + this.created_at +
      "\nTweet: " + this.text +
      "\nSource: " + this.source +
      "\n Lang: " + this.lang;
  }

  public Set<Hashtag> getHashtags()
  {
    return hashtags;
  }

  public void setHashtags(Set<Hashtag> hashtags)
  {
    this.hashtags = hashtags;
  }

} // Tweet
