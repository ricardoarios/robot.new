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
// OVERVIEW: ServerRobot.java
// ========
// Class definition for server robot.
//
// Authors: Ricardo Rios
// Last revision: 15/04/2017

package com.tsviz.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.tsviz.robot.TwitterMonitor;
import com.tsviz.twitterdata.Hashtag;


/////////////////////////////////////////////////////////////////////
//
// ServerRobot: server robot class
// ===========
public class ServerRobot {

  private int portNumber;
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private ObjectInputStream inStream;
  private ObjectOutputStream outToClient;
  private TwitterMonitor tm;

  public ServerRobot() {
    portNumber = 7878;
  }

  public int getPortNumber() {
    return portNumber;
  }

  public void setPortNumber(int portNumber) {
    this.portNumber = portNumber;
  }

  public TwitterMonitor getTm() {
    return tm;
  }

  public void setTm(TwitterMonitor tm) {
    this.tm = tm;
  }

  public void endConn()
          throws IOException {
    this.serverSocket.close();
  }

  public void checkMessageType(Message m, ObjectOutputStream outToClient)
          throws IOException {
    int i;
    String resp[];
    switch (m.getType()) {
      case MessageType.ADD:
        i = 0;
        resp = new String[m.getHashtags().length];
        for (String nht : m.getHashtags()) {
          tm.addHashtag(nht);
          resp[i++] = "Hashtag added: " + nht;
        }
        outToClient.writeObject(new Message(MessageType.ACK, resp));
        break;
      case MessageType.DELETE:
        i = 0;
        resp = new String[m.getHashtags().length];
        for (String sht : m.getHashtags()) {
          tm.deleteHashtag(sht);
          resp[i++] = "Hashtag deleted: " + sht;
        }
        outToClient.writeObject(new Message(MessageType.ACK, resp));
        break;
      case MessageType.LIST:
        //list
        String[] arrayHashtags = new String[this.tm.getMonitoredHashtags().size()];
        i = 0;
        for (Hashtag ht : this.tm.getMonitoredHashtags()) {
          arrayHashtags[i++] = ht.getHashtag();
        }
        outToClient.writeObject(new Message(MessageType.ACK, arrayHashtags));
        break;
      case MessageType.ERROR:
      default:
        resp = new String[1];
        resp[0] = "Message unknown";
        outToClient.writeObject(new Message(MessageType.ERROR, resp));
        break;
    }
  }

  public void startServer()
          throws IOException, ClassNotFoundException {
    serverSocket = new ServerSocket(portNumber);
    while (true) {
      System.out.println("Server started. Waiting a client...");
      clientSocket = serverSocket.accept();
      inStream = new ObjectInputStream(clientSocket.getInputStream());
      outToClient = new ObjectOutputStream(clientSocket.getOutputStream());

      Message md = (Message) inStream.readObject();
      this.checkMessageType(md, outToClient);
      clientSocket.close();
    }
  }

} // ServerRobot
