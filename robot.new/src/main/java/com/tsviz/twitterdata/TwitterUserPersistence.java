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
// OVERVIEW: TwitterUserPersistence.java
// ========
// Class definition for twitter user persistence.
//
// Authors: Ricardo Rios, Rodrigo Mello, and Paulo Pagliosa
// Last revision: 20/04/2017

package com.tsviz.twitterdata;

import java.sql.*;
import com.tsviz.persistence.*;


/////////////////////////////////////////////////////////////////////
//
// TwitterUserPersistence: twitter user persistence class
// ======================
public class TwitterUserPersistence
  extends TwitterDataPersistence<TwitterUser>
{
  @Override
  public void insert(Database db, TwitterUser u)
    throws PersistenceException
  {
    try
    {
      PreparedStatement s = prepareInsert(db, Query.INSERT.sql, false);

      s.setLong(1, u.getId());
      s.setString(2, u.getName());
      s.setString(3, u.getScreen_name());
      s.setString(4, u.getLocation());
      s.setTimestamp(5, new Timestamp(u.getCreated_at()));
      s.setInt(6, u.getUtc_offset());
      s.setString(7, u.getTime_zone());
      s.setString(8, u.getLang());
      s.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new PersistenceException(e);
    }
  }

  @Override
  public TwitterUserIterator findAll(Database db)
    throws PersistenceException
  {
    return new TwitterUserIterator(db, Query.FIND_ALL.sql);
  }

  public TwitterUser findById(Database db, long user_id)
    throws PersistenceException
  {
    return find(db, Query.FIND_BY_ID.sql, user_id);
  }

  private TwitterUser find(Database db, String sql, Object... args)
    throws PersistenceException
  {
    return getInstance(new TwitterUserIterator(db, sql, args));
  }

  private static enum Query
  {
    INSERT("insert into user_twitter (id,name,screen_name,location," +
      "created_at,utc_offset,time_zone,lang) values (?,?,?,?,?,?,?,?)"),
    FIND_ALL("select id,name,screen_name,location,created_at,utc_offset," +
      "time_zone,lang from user_twitter"),
    FIND_BY_ID("select id,name,screen_name,location,created_at,utc_offset," +
      "time_zone,lang from user_twitter where id=%d");

    final String sql;

    Query(String sql)
    {
      this.sql = sql;
    }

    @Override
    public String toString()
    {
      return sql;
    }

  } // Query


} // HashtagPersistence
