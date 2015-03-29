package io.ll.warden.utils.jodd;


import java.text.*;
import java.util.*;

public class LocaleUtil
{
  protected static Map<String, LocaleData> locales;

  protected static LocaleData lookupLocaleData(final String code) {
    LocaleData localeData = LocaleUtil.locales.get(code);
    if (localeData == null) {
      final String[] data = decodeLocaleCode(code);
      localeData = new LocaleData(new Locale(data[0], data[1], data[2]));
      LocaleUtil.locales.put(code, localeData);
    }
    return localeData;
  }

  protected static LocaleData lookupLocaleData(final Locale locale) {
    return lookupLocaleData(resolveLocaleCode(locale));
  }

  public static Locale getLocale(final String language, final String country, final String variant) {
    final LocaleData localeData = lookupLocaleData(resolveLocaleCode(language, country, variant));
    return localeData.locale;
  }

  public static Locale getLocale(final String language, final String country) {
    return getLocale(language, country, null);
  }

  public static Locale getLocale(final String languageCode) {
    final LocaleData localeData = lookupLocaleData(languageCode);
    return localeData.locale;
  }

  public static String resolveLocaleCode(final String lang, final String country, final String variant) {
    final StringBuilder code = new StringBuilder(lang);
    if (!StringUtil.isEmpty(country)) {
      code.append('_').append(country);
      if (!StringUtil.isEmpty(variant)) {
        code.append('_').append(variant);
      }
    }
    return code.toString();
  }

  public static String resolveLocaleCode(final Locale locale) {
    return resolveLocaleCode(locale.getLanguage(), locale.getCountry(), locale.getVariant());
  }

  public static String[] decodeLocaleCode(final String localeCode) {
    final String[] result = new String[3];
    final String[] data = StringUtil.splitc(localeCode, '_');
    result[0] = data[0];
    result[1] = (result[2] = "");
    if (data.length >= 2) {
      result[1] = data[1];
      if (data.length >= 3) {
        result[2] = data[2];
      }
    }
    return result;
  }

  public static DateFormatSymbolsEx getDateFormatSymbols(final Locale locale) {
    final LocaleData localeData = lookupLocaleData(locale);
    DateFormatSymbolsEx dfs = localeData.dateFormatSymbols;
    if (dfs == null) {
      dfs = new DateFormatSymbolsEx(locale);
      localeData.dateFormatSymbols = dfs;
    }
    return dfs;
  }

  public static NumberFormat getNumberFormat(final Locale locale) {
    final LocaleData localeData = lookupLocaleData(locale);
    NumberFormat nf = localeData.numberFormat;
    if (nf == null) {
      nf = NumberFormat.getInstance(locale);
      localeData.numberFormat = nf;
    }
    return nf;
  }

  static {
    LocaleUtil.locales = new HashMap<String, LocaleData>();
  }

  static class LocaleData
  {
    final Locale locale;
    DateFormatSymbolsEx dateFormatSymbols;
    NumberFormat numberFormat;

    LocaleData(final Locale locale) {
      super();
      this.locale = locale;
    }
  }
}