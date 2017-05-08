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
// OVERVIEW: HashtagPersistence.java
// ========
// Class definition for hashtag persistence.
//
// Authors: Ricardo Rios, Rodrigo Mello and Paulo Pagliosa
// Last revision: 20/04/2017

package com.tsviz.twitterdata;

import java.sql.*;
import com.tsviz.persistence.*;


/////////////////////////////////////////////////////////////////////
//
// HashtagPersistence: hashtag persistence class
// ==================
public class HashtagPersistence
  extends TwitterDataPersistence<Hashtag>
{
  @Override
  public void insert(Database db, Hashtag h)
    throws PersistenceException
  {
    try
    {
      PreparedStatement s = prepareInsert(db, Query.INSERT.sql, true);

      s.setString(1, h.getHashtag());
      s.executeUpdate();
      try (ResultSet keys = s.getGeneratedKeys())
      {
        if (!keys.next())
          throw new PersistenceException("No key for hashtag id");
        h.setHashtag_id(keys.getLong("hashtag_id"));
      }
    }
    catch (SQLException e)
    {
      throw new PersistenceException(e);
    }
  }

  @Override
  public HashtagIterator findAll(Database db)
    throws PersistenceException
  {
    return new HashtagIterator(db, Query.FIND_ALL.sql);
  }

  public Hashtag findById(Database db, long hashtag_id)
    throws PersistenceException
  {
    return find(db, Query.FIND_BY_ID.sql, hashtag_id);
  }

  public Hashtag findByHashtag(Database db, String hashtag)
    throws PersistenceException
  {
    return find(db, Query.FIND_BY_HASHTAG.sql, hashtag);
  }

  private Hashtag find(Database db, String sql, Object... args)
    throws PersistenceException
  {
    return getInstance(new HashtagIterator(db, sql, args));
  }

  private static enum Query
  {
    INSERT("insert into hashtag (hashtag) values (?)"),
    FIND_ALL("select hashtag_id,hashtag from hashtag"),
    FIND_BY_ID("select hashtag_id,hashtag from hashtag where hashtag_id=%d"),
    FIND_BY_HASHTAG("select hashtag_id,hashtag from hashtag " +
      "where hashtag='%s'");

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
