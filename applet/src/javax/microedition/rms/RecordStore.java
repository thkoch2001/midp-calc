package javax.microedition.rms;

public class RecordStore
{
  private RecordStore() {
  }

  public static RecordStore openRecordStore(String a, boolean create)
    throws Exception
  {
    throw new Exception();
  }

  public static void deleteRecordStore(String a) {
  }

  public RecordEnumeration enumerateRecords(Object a, Object b, boolean c) {
    return new RecordEnumeration();
  }

  public int addRecord(byte[] data, int offset, int nymBytes) {
    return 0;
  }

  public void setRecord(int id, byte[] newData, int offset, int numBytes) {
  }

  public void closeRecordStore() {
  }

  public String getName() {
    return null;
  }
}
