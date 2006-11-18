package javax.microedition.rms;

import netscape.javascript.JSObject;

public class RecordStore
{
    private RecordStore() {
    }

    public static RecordStore openRecordStore(String a, boolean create) {
        return new RecordStore();
    }

    public static void deleteRecordStore(String a) {
        try {
            JSObject.getWindow(midpcalc.CalcApplet.getCurrentApplet()).
                eval("document.cookie = 'MIDPCalcState=00000; "+
                     "expires=01-Jan-1970 GMT';");
        } catch (Throwable e) {
        }
    }

    public RecordEnumeration enumerateRecords(Object a, Object b, boolean c) {
        return new RecordEnumeration();
    }

    static String encodeByteArray(byte[] data, int offset, int numBytes) {
        StringBuffer buf = new StringBuffer();
        String charSet =
            "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
        int numBits = numBytes*8;
        int bitPos = 0;
        buf.append((char)((numBytes/10000)%10+'0'));
        buf.append((char)((numBytes/1000)%10+'0'));
        buf.append((char)((numBytes/100)%10+'0'));
        buf.append((char)((numBytes/10)%10+'0'));
        buf.append((char)(numBytes%10+'0'));
        while (bitPos<numBits) {
            int bits = (data[offset+bitPos/8]>>(bitPos%8))&0x3f;
            if (bitPos%8 > 2 && offset+bitPos/8+1<data.length)
                bits = (bits+(data[offset+bitPos/8+1]<<(8-bitPos%8)))&0x3f;
            buf.append(charSet.charAt(bits));
            bitPos += 6;
        }
        return buf.toString();
    }

    public int addRecord(byte[] data, int offset, int numBytes) {
        try {
            JSObject.getWindow(midpcalc.CalcApplet.getCurrentApplet()).
                eval("document.cookie='MIDPCalcState="+
                     encodeByteArray(data,offset,numBytes)+
                     "; expires=01-Jan-2020 GMT';");
        } catch (Throwable e) {
        }
        return 0;
    }

    public void setRecord(int id, byte[] newData, int offset, int numBytes) {
    }

    public void closeRecordStore() throws Exception {
        throw new Exception();
    }

    public String getName() {
        return null;
    }
}
