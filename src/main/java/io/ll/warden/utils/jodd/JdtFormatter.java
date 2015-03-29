package io.ll.warden.utils.jodd;

import java.io.*;

public interface JdtFormatter extends Serializable
{
  String convert(JDateTime p0, String p1);

  DateTimeStamp parse(String p0, String p1);
}
