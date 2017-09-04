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
// OVERVIEW: TwitterMonitor.java
// ========
// Class definition for twitter monitor.
//
// Authors: Ricardo Rios, Rodrigo Mello, and Paulo Pagliosa
// Last revision: 21/04/2017

package com.tsviz.robot;

import java.util.*;
import org.apache.log4j.Logger;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import com.tsviz.notification.Email;
import com.tsviz.persistence.*;
import com.tsviz.twitterdata.*;
import javax.mail.MessagingException;


/////////////////////////////////////////////////////////////////////
//
// TwitterMonitor: twitter monitor class
// ==============
public class TwitterMonitor
  implements Runnable
{
  private final ConfigurationBuilder cb;
  private final TwitterStream twitterStream;
  private final FilterQuery fq;
  private final String emailTo;
  private StatusListener listener;
  private Database database;
  private ArrayList<Hashtag> monitoredHashtags;
  private final HashtagPersistence htp = new HashtagPersistence();
  private final TwitterUserPersistence utp = new TwitterUserPersistence();
  private final TweetPersistence tp = new TweetPersistence();

  static final String CLASS_NAME = TwitterMonitor.class.getName();
  static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

  public TwitterMonitor(Properties props)
  {
    cb = new ConfigurationBuilder();
    cb.setDebugEnabled(Boolean.getBoolean(props.getProperty("DebugEnabled")));
    cb.setOAuthConsumerKey(props.getProperty("OAuthConsumerKey"));
    cb.setOAuthConsumerSecret(props.getProperty("OAuthConsumerSecret"));
    cb.setOAuthAccessToken(props.getProperty("OAuthAccessToken"));
    cb.setOAuthAccessTokenSecret(props.getProperty("OAuthAccessTokenSecret"));
    twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
    configMonitoredHashtag(props.getProperty("Hashtags"));
    fq = new FilterQuery();
    emailTo = props.getProperty("EmailTo");
  }

  public ConfigurationBuilder getConfigurationBuilder()
  {
    return cb;
  }

  public TwitterStream getTwitterStream()
  {
    return twitterStream;
  }

  public FilterQuery getFilterQuery()
  {
    return fq;
  }

  public StatusListener getListener()
  {
    return listener;
  }

  public Database getDatabase()
  {
    return database;
  }

  public void setDatabase(Database database)
  {
    this.database = database;
  }

  public ArrayList<Hashtag> getMonitoredHashtags()
  {
    return monitoredHashtags;
  }

  public void setMonitoredHashtags(ArrayList<Hashtag> monitoredHashtags)
  {
    this.monitoredHashtags = monitoredHashtags;
    // TODO: persistenceHashtags(); ???
  }

  public void setMonitoredHashtags(MonitoredHashTags hashtags)
  {
    monitoredHashtags = hashtags.getHashtags();
    // TODO: persistenceHashtags(); ???
  }

  public boolean addHashtag(String hashtag)
  {
    Hashtag mht = new Hashtag(-1, hashtag);

    if (!monitoredHashtags.contains(mht))
    {
      Hashtag pht = htp.findByHashtag(database, hashtag);

      if (pht != null)
        htp.insert(database, mht);
      else
        mht = pht;
      monitoredHashtags.add(mht);
      fq.track(toStringArray(monitoredHashtags));
      twitterStream.filter(fq);
      LOGGER.debug("Hashtag has been monitored: " + hashtag);
      return true;
    }
    LOGGER.debug("Hashtag has ***already*** been monitored: " + hashtag);
    return false;
  }

  public boolean deleteHashtag(String hashtag)
  {
    Hashtag ht = htp.findByHashtag(database, hashtag);

    if (ht == null)
      return false;
    monitoredHashtags.remove(ht);
    fq.track(toStringArray(monitoredHashtags));
    twitterStream.filter(fq);
    return true;
  }

  public void startListener()
    throws TwitterException
  {
    listener = new StatusListener()
    {
      final Email email = new Email("tsviz.com@gmail.com", "Ts41z@tw1tt3r");
      final String to = emailTo;

      @Override
      public void onException(Exception arg0)
      {
        try
        {
          email.sendMessage("[problem] Twitter Robot", arg0.getMessage(), to);
          LOGGER.error("An email containing the thrown exception " +
            "has been sent to " + to + ".\n");
        }
        catch (MessagingException e)
        {
          LOGGER.error("Problem by sending the notification by email: " + to);
        }
      }

      @Override
      public void onDeletionNotice(StatusDeletionNotice arg0)
      {
        // TODO Auto-generated method stub
      }

      @Override
      public void onScrubGeo(long arg0, long arg1)
      {
        // TODO Auto-generated method stub
      }

      @Override
      public void onStatus(Status status)
      {
        LOGGER.debug(CLASS_NAME + ":\n" + status.getId() + "\n\n");

        Tweet tweet = new Tweet();
        Set<Hashtag> hashtagRefs = new HashSet<>();

        tweet.setTweet(status);
        LOGGER.debug("Selecting tweet hashtags...");
        for (Hashtag ht : monitoredHashtags)
          if (tweet.getText().toUpperCase().contains(ht.getHashtag().toUpperCase()))
            hashtagRefs.add(ht);
        // we dont't insert a tweet without monitored hashtag refs...
        if (hashtagRefs.isEmpty())
          return;
        tweet.setHashtags(hashtagRefs);

        TwitterUser user = new TwitterUser();

        user.setUser(status);
        LOGGER.debug("Checking user " + user.getId());
        try
        {
          utp.insert(database, user);
          LOGGER.debug(user.getName() + " is a new user");
        }
        catch (PersistenceException e)
        {
          // do nothing
        }
        LOGGER.debug("Inserting TWEET " + tweet.getText());
        try
        {
          tp.insert(database, tweet);
          LOGGER.debug("New TWEET " + tweet.getText());
        }
        catch (PersistenceException e)
        {
          // do nothing
        }
      }

      @Override
      public void onTrackLimitationNotice(int arg0)
      {
        // TODO Auto-generated method stub
      }

      @Override
      public void onStallWarning(StallWarning arg0)
      {
        // TODO Auto-generated method stub
      }
    };
  }

  public void run(MonitoredHashTags hashtags)
  {
    try
    {
      if (database == null || !database.isOpen())
      {
        LOGGER.error("Error: TsViz database is not open\n");
        return;
      }
      if (hashtags != null)
        setMonitoredHashtags(hashtags);

      String[] arrayHashtags = persistenceHashtags();

      this.startListener();
      fq.track(arrayHashtags);
      twitterStream.addListener(listener);
      twitterStream.filter(fq);
    }
    catch (TwitterException | PersistenceException e)
    {
      LOGGER.error("Error:" + CLASS_NAME + ":\n" + e.getMessage() + "\n\n");
    }
  }

  @Override
  public void run()
  {
    this.run(null);
  }

  private void configMonitoredHashtag(String hashtags)
  {
    StringTokenizer strtok = new StringTokenizer(hashtags, ";");

    if (monitoredHashtags == null)
      monitoredHashtags = new ArrayList<>();
    while (strtok.hasMoreTokens())
      monitoredHashtags.add(new Hashtag(-1, strtok.nextToken()));
  }

  private static String[] toStringArray(ArrayList<Hashtag> hs)
  {
    int n = hs.size();
    String[] arrayHashtags = new String[n];

    for (int i = 0; i < n; i++)
      arrayHashtags[i] = hs.get(i).getHashtag();
    return arrayHashtags;
  }

  private String[] persistenceHashtags()
    throws PersistenceException
  {
    int n = monitoredHashtags.size();
    String[] arrayHashtags = new String[n];

    // insert all monitored hashtag, if they werent previously monitored
    for (int i = 0; i < n; i++)
    {
      Hashtag mht = monitoredHashtags.get(i);

      arrayHashtags[i] = mht.getHashtag();

      Hashtag pht = htp.findByHashtag(database, mht.getHashtag());

      if (pht == null)
        htp.insert(database, mht);
      else
        mht.setHashtag_id(pht.getHashtag_id());
    }
    return arrayHashtags;
  }

} // TwitterMonitor
