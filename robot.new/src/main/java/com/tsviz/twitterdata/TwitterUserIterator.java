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
// OVERVIEW: TwitterUserIterator.java
// ========
// Class definition for twitter user iterator.
//
// Authors: Ricardo Rios, Rodrigo Mello, and Paulo Pagliosa
// Last revision: 15/04/2017

package com.tsviz.twitterdata;

import java.sql.*;
import com.tsviz.persistence.*;


/////////////////////////////////////////////////////////////////////
//
// TwitterUserIterator: twitter user iterator class
// ===================
public class TwitterUserIterator
  extends AbstractIterator<TwitterUser>
{
  public TwitterUserIterator(Database db, String sql, Object... args)
    throws PersistenceException
  {
    super(db, sql, args);
  }

  @Override
  protected TwitterUser createObject(ResultSet rs)
    throws SQLException
  {
    TwitterUser u = new TwitterUser();

    u.setId(rs.getLong("id"));
    u.setName(rs.getString("name"));
    u.setScreen_name(rs.getString("screen_name"));
    u.setLocation(rs.getString("location"));
    u.setCreated_at(rs.getTimestamp("created_at").getTime());
    u.setUtc_offset(rs.getInt("utc_offset"));
    u.setTime_zone(rs.getString("time_zone"));
    u.setLang(rs.getString("lang"));
    return u;
  }

} // TwitterUserIterator
