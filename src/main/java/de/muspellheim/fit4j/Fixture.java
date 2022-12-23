/*
 * Fit4J
 * Copyright (c) 2022 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.fit4j;

import java.text.*;
import java.util.*;

public class Fixture {
  public Object parse(String s, Class<?> type) throws Exception {
    if (type.equals(String.class)) {
      return s;
    }
    if (type.equals(Date.class)) {
      return DateFormat.getDateInstance().parse(s);
    }
    throw new Exception("can't yet parse " + type);
  }
}
