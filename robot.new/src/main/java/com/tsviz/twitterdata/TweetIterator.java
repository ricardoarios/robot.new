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
// OVERVIEW: TweetIterator.java
// ========
// Class definition for tweet iterator.
//
// Authors: Ricardo Rios, Rodrigo Mello, and Paulo Pagliosa
// Last revision: 15/04/2017

package com.tsviz.twitterdata;

import java.sql.*;
import com.tsviz.persistence.*;


/////////////////////////////////////////////////////////////////////
//
// TweetIterator: tweet iterator class
// =============
public class TweetIterator
  extends AbstractIterator<Tweet>
{
  TweetIterator(Database db, String sql, Object... args)
    throws PersistenceException
  {
    super(db, sql, args);
  }

  @Override
  protected Tweet createObject(ResultSet rs)
    throws SQLException
  {
    Tweet t = new Tweet();

    t.setTweet_id(rs.getLong("tweet_id"));
    t.setUser_id(rs.getLong("id_user"));
    t.setText(rs.getString("text"));
    if (hasColumn(rs.getMetaData(), "source"))
      t.setSource(rs.getString("source"));
    t.setLang(rs.getString("lang"));
    t.setCreated_at(rs.getTimestamp("created_at").getTime());
    t.setCoordinates_longitude(rs.getDouble("coordinates_longitude"));
    t.setCoordinates_latitude(rs.getDouble("coordinates_latitude"));
    // TODO: hashtags
    return t;
  }

  private static boolean hasColumn(ResultSetMetaData m, String columnName)
    throws SQLException
  {
    for (int n = m.getColumnCount(), i = 1; i <= n; i++)
      if (m.getColumnName(i).equals(columnName))
        return true;
    return false;
  }

} // TweetIterator
