package ral;

import javax.microedition.rms.*;

public final class PropertyStore
    implements RecordFilter
{
  private final RecordStore rs;
  
  private PropertyStore(RecordStore rs) {
    this.rs = rs;
  }
  
  public static PropertyStore open(String name) {
    RecordStore rs;
    try {
      rs = RecordStore.openRecordStore(name,true);
    } catch (Exception e) {
      // Fatal error.
      System.out.println(e);
      return null;
    }
    return new PropertyStore(rs);
  }
  
  public void close() {
    try {
      rs.closeRecordStore();
    } catch (Exception e) {
    }
  }
  
  public void destroy() {
    String recordStoreName = null;
    try {
      recordStoreName = rs.getName();
      try {
        // Close repeatedly until an exception is thrown
        for (;;)
          rs.closeRecordStore();
      } catch (Exception e) {
        // Ignore this
      }
      RecordStore.deleteRecordStore(recordStoreName);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  
  byte filterData;
  public boolean matches(byte [] candidate) {
    return (candidate.length>0 && candidate[0]==filterData);
  }
  
  public synchronized int getProperty(byte[] propertyIDValue)
  {
    filterData = propertyIDValue[0];
    RecordEnumeration re = null;
    try {
      re = rs.enumerateRecords(this, null, false);
      int nBytes = rs.getRecord(re.nextRecordId(), propertyIDValue, 0);
      re.destroy();
      return nBytes;
    } catch (Exception e) {
      if (re != null)
        re.destroy();
      return 0;
    }
  }
  
  public synchronized void setProperty(byte[] propertyIDValue,
                                       int length)
  {
    filterData = propertyIDValue[0];
    RecordEnumeration re = null;
    try {
      re = rs.enumerateRecords(this, null, false);
      int id;
      try {
        id = re.nextRecordId();
        re.destroy();
        re = null;
      } catch (Exception e) {
        id = -1;
      }
      if (id < 0) {
        rs.addRecord(propertyIDValue,0,length);
      } else {
        rs.setRecord(id,propertyIDValue,0,length);
      }
    } catch (Exception e) {
      if (re != null)
        re.destroy();
    }
  }

  public synchronized void deleteProperty(byte propertyID) {
    filterData = propertyID;
    RecordEnumeration re = null;
    try {
      re = rs.enumerateRecords(this, null, false);
      int id = re.nextRecordId();
      re.destroy();
      re = null;
      rs.deleteRecord(id);
    } catch (Exception e) {
      if (re != null)
        re.destroy();
    }
  }
}
