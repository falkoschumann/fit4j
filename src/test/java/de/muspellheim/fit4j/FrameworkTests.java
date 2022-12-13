package de.muspellheim.fit4j;

import static org.junit.jupiter.api.Assertions.*;

import java.text.*;
import java.util.*;
import org.junit.jupiter.api.*;

class FrameworkTests {
  @Test
  void testTypeAdapter() throws Exception {
    TestFixture fixture = new TestFixture();
    TypeAdapter adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleInt"));
    adapter.set(adapter.parse("123456"));
    assertEquals(123456, fixture.sampleInt);
    assertEquals("-234567", adapter.parse("-234567").toString());
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleInteger"));
    adapter.set(adapter.parse("54321"));
    assertEquals("54321", fixture.sampleInteger.toString());
    adapter = TypeAdapter.on(fixture, fixture.getClass().getMethod("pi", new Class[] {}));
    assertEquals(3.14159, ((Double) adapter.invoke()).doubleValue(), 0.00001);
    assertEquals(Double.valueOf(3.14159862), adapter.invoke());
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
    Date date = new GregorianCalendar(1949, 4, 26).getTime();
    adapter.set(adapter.parse(DateFormat.getDateInstance().format(date)));
    assertEquals(date, fixture.sampleDate);
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleByte"));
    adapter.set(adapter.parse("123"));
    assertEquals(123, fixture.sampleByte);
    adapter = TypeAdapter.on(fixture, fixture.getClass().getField("sampleShort"));
    adapter.set(adapter.parse("12345"));
    assertEquals(12345, fixture.sampleShort);
  }

  class TestFixture extends Fixture {
    public byte sampleByte;
    public short sampleShort;
    public int sampleInt;
    public Integer sampleInteger;
    public float sampleFloat;

    public double pi() {
      return 3.14159862;
    }

    public char ch;
    public String name;
    public int[] sampleArray;
    public Date sampleDate;
  }
}
