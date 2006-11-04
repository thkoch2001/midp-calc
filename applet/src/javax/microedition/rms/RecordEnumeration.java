package javax.microedition.rms;

import netscape.javascript.JSObject;

public class RecordEnumeration
{
  public RecordEnumeration() {
  }

  public void destroy() {
  }

  static byte[] decodeByteArray(String s) {
    int i;
    if (s.length() == 0 || (i = s.indexOf("MIDPCalcState="))<0)
      return null;
    i += 14;
    int numBytes = ((s.charAt(i+0)-'0')*10000+
                    (s.charAt(i+1)-'0')*1000+
                    (s.charAt(i+2)-'0')*100+
                    (s.charAt(i+3)-'0')*10+
                    (s.charAt(i+4)-'0'));
    byte [] data = new byte[numBytes];
    String charSet =
      "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
    int numBits = numBytes*8;
    int bitPos = 0;
    i += 5;
    while (bitPos<numBits) {
      int bits = charSet.indexOf(s.charAt(i));
      data[bitPos/8] |= (byte)(bits<<(bitPos%8));
      if (bitPos%8 > 2 && bitPos/8+1<data.length)
        data[bitPos/8+1] |= (byte)(bits>>(8-bitPos%8));
      bitPos += 6;
      i++;
    }
    return data;
  }

  public byte[] nextRecord() {
    byte [] record = null;
    try {
      record = decodeByteArray((String)JSObject.getWindow(
        midpcalc.CalcApplet.getCurrentApplet()).eval("document.cookie"));
    } catch (Throwable e) {
      record = null;
    }
    return record;
  }

  public int nextRecordId() {
    return 0;
  }

  public boolean hasNextElement() {
    return false;
  }
}
