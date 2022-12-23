/*
 * Fit4J
 * Copyright (c) 2022 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.fit4j;

import static org.junit.jupiter.api.Assertions.*;

import java.text.*;
import java.util.*;
import org.junit.jupiter.api.*;

class FrameworkTests {
  @Test
  void testTypeAdapter() throws Exception {
    var fixture = new TestFixture();
    var adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleInt"));
    adapter.set(adapter.parse("123456"));
    assertEquals(123456, fixture.sampleInt);
    assertEquals("-234567", adapter.parse("-234567").toString());
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleInteger"));
    adapter.set(adapter.parse("54321"));
    assertEquals("54321", fixture.sampleInteger.toString());
    adapter = TypeAdapter.on(fixture, fixture.getClass().getMethod("pi"));
    assertEquals(3.14159, (Double) adapter.invoke(), 0.00001);
    assertEquals(3.14159862, adapter.invoke());
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("ch"));
    adapter.set(adapter.parse("abc"));
    assertEquals('a', fixture.ch);
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("name"));
    adapter.set(adapter.parse("xyzzy"));
    assertEquals("xyzzy", fixture.name);
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleFloat"));
    adapter.set(adapter.parse("6.02e23"));
    assertEquals(6.02e23, fixture.sampleFloat, 1e17);
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleArray"));
    adapter.set(adapter.parse("1,2,3"));
    assertEquals(1, fixture.sampleArray[0]);
    assertEquals(2, fixture.sampleArray[1]);
    assertEquals(3, fixture.sampleArray[2]);
    assertEquals("1, 2, 3", adapter.toString(fixture.sampleArray));
    assertTrue(adapter.equals(new int[] {1, 2, 3}, fixture.sampleArray));
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleDate"));
    var date = new GregorianCalendar(1949, Calendar.MAY, 26).getTime();
    adapter.set(adapter.parse(DateFormat.getDateInstance().format(date)));
    assertEquals(date, fixture.sampleDate);
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleByte"));
    adapter.set(adapter.parse("123"));
    assertEquals(123, fixture.sampleByte);
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleShort"));
    adapter.set(adapter.parse("12345"));
    assertEquals(12345, fixture.sampleShort);
  }

  @Test
  void testScientificDouble() {
    var pi = Double.valueOf(3.141592865);
    assertEquals(ScientificDouble.valueOf("3.14"), pi);
    assertEquals(ScientificDouble.valueOf("3.142"), pi);
    assertEquals(ScientificDouble.valueOf("3.1416"), pi);
    assertEquals(ScientificDouble.valueOf("3.14159"), pi);
    assertEquals(ScientificDouble.valueOf("3.141592865"), pi);
    assertNotEquals(ScientificDouble.valueOf("3.140"), pi);
    assertNotEquals(ScientificDouble.valueOf("3.144"), pi);
    assertNotEquals(ScientificDouble.valueOf("3.1414"), pi);
    assertNotEquals(ScientificDouble.valueOf("3.141592863"), pi);
    assertEquals(ScientificDouble.valueOf("6.02e23"), 6.02e23);
    assertEquals(ScientificDouble.valueOf("6.02E23"), 6.024E23);
    assertEquals(ScientificDouble.valueOf("6.02e23"), 6.016e23);
    assertNotEquals(ScientificDouble.valueOf("6.02e23"), 6.026e23);
    assertNotEquals(ScientificDouble.valueOf("6.02e23"), 6.014e23);
  }

  @Test
  void testEscape() {
    var junk = "!@#$%^*()_-+={}|[]\\:\";',./?`";
    assertEquals(junk, Fixture.escape(junk));
    assertEquals("", Fixture.escape(""));
    assertEquals("&lt;", Fixture.escape("<"));
    assertEquals("&lt;&lt;", Fixture.escape("<<"));
    assertEquals("x&lt;", Fixture.escape("x<"));
    assertEquals("&amp;", Fixture.escape("&"));
    assertEquals("&lt;&amp;&lt;", Fixture.escape("<&<"));
    assertEquals("&amp;&lt;&amp;", Fixture.escape("&<&"));
    assertEquals("a &lt; b &amp;&amp; c &lt; d", Fixture.escape("a < b && c < d"));
    assertEquals("a<br />b", Fixture.escape("a\nb"));
  }

  static class TestFixture extends Fixture {
    public byte sampleByte;
    public short sampleShort;
    public int sampleInt;
    public Integer sampleInteger;
    public float sampleFloat;
    public char ch;
    public String name;
    public int[] sampleArray;
    public Date sampleDate;

    public double pi() {
      return 3.14159862;
    }
  }
}
