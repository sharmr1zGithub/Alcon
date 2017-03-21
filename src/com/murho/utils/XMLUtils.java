package com.murho.utils;

public class XMLUtils {

  private static final String AND = "&";
  private static final String R_AND = "&amp;";

  public XMLUtils() {
  }

  /**
   *
   * @webmethod
   */
  public String getXMLMessage(int status, String message) {
    String xmlStr;
    xmlStr = "<?xml version='1.0' encoding='UTF-8'?>";
    xmlStr += "<message>";
    xmlStr += "<status>" + String.valueOf(status).trim() + "</status>";
    xmlStr += "<description>" + message.trim() + "</description>";
    xmlStr += "</message>";
    return xmlStr.trim();
  }

  /**
   *
   * @webmethod
   */
  public String getXMLNode(String nodeName, String nodeValue) {
    String xmlStr = "";
    try {
      xmlStr = "<" + nodeName.trim() + ">" + nodeValue.trim() + "</" +
          nodeName.trim() + ">";
    }
    catch (Exception e) {
      xmlStr = "<" + nodeName.trim() + ">" + "</" + nodeName.trim() + ">";
    }
    return xmlStr.trim();
  }

  /**
   *
   * @webmethod
   */
  public String getXMLAttrib(String nodeName, String attribName,
                             String attribValue) {
    String xmlStr = "";
    try {
      xmlStr = "<" + nodeName.trim() + "  " + attribName.trim() + "='" +
          attribValue.trim() + "'>"; ;
    }
    catch (Exception e) {}
    return xmlStr.trim();
  }

  /**
   *
   * @webmethod
   */
  public String getXMLHeader() {
    return "<?xml version='1.0' encoding='utf-8' ?>";
  }

  /**
   *
   * @webmethod
   */
  public String getStartNode(String nodeName) {
    return "<" + nodeName.trim() + ">";
  }

  /**
   *
   * @webmethod
   */
  public String getEndNode(String nodeName) {
    return "</" + nodeName.trim() + ">";
  }

  /**
   *
   * @webmethod
   */
  public String getValidXMLString(String str) {
    String retStr = "";

    if (!str.equalsIgnoreCase("")) {
      retStr = str.replaceAll(AND, R_AND);
    }
    return retStr;
  }
}
