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
// OVERVIEW: TwitterUser.java
// ========
// Class definition for twitter user.
//
// Authors: Ricardo Rios, Rodrigo Mello, and Paulo Pagliosa
// Last revision: 15/04/2017

package com.tsviz.twitterdata;

import twitter4j.Status;
import twitter4j.User;
import com.tsviz.persistence.*;


/////////////////////////////////////////////////////////////////////
//
// TwitterUser: twitter user class
// ===========
public class TwitterUser
  implements Persistable
{
  private long id;
  private String name;
  private String screen_name;
  private String location;
  private long created_at;
  private int utc_offset;
  private String time_zone;
  private String lang;

  public TwitterUser()
  {
    id = -1;
    location = "NA";
  }

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getScreen_name()
  {
    return screen_name;
  }

  public void setScreen_name(String screen_name)
  {
    this.screen_name = screen_name;
  }

  public String getLocation()
  {
    return location;
  }

  public void setLocation(String location)
  {
    this.location = location;
  }

  public long getCreated_at() {
    return created_at;
  }

  public void setCreated_at(long created_at)
  {
    this.created_at = created_at;
  }

  public int getUtc_offset()
  {
    return utc_offset;
  }

  public void setUtc_offset(int utc_offset)
  {
    this.utc_offset = utc_offset;
  }

  public String getTime_zone()
  {
    return time_zone;
  }

  public void setTime_zone(String time_zone)
  {
    this.time_zone = time_zone;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang)
  {
    this.lang = lang;
  }

  public void setUser(Status status)
  {
    User user = status.getUser();

    id = user.getId();
    name = user.getName();
    screen_name = user.getScreenName();
    if (user.getLocation() != null)
      location = user.getLocation();
    created_at = user.getCreatedAt().getTime();
    utc_offset = user.getUtcOffset();
    time_zone = user.getTimeZone();
    lang = user.getLang();
  }

} // TwitterUser
