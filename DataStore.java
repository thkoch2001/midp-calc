package ral;

import javax.microedition.rms.*;
import java.io.*;

public final class DataStore
    extends ByteArrayOutputStream
{
  private final RecordStore rs;
  private final static int magic = 0x526f6172; // "Roar"

  private DataStore(RecordStore rs) {
    this.rs = rs;
  }
  
  public static DataStore open(String name) {
    RecordStore rs;
    try {
      rs = RecordStore.openRecordStore(name,true);
    } catch (Exception e) {
      // Fatal error.
      //System.out.println(e);
      return null;
    }
    return new DataStore(rs);
  }

  public DataInputStream startReading() {
    try {
      RecordEnumeration re = rs.enumerateRecords(null, null, false);
      DataInputStream dis =
        new DataInputStream(new ByteArrayInputStream(re.nextRecord()));
      re.destroy();
      if (dis.readInt() != magic) {
        dis.close();
        return null;
      }
      return dis;
    } catch (Exception e) {
      return null;
    }
  }

  public DataOutputStream startWriting() {
    reset();
    DataOutputStream dos = new DataOutputStream(this);
    try {
      dos.writeInt(magic);
      return dos;
    } catch (IOException ioe) {
      return null;
    }
  }

  public void close() {
    try {
      RecordEnumeration re = rs.enumerateRecords(null, null, false);
      if (re.hasNextElement()) {
        int id = re.nextRecordId();
        rs.setRecord(id,buf,0,count);
      } else {
        rs.addRecord(buf,0,count);
      }
      re.destroy();
    } catch (Exception e) {
    }      
    try {
      super.close();
      rs.closeRecordStore();
    } catch (Exception e) {
    }
  }

  public void destroy() {
    try {
      String recordStoreName = rs.getName();
      try {
        // Close repeatedly until an exception is thrown
        for (;;)
          rs.closeRecordStore();
      } catch (Exception e) {
        // Ignore this
      }
      RecordStore.deleteRecordStore(recordStoreName);
    } catch (Exception e) {
	//System.out.println(e);
    }
  }
}
