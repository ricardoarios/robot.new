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
// OVERVIEW: Console.java
// ========
// Class definition for simple console.
//
// Authors: Paulo Pagliosa
// Last revision: 18/04/2017

package com.tsviz.robot.test;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;


/////////////////////////////////////////////////////////////////////
//
// Console: simple console class
// =======
public class Console
{
  private final static InputStreamReader keyboard;
  private final static BufferedReader in;
  private final static PrintStream out;

  static
  {
    keyboard = new InputStreamReader(System.in);
    in = new BufferedReader(keyboard);
    out = System.out;
  }

  private static void prompt(String message)
  {
    out.print(message);
  }

  public static void error(String message)
  {
    out.println("**Error: " + message + ". Try again");
  }

  private static void inputError()
  {
    error("input");
  }

  final public static void println(String s)
  {
    out.println(s);
  }

  final public static void println(int i)
  {
    out.println(i);
  }

  final public static void println(Object object)
  {
    out.println(object);
  }

  final public static void printf(String fmt, Object... args)
  {
    out.printf(fmt, args);
  }

  public static String readString(String message)
  {
    for (;;)
    {
      prompt(message);
      try
      {
        return in.readLine();
      }
      catch (IOException e)
      {
        inputError();
      }
    }
  }

  public static int readChar(String message)
  {
    for (;;)
    {
      prompt(message);
      try
      {
        return keyboard.read();
      }
      catch (IOException e)
      {
        inputError();
      }
    }
  }

  public static int readInt(String message)
  {
    for (;;)
      try
      {
        return Integer.parseInt(readString(message));
      }
      catch (NumberFormatException e)
      {
        error("integer expected");
      }
  }

  public static long readLong(String message)
  {
    for (;;)
      try
      {
        return Long.parseLong(readString(message));
      }
      catch (NumberFormatException e)
      {
        error("long expected");
      }
  }

  public static float readFloat(String message)
  {
    for (;;)
      try
      {
        return Float.parseFloat(readString(message));
      }
      catch (NumberFormatException e)
      {
        error("float expected");
      }
  }

  public static char readOption(String message, String options)
  {
    for (int last = options.length() - 1;;)
    {
      prompt(message);
      try
      {
        String input = in.readLine();
        int index = input.equals("") ? last : options.indexOf(input.charAt(0));

        if (index != -1)
          return options.charAt(index);
        error("invalid option");
      }
      catch (IOException e)
      {
        inputError();
      }
    }
  }

  public static Date readDate(String message)
  {
    for (DateFormat df = DateFormat.getDateInstance();;)
      try
      {
        return df.parse(readString(message));
      }
      catch (ParseException e)
      {
        error("invalide date format");
      }
  }

} // Console
