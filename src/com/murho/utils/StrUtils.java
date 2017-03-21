package com.murho.utils;

import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class StrUtils {
  String alphabet = "";
  String Quo = "\"";

  public StrUtils() {
  }

  //
  public static final void sop(String str) {
    System.out.println("Success::--->>>" + str);
  }

  //
  /*
  public static boolean isNumber(String number)
  {
    
    try {
     Long.parseLong(number);
    return true;
    } catch(Exception nfe) {
    return false;
  }
 }
 */
 public static boolean isNumber(String s) {
 int zeroCount=0;
	for (int j = 0;j < s.length();j++) {
 //   System.out.println(s.charAt(j));
		if (!Character.isDigit(s.charAt(j))) {
       if(Character.toString(s.charAt(j)).equalsIgnoreCase(".")){
       zeroCount++;
     //  System.out.println("has dot");
     //  System.out.println("zeroCount " + zeroCount);
       if(zeroCount>=2)return false;
       }
       else
       {
         return false;
       }
		}
  }
  return true;
}

public static void main(String[] arg)
{
  System.out.println("IsNumber : " + isNumber("02.1223"));
}



  public Vector parseString(String a, String delim) {
    Vector resString = new Vector();
    StringTokenizer tokens = new StringTokenizer(a, delim);
    int m = tokens.countTokens();
    while (tokens.hasMoreTokens()) {
      resString.addElement(tokens.nextToken());
    }
    return resString;
  }

  public static Vector parseStringGetAll(String inParseString, String inDelim) {
    Vector result = null;
    if (inParseString != null) {
      result = new Vector();
      StringTokenizer tokenizer = new StringTokenizer(inParseString, inDelim, true);
      String token = "";
      String last = inDelim;
      while (tokenizer.hasMoreTokens()) {
        token = tokenizer.nextToken();
        if (token.compareTo(inDelim) != 0) {
          result.add(token);
        }
        else {
          if (last.compareTo(inDelim) == 0) {
            result.add("");
          }
        }
        last = token;
      }
    }
    return result;
  }

  public static String fString(String inputStr) {
    String str = "";
    try {
      str = (inputStr.equalsIgnoreCase("") || (inputStr.equalsIgnoreCase("NULL"))) ?
          "" : inputStr.trim();
    }
    catch (Exception e) {}
    return str;
  }

  //farid to formate double Qotes 19-08-2005
  public String replaceQuoteBySlashQuote(String str) {
    if (fString(str).length() > 0) {
      // str = str.replace('#', '*');
      // str = str.replace('+', '$');

      if (str.indexOf("\"") != -1) {
        str = str.replaceAll("\"", "\\\"");

      }
    }
    return str;
  }

  public String getSQLDate(String inputStr, String del) {
    String str = "";
    try {
      String[] s = inputStr.split(del, 3);
      str = s[2].trim() + "-" + s[1].trim() + "-" + s[0].trim();
    }
    catch (Exception e) {}
    return str.trim();
  }

  public String getUserDate(String inputStr, String del, String show) {
    String str = "";
    try {
      String[] s = inputStr.trim().split(del, 3);
      str = s[2].trim() + show.trim() + s[1].trim() + show.trim() + s[0].trim();
    }
    catch (Exception e) {}
    return str.trim();
  }

  public String getSQLDate(String nd) {
    String fDate = "";
    try {
      //nd.trim();
      fDate = nd.substring(6, 10) + nd.substring(3, 5) + nd.substring(0, 2);
    }
    catch (Exception e) {}
    return fDate;
  }
  
 

  /**
   * @mthod : changeFloatValueWithSpcifiedDecimalPoint(float a, int decimal)
   * @decription : chage the given float value into specified decimal points.
   * @param a
   * @param decimal
   * @return
   */
  public float formatNumber(float a, int decimal) {
    float val = 0;
    try {
      int actualDec = (a + "").indexOf(".");
      if (actualDec == -1) {
        actualDec = 0;
      }
      else {
        actualDec = actualDec + decimal + 1;
      }
      if ( (a + "").length() > actualDec) {
        val = new Float( (a + "").substring(0, actualDec)).floatValue();
      }
      else {
        val = new Float(a).floatValue();
      }
      return val;
    }
    catch (Exception e) {
      return val;
    }

  }

  /**
   * @mthod : changeFloatValueWithSpcifiedDecimalPoint(float a, int decimal)
   * @decription : chage the given float value into specified decimal points.
   * @param a
   * @param decimal
   * @return
   */
  public BigDecimal formatNumber(BigDecimal a, int decimal, boolean hideZeros) {
    BigDecimal val = new BigDecimal(0);
    String valStr = "0";
    int total_length = 0;
    int decimal_position = 0;
    int value_after_decimal = 0;
    int value_before_decimal = 0;

    try {
      valStr = a + "";
      total_length = valStr.length();
      decimal_position = (valStr).lastIndexOf(".");
      if (decimal_position >= 1) {
        if (decimal_position > 1) {
          value_before_decimal = Integer.parseInt(valStr.substring(0,
              decimal_position - 1));
        }
        else {
          value_before_decimal = Integer.parseInt(valStr.substring(0,
              decimal_position));
        }
        if (decimal_position > 0) {
          value_after_decimal = Integer.parseInt(valStr.substring(
              decimal_position + 1, total_length));
        }
        if (value_after_decimal > 0 & value_after_decimal < 10) {
          valStr = valStr.substring(0, decimal_position) + "." +
              value_after_decimal;
        }
        else if (value_after_decimal > 10) {
          valStr = valStr.substring(0, decimal_position + 3);
        }
        else if (value_after_decimal == 0) {
          if (hideZeros) {
            valStr = valStr.substring(0, decimal_position);
          }
          else {
            valStr = valStr.substring(0, decimal_position) + ".00";
          }
        }
      }
      val = new BigDecimal(valStr);
      return val;
    }
    catch (Exception e) {
      e.printStackTrace();
      return val;

    }
  }

  public double formatNumber2Double(double a, int decimal) {
    double val = 0;
    try {
      int actualDec = (a + "").indexOf(".");
      if (actualDec == -1) {
        actualDec = 0;
      }
      else {
        actualDec = actualDec + decimal + 1;
      }
      if ( (a + "").length() > actualDec) {
        val = new Float( (a + "").substring(0, actualDec)).doubleValue();
      }
      else {
        val = new Float(a).doubleValue();
      }
      return val;
    }
    catch (Exception e) {
      return val;
    }

  }

  /*public String getHtmlFormatNumber(BigDecimal number,int dispDec){
    String qty_org = "";
    //float qty_dec_f   = formatNumber(number.floatValue(),3);
    double qty_dec_d   = formatNumber2Double(number.doubleValue(),3);
    String qty_dec    = new Float(qty_dec_d).toString();
    //float qty_nodec_f = formatNumber(number.floatValue(),0);
    double qty_nodec_d   = formatNumber2Double(number.doubleValue(),3);
    String qty_nodec  = new Float(qty_nodec_d).toString();
//    System.out.println("getHtmlFormatNumber : qty_nodec : "  + qty_nodec);
    if(Integer.parseInt(qty_dec.substring(qty_dec.lastIndexOf(".")+1,qty_dec.length())) > 0)
       qty_org = qty_dec;
    else {
       qty_org =  qty_nodec.substring(0,qty_nodec.lastIndexOf("."));
    }
    return qty_org;
     }*/
  public String getHtmlFormatNumber(BigDecimal number, int dispDec) {
    String qty_org = "";
    String qty_str = number.toString();
    NumberFormat formatter = new DecimalFormat("###0.000#");
    try {
      qty_str = formatter.format(number.doubleValue());
      //System.out.println("qty_str : " + qty_str);
      if (Integer.parseInt(qty_str.substring(qty_str.lastIndexOf(".") + 1,
                                             qty_str.length())) > 0) {
        qty_org = qty_str.substring(0, qty_str.lastIndexOf(".") + 4);
      }
      else {
        qty_org = qty_str.substring(0, qty_str.lastIndexOf("."));
      }
    }
    catch (Exception e) {
      System.out.println("Exception getHtmlFormatNumber :: " + e.toString());
    }
    return qty_org;
  }

  public String formatNumberString(String str) {
    String retStr = "";
    try {
      if (!str.equalsIgnoreCase("")) {
        BigDecimal strVal = new BigDecimal(str);
        retStr = formatNumber(strVal, 2, true).toString();
      }
    }
    catch (Exception e) {
      retStr = "0";
    }
    return retStr;
  }

  public String formatHTML(String field) {
    String retStr = "";
    try {
      if (field.trim().length() > 0) {
        retStr = field.replaceAll("\"", "'");
        retStr += "&nbsp";
      }
    }
    catch (Exception e) {}
    return retStr;
  }

  public String removeQuotes(String field) {
    String retStr = "";
    try {
      if (field.trim().length() > 0) {
        retStr = field.replaceAll("\"", "");
        retStr = retStr.replaceAll("'", "");
        retStr += "&nbsp";
      }
    }
    catch (Exception e) {}
    return retStr;
  }

  public String getStringSeparatedByCommasFromList(List l) {
    String strMail = "";
    try {
      for (int jj = 0; jj < l.size(); jj++) {
        String str = ( (String) l.get(jj)).trim();
        if (jj == l.size() - 1) {
          strMail += str;
        }
        else {
          strMail += str + ",";
        }

      }
      return strMail;
    }
    catch (Exception e) {
      return strMail;
    }
  }

  public String getStringSeparatedByQuots(String wono) {
    String[] arr = wono.split(",");
    String str = "";
    for (int i = 0; i < arr.length; i++) {
      String w = (arr[i]).trim();
      if (arr.length - 1 == i) {
        str += "'" + w + "'";
      }
      else {
        str += "'" + w + "',";
      }

    }
    return str;

  }

  public String getStringSeparatedByQuotsFromArray(String[] strArr) {
    String str = "";
    try {
      for (int i = 0; i < strArr.length; i++) {
        String s = strArr[i];
        if (i == strArr.length - 1) {
          str += "'" + s + "'";
        }
        else {
          str += "'" + s + "',";
        }
      }

    }
    catch (Exception e) {}
    return str;
  }

  public String getStringSeparatedByQuotsFromList(List list) {
    String str = "";
    try {
      for (int i = 0; i < list.size(); i++) {
        String s = (String) list.get(i);
        if (i == list.size() - 1) {
          str += "'" + s + "'";
        }
        else {
          str += "'" + s + "',";
        }
      }

    }
    catch (Exception e) {}
    return str;
  }

//THIS METHOD FOR REPLACING DOUBLE QOUTS TO &QUOT
  public String insertEscp(String field) {
    String retStr = "";
    try {
      if (field.trim().length() > 0) {
        //retStr  = field.replaceAll("SINGLEQOUTS", "\\\\\\\'");
        retStr = field.replaceAll("\'", "&apos"); //added by farid 19/08/2005

        //retStr  = retStr.replaceAll("DOUBLEQOUTS", "\\\\\\\'\\\\\\\'");
        retStr = retStr.replaceAll("\"", "&quot"); //added by farid 19/08/2005

        retStr = retStr.replaceAll("HASH", "#");
        //retStr  = retStr.replaceAll( "#","");

        //retStr = retStr.replaceAll("AMPRASAND", "&");
        retStr = retStr.replaceAll("&", "&amp"); //added by farid 19/08/2005

        //retStr += "&nbsp";
      }
    }
    catch (Exception e) {}
    return retStr;
  }

  //added by farid 30-08-05 resolve probles of ",',# etc
  public static String insertEscpToSent(String field) {
    String retStr = "";
    try {
      if (field.trim().length() > 0) {
        //retStr  = field.replaceAll("SINGLEQOUTS", "\\\\\\\'");
        retStr = field.replaceAll("\'", "&#39;"); //added by farid 19/08/2005

        //retStr  = retStr.replaceAll("DOUBLEQOUTS", "\\\\\\\'\\\\\\\'");
        retStr = retStr.replaceAll("\"", "&#34;"); //added by farid 19/08/2005

      }
    }
    catch (Exception e) {
      System.out.println("Excetpion in StrUtils : insertEscpToSent" + retStr);
    }
    return retStr;
  }

  //added by farid 30-08-05 resolve probles of ",' etc
  public static String insertEscpToReceive(String field) {
    String retStr = "";
    //System.out.println("Before Replacement : "+ field);
    try {
      if (field.trim().length() > 0) {
        retStr = field.replaceAll("SINGLEQOUTS", "&apos;");
        //retStr  = field.replaceAll("&apos;","\'"); //added by farid 19/08/2005

        retStr = retStr.replaceAll("DOUBLEQOUTS", "&quot;");
        // retStr  = retStr.replaceAll("&quot;","\""); //added by farid 19/08/2005

        retStr = retStr.replaceAll("HASH", "#");

        retStr = retStr.replaceAll("PLUS", "+");

        retStr = retStr.replaceAll("AMPRASAND", "&amp;");
        // retStr = retStr.replaceAll("&amp;","&"); //added by farid 19/08/2005

        //retStr += "&nbsp";
        //System.out.println("After Replacement  : "+ retStr);
      }
    }
    catch (Exception e) {}
    return retStr;
  }

  public String replaceCharacters2Send(String str) {
    //   System.out.println(" StrUtil : Before -" + str);
    if (fString(str).length() > 0) {

      if (str.indexOf("#") != -1) {
        str = str.replaceAll("\\#", "HASH");

      }
      if (str.indexOf("+") != -1) {
        str = str.replaceAll("\\+", "PLUS");

      }
      if (str.indexOf("\"") != -1) {
        str = str.replaceAll("\"", "DOUBLEQOUTS");

      }
      if (str.indexOf("\'") != -1) {
        str = str.replaceAll("\'", "SINGLEQOUTS");

      }
      if (str.indexOf("&") != -1) {
        str = str.replaceAll("&", "AMBERSENT");

      }
      
    }
    // System.out.println(" StrUtil : After -" + str);
    return str;
  }

  public String replaceCharacters2Recv(String str) {
    //   System.out.println("StrUtil : Before - " + str);
    if (fString(str).length() > 0) {

      if (str.indexOf("HASH") != -1) {
        str = str.replaceAll("HASH", "\\#");

      }
      if (str.indexOf("PLUS") != -1) {
        str = str.replaceAll("PLUS", "\\+");

      }
      if (str.indexOf("DOUBLEQOUTS") != -1) {
        str = str.replaceAll("DOUBLEQOUTS", "\"");

      }
      if (str.indexOf("SINGLEQOUTS") != -1) {
        str = str.replaceAll("SINGLEQOUTS", "\'");

      }
      
     
    }
    // System.out.println("StrUtil : After - " + str);
    return str;
  }

  /*
   'added for html tags
   07-09-2005
   farid.
   */
  public static String forHTMLTag(String aTagFragment) {
    final StringBuffer result = new StringBuffer();

    final StringCharacterIterator iterator = new StringCharacterIterator(
        aTagFragment);
    char character = iterator.current();
    while (character != StringCharacterIterator.DONE) {
      if (character == '<') {
        result.append("&lt;");
      }
      else if (character == '>') {
        result.append("&gt;");
      }
      else if (character == '\"') {
        result.append("&quot;");
      }
      else if (character == '\'') {
        result.append("&#039;");
      }
      else if (character == '\\') {
        result.append("&#092;");
      }
      else if (character == '&') {
        result.append("&amp;");
      }
      else {
        //the char is not a special one
        //add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }

  /*
   this is for handling regular expression in java
   */
  public static String forRegex(String aRegexFragment) {
    final StringBuffer result = new StringBuffer();

    final StringCharacterIterator iterator = new StringCharacterIterator(
        aRegexFragment);
    char character = iterator.current();
    while (character != StringCharacterIterator.DONE) {
      /*
       * All literals need to have backslashes doubled.
       */
      if (character == '.') {
        result.append("\\.");
      }
      else if (character == '\\') {
        result.append("\\\\");
      }
      else if (character == '?') {
        result.append("\\?");
      }
      else if (character == '*') {
        result.append("\\*");
      }
      else if (character == '+') {
        result.append("\\+");
      }
      else if (character == '&') {
        result.append("\\&");
      }
      else if (character == ':') {
        result.append("\\:");
      }
      else if (character == '{') {
        result.append("\\{");
      }
      else if (character == '}') {
        result.append("\\}");
      }
      else if (character == '[') {
        result.append("\\[");
      }
      else if (character == ']') {
        result.append("\\]");
      }
      else if (character == '(') {
        result.append("\\(");
      }
      else if (character == ')') {
        result.append("\\)");
      }
      else if (character == '^') {
        result.append("\\^");
      }
      else if (character == '$') {
        result.append("\\$");
      }
       else if (character == '"') {
        result.append("\"");
      }
      else {
        //the char is not a special one
        //add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }
  
   public static String removeChar(String s, char c) {
    String r = "";
    for (int i = 0; i < s.length(); i ++) {
       if (s.charAt(i) != c) r += s.charAt(i);
       }
    return r;
    }
    
       public  String leftPad (String stringToPad, String padder, int size)
    {
     StringBuffer strb = null; 
	  StringCharacterIterator sci = null;
		if (padder.length() == 0)
		{
			return stringToPad;
		}
		strb = new StringBuffer(size);
		sci  = new StringCharacterIterator(padder);
 
        while (strb.length() < (size - stringToPad.length()))
        {
			for (char ch = sci.first(); ch != CharacterIterator.DONE ; ch = sci.next())
			{
				if (strb.length() <  size - stringToPad.length())
				{
					strb.insert(  strb.length(),String.valueOf(ch));
				}
			}
		}
		return strb.append(stringToPad).toString();
	}

public  String rightPad (String stringToPad, String padder, int size)
	{
    StringBuffer strb = null; 
	  StringCharacterIterator sci = null;

		if (padder.length() == 0)
		{
			return stringToPad;
		}
		strb = new StringBuffer(stringToPad);
		sci  = new StringCharacterIterator(padder);
 
        while (strb.length() < size)
        {
			for (char ch = sci.first(); ch != CharacterIterator.DONE ; ch = sci.next())
			{
				if (strb.length() < size)
				{
					strb.append(String.valueOf(ch));
				}
			}
		}
		return strb.toString();
	}

public String InsertQuotes(String field){
 String retStr = "";
 try{
   if (field.trim().length() >0) {
         retStr = field.replaceAll("'", "''");
   }
 }catch(Exception e){}
 return retStr;
}


	public String replaceSpace2Send(String str) {
		// System.out.println(" StrUtil : Before -" + str);
		if (fString(str).length() > 0) {
			if (str.indexOf(" ") != -1) {
					str = str.replaceAll("\\s", "SPACE");
			}
    	}else { //14oct
			str ="SPACE";
		}
    	
      
		
		// System.out.println(" StrUtil : After -" + str);
		return str;
	}

public static String makeDoubleSlash(String path) 
{ 
StringBuffer sb = new StringBuffer(); 

char[] chars = new char[path.length()]; 

path.getChars(0, path.length(), chars, 0); 

for( int i = 0; i < chars.length; i++ ) 
{ 
sb.append(chars[i]); 
if( chars[i] == '\\' ) 
{ 
sb.append('\\'); 
} 
} 
return sb.toString(); 
}

// added by Arun for #1848
public static String formatForDoubleQuote(String str) {
    final StringBuffer result = new StringBuffer();

    final StringCharacterIterator iterator = new StringCharacterIterator(
    		str);
    char character = iterator.current();
    while (character != StringCharacterIterator.DONE) {
      if (character == '\"') {
        result.append("&quot;");
      } else {
          //the char is not a special one
          //add it to the result as is
          result.append(character);
        }
      character = iterator.next();
    }
    return result.toString();
  }


}
