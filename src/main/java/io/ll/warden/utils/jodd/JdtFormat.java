package io.ll.warden.utils.jodd;

public class JdtFormat
{
  protected final String format;
  protected final JdtFormatter formatter;

  public JdtFormat(final JdtFormatter formatter, final String format) {
    super();
    this.format = format;
    this.formatter = formatter;
  }

  public String getFormat() {
    return this.format;
  }

  public JdtFormatter getFormatter() {
    return this.formatter;
  }

  public String convert(final JDateTime jdt) {
    return this.formatter.convert(jdt, this.format);
  }

  public DateTimeStamp parse(final String value) {
    return this.formatter.parse(value, this.format);
  }
}

