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
// OVERVIEW: App.java
// ========
// Class definition for robot app.
//
// Authors: Ricardo Rios and Paulo Pagliosa
// Last revision: 19/04/2017

package com.tsviz.robot;

import java.io.*;
import java.util.Properties;
import javax.sql.DataSource;
import org.postgresql.ds.PGPoolingDataSource;
import org.apache.log4j.Logger;
import com.tsviz.network.ServerRobot;
import com.tsviz.persistence.*;


/////////////////////////////////////////////////////////////////////
//
// App: robot app class
// ===
public class App
{
  private static PGPoolingDataSource source;
  private static final Logger LOGGER = Logger.getLogger(App.class.getName());
  private static TwitterMonitor tm;

  private static DataSource getTsVizDataSource(Properties props)
  {
    if (source == null)
    {
      source = new PGPoolingDataSource();
      source.setServerName(props.getProperty("ServerName"));
      source.setDatabaseName(props.getProperty("DatabaseName"));
      source.setUser(props.getProperty("User"));
      source.setPassword(props.getProperty("Password"));
    }
    return source;
  }

  public static void main(String[] args)
  {
    LOGGER.info("Starting robot...");
    if (args.length == 0)
    {
      LOGGER.error("Error: Missing configuration file");
      return;
    }

    String configFilename = args[0];
    Properties props = new Properties();

    try (FileInputStream in = new FileInputStream(configFilename))
    {
      props.load(in);
      LOGGER.info("Read configuration file: " + configFilename);
    }
    catch (IOException e)
    {
      LOGGER.error("Error: " + e.getMessage());
    }

    Database database;

    try
    {
      database = new Database(getTsVizDataSource(props));
      database.open();
      LOGGER.info("Connected to TsViz database");
    }
    catch (PersistenceException e)
    {
      LOGGER.error("Error: Unable to connect to TsViz database");
      return;
    }

    ServerRobot sr = new ServerRobot();

    tm = new TwitterMonitor(props);
    tm.setDatabase(database);

    new Thread(tm).start();

    sr.setTm(tm);
    try
    {
      sr.startServer();
      sr.endConn();
    }
    catch (IOException | ClassNotFoundException e)
    {
      LOGGER.error(e.getMessage());
    }
  }

} // App
