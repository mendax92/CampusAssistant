package com.ming.base.util;


import com.ming.base.retrofit2.request.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A collection of utilities for encoding URLs.
 *
 * @since 4.0
 */
public class URLEncodedUtils {

    private static final String DEFAULT_CONTENT_CHARSET = "UTF-8";

    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String URI_SEPARATOR = "?";
    private static final String NAME_VALUE_SEPARATOR = "=";

    public static List<NameValuePair> parse(final URI uri) {
        return parse(uri, null);
    }

    /**
     * Returns a list of {@link NameValuePair NameValuePairs} as built from the
     * URI's query portion. For example, a URI of
     * http://example.org/path/to/file?a=1&b=2&c=3 would return a list of three
     * NameValuePairs, one for a=1, one for b=2, and one for c=3.
     * <p>
     * This is typically useful while parsing an HTTP PUT.
     *
     * @param uri      uri to parse
     * @param encoding encoding to use while parsing the query
     */
    public static List<NameValuePair> parse(final URI uri, final String encoding) {
        List<NameValuePair> result = new ArrayList<>();
        final String query = uri.getRawQuery();
        if (query != null && query.length() > 0) {
            parse(result, new Scanner(query), encoding);
        }
        return result;
    }

    /**
     * Adds all parameters within the Scanner to the list of
     * <code>parameters</code>, as encoded by <code>encoding</code>. For
     * example, a scanner containing the string <code>a=1&b=2&c=3</code> would
     * add the {@link NameValuePair NameValuePairs} a=1, b=2, and c=3 to the
     * list of parameters.
     *
     * @param parameters List to add parameters to.
     * @param scanner    Input that contains the parameters to parse.
     * @param encoding   Encoding to use when decoding the parameters.
     */
    public static void parse(
            final List<NameValuePair> parameters,
            final Scanner scanner,
            final String encoding) {
        scanner.useDelimiter(PARAMETER_SEPARATOR);
        while (scanner.hasNext()) {
            final String[] nameValue = scanner.next().split(NAME_VALUE_SEPARATOR);
            if (nameValue.length == 0 || nameValue.length > 2)
                throw new IllegalArgumentException("bad parameter");

            final String name = decode(nameValue[0], encoding);
            String value = null;
            if (nameValue.length == 2)
                value = decode(nameValue[1], encoding);
            parameters.add(new NameValuePair(name, value));
        }
    }

    public static String format(String url, NameValuePair parameter) {
        if (parameter == null) {
            return url;
        }
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(parameter);
        return format(url, parameters);
    }

    public static String format(String url, List<NameValuePair> parameters) {
        return format(url, parameters, null);
    }

    public static String format(String url,
                                final List<NameValuePair> parameters,
                                final String encoding) {
        String result;
        try {
            URI uri = URI.create(url);
            List<NameValuePair> pairs = parse(uri, encoding);
            List<NameValuePair> nameValuePairs = new ArrayList<>(parameters);
            next:
            for (NameValuePair pair : pairs) {
                for (NameValuePair param : nameValuePairs) {
                    if (StringUtil.equalsString(param.getName(), pair.getName())) {
                        continue next;
                    }
                }
                nameValuePairs.add(pair);
            }

            int uriIndex = url.indexOf(URI_SEPARATOR);

            result = url.substring(0, uriIndex > 0 ? uriIndex : url.length()) + URI_SEPARATOR + format(nameValuePairs, encoding);
            Log.d("url", "format result:" + result);
        } catch (Exception ex) {
            ex.printStackTrace();
            int uriIndex = url.indexOf(URI_SEPARATOR);
            result = url.substring(0, uriIndex > 0 ? uriIndex : url.length()) + URI_SEPARATOR + format(parameters, encoding);
        }
        return result;
    }

    public static String format(List<NameValuePair> parameters) {
        return format(parameters, null);
    }

    /**
     * Returns a String that is suitable for use as an <code>application/x-www-form-urlencoded</code>
     * list of parameters in an HTTP PUT or HTTP POST.
     *
     * @param parameters The parameters to include.
     * @param encoding   The encoding to use.
     */
    public static String format(
            final List<NameValuePair> parameters,
            final String encoding) {
        final StringBuilder result = new StringBuilder();
        for (final NameValuePair parameter : parameters) {
            final String encodedName = encode(parameter.getName(), encoding);
            final String value = parameter.getValue();
            final String encodedValue = value != null ? encode(value, encoding) : "";
            if (result.length() > 0)
                result.append(PARAMETER_SEPARATOR);
            result.append(encodedName);
            result.append(NAME_VALUE_SEPARATOR);
            result.append(encodedValue);
        }
        return result.toString();
    }

    private static String decode(final String content) {
        return decode(content, null);
    }

    private static String decode(final String content, final String encoding) {
        try {
            return URLDecoder.decode(content,
                    encoding != null ? encoding : DEFAULT_CONTENT_CHARSET);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }

    public static String encode(String content) {
        return encode(content, null);
    }

    public static String encode(final String content, final String encoding) {
        try {
            return URLEncoder.encode(content,
                    encoding != null ? encoding : DEFAULT_CONTENT_CHARSET);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }

    /**
     * 获取url，取消参数
     *
     * @param url
     * @return
     */
    public static String getUrlWithNotParam(String url) {
        int uriIndex = url.indexOf("?");
        return url.substring(0, uriIndex > 0 ? uriIndex : url.length());
    }

}
