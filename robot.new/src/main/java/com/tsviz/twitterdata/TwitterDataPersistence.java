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
// OVERVIEW: TwitterDataPersistence.java
// ========
// Class definition for generic twitter data persistence.
//
// Authors: Paulo Pagliosa
// Last revision: 13/04/2017

package com.tsviz.twitterdata;

import java.io.IOException;
import java.sql.*;
import com.tsviz.persistence.*;


/////////////////////////////////////////////////////////////////////
//
// TwitterDataPersistence: generic twitter data persistence class
// ======================
public abstract class TwitterDataPersistence<P extends Persistable>
  extends PreparedInsert implements Persistence<P>
{
  @Override
  public void update(Database db, P p)
    throws PersistenceException
  {
    throw PersistenceException.unsupported();
  }

  @Override
  public void delete(Database db, P p)
    throws PersistenceException
  {
    throw PersistenceException.unsupported();
  }

  @Override
  public AbstractIterator<P> find(Database db, int id)
    throws PersistenceException
  {
    throw PersistenceException.unsupported();
  }

  protected final P getInstance(AbstractIterator<P> i)
    throws PersistenceException
  {
    try
    {
      P p = i.hasNext() ? i.next() : null;

      i.close();
      return p;
    }
    catch (IOException e)
    {
      throw new PersistenceException(e);
    }
  }

} // TwitterDataPersistence


/////////////////////////////////////////////////////////////////////
//
// PreparedInsert: prepared insert class
// ==============
abstract class PreparedInsert
{
  protected PreparedStatement prepareInsert(Database db, String sql, boolean k)
    throws PersistenceException
  {
    try
    {
      Connection c = db.getConnection();

      if (c != cachedConnection)
      {
        statement = c.prepareStatement(sql,
          k ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        cachedConnection = c;
      }
      return statement;
    }
    catch (SQLException e)
    {
      throw new PersistenceException(e);
    }
  }

  private Connection cachedConnection;
  private PreparedStatement statement;

} // PreparedInsert