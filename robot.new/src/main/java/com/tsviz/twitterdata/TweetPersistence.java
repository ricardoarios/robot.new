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
// OVERVIEW: TweetPersistence.java
// ========
// Class definition for tweet persistence.
//
// Authors: Ricardo Rios, Rodrigo Mello, and Paulo Pagliosa
// Last revision: 20/04/2017

package com.tsviz.twitterdata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import com.tsviz.persistence.*;


/////////////////////////////////////////////////////////////////////
//
// TweetPersistence: tweet persistence class
// ================
public class TweetPersistence
  extends TwitterDataPersistence<Tweet>
{
  @Override
  public void insert(Database db, Tweet t)
    throws PersistenceException
  {
    boolean commit = false;

    try
    {
      //db.getConnection().setAutoCommit(false);

      PreparedStatement s = prepareInsert(db, Query.INSERT.sql, false);

      s.setLong(1, t.getTweet_id());
      s.setLong(2, t.getUser_id());
      s.setString(3, t.getText());
      s.setString(4, t.getSource());
      s.setString(5, t.getLang());
      s.setDouble(6, t.getCoordinates_longitude());
      s.setDouble(7, t.getCoordinates_latitude());
      s.setTimestamp(8, new Timestamp(t.getCreated_at()));
      s.executeUpdate();
      thp.insert(db, t, t.getHashtags());
      //db.getConnection().commit();
      //db.getConnection().setAutoCommit(commit = true);
    }
    catch (SQLException e)
    {
      throw new PersistenceException(e);
    }
    /*finally
    {
      if (!commit)
        rollback(db);
    }*/
  }

  @Override
  public TweetIterator findAll(Database db)
    throws PersistenceException
  {
    return new TweetIterator(db, Query.FIND_ALL.sql);
  }

  public TweetIterator findById(Database db, long id)
    throws PersistenceException
  {
    return new TweetIterator(db, Query.FIND_BY_ID.sql, id);
  }

  public TweetIterator findByIdLast(Database db, long id, long lastKTweets)
    throws PersistenceException
  {
    return new TweetIterator(db, Query.FIND_BY_ID_LAST.sql, id, lastKTweets);
  }

  public TweetIterator findById(Database db, long id, Date from)
    throws PersistenceException
  {
    return new TweetIterator(db,
      Query.FIND_BY_ID_FROM.sql,
      id,
      from.toString());
  }

  public int findByIdCounter(Database db, long id, Date from)
    throws PersistenceException
  {
    try
    {
      ResultSet rs = db.query(Query.FIND_BY_ID_FROM_COUNTER.sql,
        id,
        from.toString());

      if (rs.next()) 
        return rs.getInt("counter");
      return 0;
    }
    catch (SQLException e)
    {
      throw new PersistenceException(e);
    }
  }

  private static void rollback(Database db)
  {
    try
    {
      db.getConnection().rollback();
      db.getConnection().setAutoCommit(true);
    }
    catch (SQLException e)
    {
      // do nothing
    }
  }

  private final TweetHashtagPersistence thp = new TweetHashtagPersistence();

  private static enum Query
  {
    INSERT("insert into tweet (tweet_id,id_user,text,source,lang," +
      "coordinates_longitude,coordinates_latitude,created_at) "+
      "values (?,?,?,?,?,?,?,?)"),
    FIND_ALL("select tweet_id,id_user,text,lang,created_at," +
      "coordinates_longitude,coordinates_latitude from tweet order by " +
      "created_at asc"),
    FIND_BY_ID("select tweet.tweet_id,id_user,text,lang,created_at," +
      "coordinates_longitude,coordinates_latitude from tweet,tweet_hashtag " +
      "where tweet.tweet_id=tweet_hashtag.tweet_id and " +
      "tweet_hashtag.hashtag_id=%d order by created_at asc"),
    FIND_BY_ID_LAST("select tweet.tweet_id,id_user,text,lang,created_at," +
      "coordinates_longitude,coordinates_latitude from tweet,tweet_hashtag " +
      "where tweet.tweet_id=tweet_hashtag.tweet_id and " +
      "tweet_hashtag.hashtag_id=%d order by created_at desc limit %d"),
    FIND_BY_ID_FROM("select tweet.tweet_id,id_user,text,lang,created_at," +
      "coordinates_longitude,coordinates_latitude from tweet,tweet_hashtag " +
      "where tweet.tweet_id=tweet_hashtag.tweet_id and " +
      "tweet_hashtag.hashtag_id=%d and tweet.created_at >= '%s' " +
      "order by created_at asc"),
    FIND_BY_ID_FROM_COUNTER("select count(*) as counter from tweet," +
      "tweet_hashtag where tweet.tweet_id=tweet_hashtag.tweet_id and " +
      "tweet_hashtag.hashtag_id=%d and tweet.created_at >= '%s' ");

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

} // TweetPersistence


/////////////////////////////////////////////////////////////////////
//
// TweetHashtagPersistence: tweet/hastag persistence class
// =======================
class TweetHashtagPersistence
  extends PreparedInsert
{
  void insert(Database db, Tweet t, Collection<Hashtag> hashtags)
    throws PersistenceException, SQLException
  {
    if (hashtags == null || hashtags.isEmpty())
      return;

    PreparedStatement s = prepareInsert(db, Query.INSERT.sql, false);

    s.setLong(1, t.getTweet_id());
    for (Hashtag h : hashtags)
    {
      s.setLong(2, h.getHashtag_id());
      s.executeUpdate();
    }
  }

  private static enum Query
  {
    INSERT("insert into tweet_hashtag (tweet_id,hashtag_id) values(?,?)");

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

} // TweetHashtagPersistence
