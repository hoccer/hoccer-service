// Copyright 2010 Google Inc. All Rights Reserved.

// This file has been copied from the Google AppEngine project.
// It is licensed under the Apache License 2.0.

package com.google.appengine.api.blobstore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A byte range as parsed from a request Range header.  Format produced by this class is
 * also compatible with the X-AppEngine-BlobRange header, used for serving sub-ranges of
 * blobs.
 *
 */
public class ByteRange {
  private long start;
  private Long end;
  private Long total;

  static final String BYTES_UNIT = "bytes";
  static final String UNIT_REGEX = "([^=\\s]+)";
  static final String VALID_RANGE_HEADER_REGEX = UNIT_REGEX + "\\s*=\\s*(\\d*)\\s*-\\s*(\\d*)";
  static final String INVALID_RANGE_HEADER_REGEX = "((?:\\s*,\\s*(?:\\d*)-(?:\\d*))*)";
  static final Pattern RANGE_HEADER_PATTERN = Pattern.compile("^\\s*" +
                                                              VALID_RANGE_HEADER_REGEX +
                                                              INVALID_RANGE_HEADER_REGEX +
                                                              "\\s*$");

  static final String CONTENT_RANGE_UNIT_REGEX = "([^\\s]+)";
  static final String VALID_CONTENT_RANGE_HEADER_REGEX =
      BYTES_UNIT + "\\s+(\\d+)-(\\d+)/(\\d+)";
  static final Pattern CONTENT_RANGE_HEADER_PATTERN = Pattern.compile(
      "^\\s*" + VALID_CONTENT_RANGE_HEADER_REGEX + "\\s*$");

  /**
   * Constructor.
   *
   * @param start Start index of blob range to serve.  If negative, serve the last abs(start) bytes
   * of the blob.
   */
  public ByteRange(long start) {
    this(start, null, null);
  }

  /**
   * Constructor.
   *
   * @param start Start index of blob range to serve.  May not be negative.
   * @param end End index of blob range to serve.  Index is inclusive, meaning the byte indicated
   * by end is included in the response.
   */
  public ByteRange(long start, long end) {
    this(start, Long.valueOf(end), null);

    if (start < 0) {
      throw new IllegalArgumentException("If end is provided, start must be positive.");
    }

    if (end < start) {
      throw new IllegalArgumentException("end must be >= start.");
    }
  }

  public ByteRange(long start, long end, long total) {
      this(start, Long.valueOf(end), Long.valueOf(total));

      if(start >= total) {
          throw new IllegalArgumentException("If total is provided, start must be less than total.");
      }

      if(end >= total) {
          throw new IllegalArgumentException("If total is provided, end must be less than total.");
      }
  }

  protected ByteRange(long start, Long end, Long total) {
    this.start = start;
    this.end = end;
    this.total = total;
  }

  /**
   * Indicates whether or not this byte range indicates an end.
   *
   * @return true if byte range has an end.
   */
  public boolean hasEnd() {
    return end != null;
  }

  /**
   * Indicates whether or not this byte range indicates a total.
   *
   * @return true if byte range has an end.
   */
  public boolean hasTotal() {
      return total != null;
  }

  /**
   * Get start index of byte range.
   *
   * @return Start index of byte range.
   */
  public long getStart() {
    return start;
  }

  /**
   * Get end index of byte range.
   *
   * @return End index of byte range.
   *
   * @throws IllegalStateException if byte range does not have an end range.
   */
  public long getEnd() {
    if (!hasEnd()) {
      throw new IllegalStateException("Byte-range does not have end.  Check hasEnd() before use");
    }
    return end;
  }

  /**
   * Get end index of byte range.
   *
   * @return End index of byte range.
   *
   * @throws IllegalStateException if byte range does not have an end range.
   */
  public long getTotal() {
      if (!hasTotal()) {
          throw new IllegalStateException("Byte-range does not have total.  Check hasTotal() before use");
      }
      return total;
  }

  /**
   * Format byte range for use in header.
   */
  @Override
  public String toString() {
    String result;
    if (end != null) {
      result = BYTES_UNIT + "=" + start + "-" + end;
    } else {
      if (start < 0) {
        result = BYTES_UNIT + "="  + start;
      } else {
        result = BYTES_UNIT + "=" + start + "-";
      }
    }
    if (total != null) {
      result += "/" + total;
    }
    return result;
  }

  /**
   * Parse byte range from header.
   *
   * @param byteRange Byte range string as received from header.
   *
   * @return ByteRange object set to byte range as parsed from string.
   *
   * @throws RangeFormatException Unable to parse header because of invalid format.
   * @throws UnsupportedRangeFormatException Header is a valid HTTP range header, the specific
   * form is not supported by app engine.  This includes unit types other than "bytes" and multiple
   * ranges.
   */
  public static ByteRange parse(String byteRange) {
    Matcher matcher = RANGE_HEADER_PATTERN.matcher(byteRange);
    if (!matcher.matches()) {
      throw new RangeFormatException("Invalid range format: " + byteRange);
    }

    String unsupportedRange = matcher.group(4);
    if (!"".equals(unsupportedRange)) {
      throw new UnsupportedRangeFormatException("Unsupported range format: " + byteRange);
    }

    String units = matcher.group(1);
    if (!BYTES_UNIT.equals(units)) {
      throw new UnsupportedRangeFormatException("Unsupported unit: " + units);
    }

    String start = matcher.group(2);
    Long startValue;
    if ("".equals(start)) {
      startValue = null;
    } else {
      startValue = Long.parseLong(start);
    }

    String end = matcher.group(3);
    Long endValue;
    if ("".equals(end)) {
      endValue = null;
    } else {
      endValue = Long.parseLong(end);
    }

    if (startValue == null && endValue != null) {
      startValue = -endValue;
      endValue = null;
    }

    if (endValue == null) {
      return new ByteRange(startValue);
    } else {
      try {
        return new ByteRange((long) startValue, (long) endValue);
      } catch (IllegalArgumentException ex) {
        throw new RangeFormatException("Invalid range format: " + byteRange, ex);
      }
    }
  }

  /**
   * Parse content range from header for byte-range only.
   *
   * @param contentRange Content range string as received from header.
   *
   * @return ByteRange object set to byte range as parsed from string, but does not include the
   * size information.
   *
   * @throws RangeFormatException Unable to parse header because of invalid format.
   */
  public static ByteRange parseContentRange(String contentRange) {
    Matcher matcher = CONTENT_RANGE_HEADER_PATTERN.matcher(contentRange);
    if (!matcher.matches()) {
      throw new RangeFormatException("Invalid content-range format: " + contentRange);
    }

    return new ByteRange(Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2)), Long.parseLong(matcher.group(3)));
  }

  @Override
  public int hashCode() {
    int hash = 17;
    hash = hash * 37 + ((Long) start).hashCode();
    if (end != null) {
      hash = hash * 37 + end.hashCode();
    }
    return hash;
  }

  /**
   * Two {@code ByteRange} objects are considered equal if they have the same start and end.
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof ByteRange) {
      ByteRange key = (ByteRange) object;
      if (start != key.getStart()) {
        return false;
      }

      if (hasEnd() != key.hasEnd()) {
        return false;
      }

      if (hasEnd()) {
        return end.equals(key.getEnd());
      } else {
        return true;
      }
    }

    return false;
  }
}
