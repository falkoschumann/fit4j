/*
 * Fit4J
 * Copyright (c) 2022 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.fit4j;

public class ScientificDouble extends Number implements Comparable<Number> {
  protected double value;
  protected double precision;

  public ScientificDouble(double value) {
    this.value = value;
    this.precision = 0;
  }

  public static ScientificDouble valueOf(String s) {
    var result = new ScientificDouble(Double.parseDouble(s));
    result.precision = precision(s);
    return result;
  }

  public static double precision(String s) {
    var value = Double.parseDouble(s);
    var bound = Double.parseDouble(tweak(s.trim()));
    return Math.abs(bound - value);
  }

  public static String tweak(String s) {
    int pos;
    if ((pos = s.toLowerCase().indexOf("e")) >= 0) {
      return tweak(s.substring(0, pos)) + s.substring(pos);
    }
    if (s.contains(".")) {
      return s + "5";
    }
    return s + ".5";
  }

  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj instanceof Number n) {
      return compareTo(n) == 0;
    }

    return false;
  }

  public int compareTo(Number obj) {
    var other = obj.doubleValue();
    var diff = value - other;
    if (diff < -precision) {
      return -1;
    }
    if (diff > precision) {
      return 1;
    }

    if (Double.isNaN(value) && Double.isNaN(other)) {
      return 0;
    }
    if (Double.isNaN(value)) {
      return 1;
    }
    if (Double.isNaN(other)) {
      return -1;
    }

    return 0;
  }

  public String toString() {
    return Double.toString(value);
  }

  public double doubleValue() {
    return value;
  }

  public float floatValue() {
    return (float) value;
  }

  public long longValue() {
    return (long) value;
  }

  public int intValue() {
    return (int) value;
  }
}
