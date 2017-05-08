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
// OVERVIEW: Hashtag.java
// ========
// Class definition for hashtag.
//
// Authors: Ricardo Rios, Rodrigo Mello, and Paulo Pagliosa
// Last revision: 15/04/2017

package com.tsviz.twitterdata;

import com.tsviz.persistence.*;
import java.util.Objects;


/////////////////////////////////////////////////////////////////////
//
// Hashtag: hashtag class
// =======
public class Hashtag
  implements Persistable, Comparable<Hashtag>
{
  private long hashtag_id;
  private String hashtag;

  public Hashtag()
  {
    // do nothing
  }

  public Hashtag(long hashtag_id, String hashtag)
  {
    this.hashtag_id = hashtag_id;
    this.hashtag = hashtag;
  }

  public long getHashtag_id()
  {
    return hashtag_id;
  }

  public void setHashtag_id(long hashtag_id)
  {
    this.hashtag_id = hashtag_id;
  }

  public String getHashtag()
  {
    return hashtag;
  }

  public void setHashtag(String hashtag)
  {
    this.hashtag = hashtag;
  }

  @Override
  public int compareTo(Hashtag h)
  {
    return hashtag.compareTo(h.hashtag);
  }

  @Override
  public String toString()
  {
    return "Hashtag [id]: " + hashtag + "[" + hashtag_id + "]\n";
  }

  @Override
  public boolean equals(Object o)
  {
    return this == o ||
      o != null && o instanceof Hashtag &&
      hashtag.equals(((Hashtag)o).hashtag);
  }

  @Override
  public int hashCode()
  {
    int hash = 5;

    hash = 97 * hash + Objects.hashCode(hashtag);
    return hash;
  }

} // Hashtag
