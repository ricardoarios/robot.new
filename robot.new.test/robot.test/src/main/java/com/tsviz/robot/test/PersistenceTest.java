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
// OVERVIEW: PersistenceTest.java
// ========
// Class definition for robot presistence test.
//
// Authors: Paulo Pagliosa
// Last revision: 18/04/2017

package com.tsviz.robot.test;

import java.util.*;
import javax.sql.DataSource;
import org.postgresql.ds.PGPoolingDataSource;
import com.tsviz.persistence.*;
import com.tsviz.twitterdata.*;


/////////////////////////////////////////////////////////////////////
//
// PersistenceTest: robot persistence test class
// ===============
public class PersistenceTest
{
  public static void main(String[] args)
  {
    try
    {
      openDatabase();
      hashtagTest();
      twitterUserTest();
      tweetTest();
      closeDatabase();
    }
    catch (PersistenceException e)
    {
      System.out.println(e.getMessage());
    }
  }

  private static void tweetTest()
    throws PersistenceException
  {
    Console.println("Inserting 9 tweets...");
    insertTweet(1, 1, "tweet 1, user 1", "hashtag_1", "hashtag_2");
    insertTweet(2, 2, "tweet 2, user 2", "hashtag_2", "hashtag_3");
    insertTweet(3, 3, "tweet 3, user 3", "hashtag_3", "hashtag_1");
    insertTweet(4, 1, "tweet 4, user 1", "hashtag_1", "hashtag_2");
    insertTweet(5, 2, "tweet 5, user 3", "hashtag_2", "hashtag_3");
    insertTweet(6, 3, "tweet 6, user 3", "hashtag_3", "hashtag_1");
    insertTweet(7, 1, "tweet 7, user 1", "hashtag_1", "hashtag_2");
    insertTweet(8, 2, "tweet 8, user 2", "hashtag_2", "hashtag_3");
    insertTweet(9, 3, "tweet 9, user 3", "hashtag_3", "hashtag_1");
    Console.println("Finding all tweets...");
    printTweets(tp.findAll(database));
}

  private static void printTweets(TweetIterator ti)
  {
    if (!ti.hasNext())
    {
      Console.println("*empty*");
      return;
    }
    do
      printTweet(ti.next());
    while (ti.hasNext());
  }

  private static void printTweet(Tweet t)
  {
    Console.printf("%5d %s\n", t.getTweet_id(), t.getText());
  }

  private static void insertTweet(long tweet_id,
   long user_id,
   String text,
   String... hashtags)
    throws PersistenceException
  {
    Tweet t = new Tweet();

    t.setTweet_id(tweet_id);
    t.setUser_id(user_id);
    t.setText(text);
    t.setCreated_at(new Date().getTime());
    t.setSource("Windows");
    t.setLang("Portuguese");

    Set<Hashtag> hs = new TreeSet<>();

    for (String s : hashtags)
    {
      Hashtag h = hp.findByHashtag(database, s);

      if (h != null)
        hs.add(h);
    }
    t.setHashtags(hs);
    try
    {
      tp.insert(database, t);
    }
    catch (PersistenceException e)
    {
      // do nothing
    }
    printTweet(t);
  }

  private static void twitterUserTest()
    throws PersistenceException
  {
    Console.println("Inserting 5 users...");
    insertUser(1, "user_1");
    insertUser(2, "user_2");
    insertUser(3, "user_3");
    insertUser(4, "user_4");
    insertUser(5, "user_5");
    Console.println("Finding all users...");
    printUsers(up.findAll(database));
  }

  private static void printUsers(TwitterUserIterator ui)
  {
    if (!ui.hasNext())
    {
      Console.println("*empty*");
      return;
    }
    do
      printUser(ui.next());
    while (ui.hasNext());
  }

  private static void printUser(TwitterUser u)
  {
    Console.printf("%5d %s\n", u.getId(), u.getName());
  }

  private static void insertUser(long id, String name)
    throws PersistenceException
  {
    TwitterUser u = up.findById(database, id);

    if (u == null)
    {
      u = new TwitterUser();

      u.setId(id);
      u.setName(name);
      u.setScreen_name(name);
      u.setLocation("Campo Grande");
      u.setCreated_at(new Date().getTime());
      u.setUtc_offset(0);
      u.setTime_zone("GMT-4");
      u.setLang("Portuguese");
      up.insert(database, u);
    }
    printUser(u);
  }

  private static void hashtagTest()
    throws PersistenceException
  {
    Console.println("Inserting 3 hashtags...");
    insertHashtag("hashtag_1");
    insertHashtag("hashtag_2");
    insertHashtag("hashtag_3");
    Console.println("Finding all hashtags...");
    printHashtags(hp.findAll(database));
  }

  private static void printHashtags(HashtagIterator hi)
  {
    if (!hi.hasNext())
    {
      Console.println("*empty*");
      return;
    }
    do
      printHashtag(hi.next());
    while (hi.hasNext());
  }

  private static void printHashtag(Hashtag h)
  {
    Console.printf("%5d %s\n", h.getHashtag_id(), h.getHashtag());
  }

  private static void insertHashtag(String hashtag)
    throws PersistenceException
  {
    Hashtag h = hp.findByHashtag(database, hashtag);

    if (h == null)
    {
      h = new Hashtag(-1, hashtag);
      hp.insert(database, h);
    }
    printHashtag(h);
  }

  private static void openDatabase()
    throws PersistenceException
  {
    database = new Database(getTsVizDataSource());
    database.open();
    Console.println("Database is open");
    hp = new HashtagPersistence();
    up = new TwitterUserPersistence();
    tp = new TweetPersistence();
  }

  private static void closeDatabase()
    throws PersistenceException
  {
    database.close();
    Console.println("Database is closed");
  }

  private static DataSource getTsVizDataSource()
  {
    if (source == null)
    {
      source = new PGPoolingDataSource();
      source.setServerName("localhost");
      source.setDatabaseName("twitter");
      source.setUser("postgres");
      source.setPassword("tsviz");
    }
    return source;
  }

  private static PGPoolingDataSource source;
  private static Database database;
  private static HashtagPersistence hp;
  private static TwitterUserPersistence up;
  private static TweetPersistence tp;

} // PersistenceTest
