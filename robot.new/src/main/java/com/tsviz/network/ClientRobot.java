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
// OVERVIEW: ClientRobot.java
// ========
// Class definition for client robot.
//
// Authors: Ricardo Rios
// Last revision: 15/04/2017

package com.tsviz.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import org.apache.log4j.Logger;


/////////////////////////////////////////////////////////////////////
//
// ClientRobot: client robot class
// ===========
public class ClientRobot {

  private Socket socket;
  private ObjectOutputStream outputStream;
  private boolean isConnected;
  private String url;
  private final int port;
  private ObjectInputStream inStream;

  static final Logger LOGGER = Logger.getLogger(ClientRobot.class.getName());

  public ClientRobot() {
    socket = null;
    outputStream = null;
    isConnected = false;
    url = "localhost";
    port = 7878;
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void start(int type, String[] message) {
    while (!isConnected) {
      try {
        socket = new Socket(this.url, this.port);
        System.out.println("Connected");
        isConnected = true;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());

        Message dataTest = new Message();

        dataTest.setType(type);
        dataTest.setHashtags(message);
        outputStream.writeObject(dataTest);
        LOGGER.info((Message) inStream.readObject());
        socket.close();
      } catch (SocketException se) {
        LOGGER.error(se.getMessage());
      } catch (IOException | ClassNotFoundException oe) {
        LOGGER.error(oe.getMessage());
      }
    }
  }

  public static void main(String[] args) {
    ClientRobot cm = new ClientRobot();

    switch (Integer.parseInt(args[0])) {
      case MessageType.ADD:
        String[] add = Arrays.copyOfRange(args, 1, args.length);
        cm.start(MessageType.ADD, add);
        break;
      case MessageType.DELETE:
        String[] del = Arrays.copyOfRange(args, 1, args.length);
        cm.start(MessageType.DELETE, del);
        break;
      case MessageType.LIST:
        cm.start(MessageType.LIST, null);
        break;
      default:
        LOGGER.error("Unknown message type. See MessageType interface for more information");
        break;
    }
  }

} // ClientRobot
