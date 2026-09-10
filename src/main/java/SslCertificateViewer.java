import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.security.auth.x500.X500Principal;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.validator.routines.DomainValidator;

import io.github.toolfactory.narcissus.Narcissus;

public class SslCertificateViewer {

	public static void main(final String[] args) throws IOException {
		//
		Map<String, Date> map = null;
		//
		Entry<String, Date> entry = null;
		//
		for (int i = 0; args != null && i < args.length; i++) {
			//
			if ((entry = getEntry(args[i])) == null) {
				//
				continue;
				//
			} // if
				//
			put(map = ObjectUtils.getIfNull(map, LinkedHashMap::new), entry.getKey(), entry.getValue());
			//
		} // for
			//
		if (map != null && map.entrySet() != null) {
			//
			Date date = null;
			//
			Map<String, Duration> durations = null;
			//
			for (final Entry<String, Date> en : map.entrySet()) {
				//
				if (en == null) {
					//
					continue;
					//
				} // if
					//
				put(durations = ObjectUtils.getIfNull(durations, LinkedHashMap::new), en.getKey(),
						toDuration(en.getValue(), date = ObjectUtils.getIfNull(date, Date::new)));
				//
			} // for
				//
			final int maxLength = orElse(max(mapToInt(stream(keySet(map)), StringUtils::length)), 0);
			//
			String key = null;
			//
			DateFormat df = null;
			//
			for (final Entry<String, Date> en : map.entrySet()) {
				//
				if (en == null) {
					//
					continue;
					//
				} // if
					//
				System.out.println(StringUtils.rightPad(key = en.getKey(), maxLength) + " "
						+ format(df = ObjectUtils.getIfNull(df, () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")),
								en.getValue())
						+ " " + toString(get(durations, key)));
				//
			} // for
				//
		} // if
			//
	}

	private static <K> Set<K> keySet(final Map<K, ?> instance) {
		return instance != null ? instance.keySet() : null;
	}

	private static int orElse(final OptionalInt instance, final int defaultValue) {
		return instance != null ? instance.orElse(defaultValue) : defaultValue;
	}

	private static OptionalInt max(final IntStream instance) {
		return instance != null ? instance.max() : null;
	}

	private static <T> IntStream mapToInt(final Stream<T> instance, final ToIntFunction<T> mapper) {
		return instance != null ? instance.mapToInt(mapper) : null;
	}

	private static <V> V get(final Map<?, V> instance, final Object key) {
		return instance != null ? instance.get(key) : null;
	}

	private static String format(final DateFormat instance, final Date date) {
		//
		if (instance == null || date == null) {
			//
			return null;
			//
		} // if
			//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(
						stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "calendar")), Collectors.toList()),
				x -> get(x, 0), null);
		//
		return field == null || Narcissus.getField(instance, field) != null ? instance.format(date) : null;
		//
	}

	private static int size(final Collection<?> instance) {
		return instance != null ? instance.size() : 0;
	}

	private static <E> E get(final List<E> instance, final int index) {
		return instance != null ? instance.get(index) : null;
	}

	private static class Duration {
		private Integer year, month, day = null;
	}

	private static <T> void testAndAccept(final Predicate<T> predicate, final T value, final Consumer<T> consumer) {
		if (test(predicate, value)) {
			accept(consumer, value);
		}
	}

	private static <T> void accept(final Consumer<T> consumer, final T value) {
		if (consumer != null) {
			consumer.accept(value);
		}
	}

	private static String toString(final Duration instance) {
		//
		if (instance == null) {
			//
			return null;
			//
		} // if
			//
		StringBuilder sb = null;
		//
		if (instance.year != null && instance.year.intValue() != 0) {
			//
			append(append(sb = ObjectUtils.getIfNull(sb, StringBuilder::new), instance.year), " Year");
			//
		} // if
			//
		if (instance.month != null && instance.month.intValue() != 0) {
			//
			testAndAccept(StringUtils::isNotEmpty, sb = ObjectUtils.getIfNull(sb, StringBuilder::new),
					x -> append(x, ' '));
			//
			append(append(sb, StringUtils.leftPad(Objects.toString(instance.month), 2)), " Month");
			//
		} // if
			//
		if (instance.day != null && instance.day.intValue() != 0) {
			//
			testAndAccept(StringUtils::isNotEmpty, sb = ObjectUtils.getIfNull(sb, StringBuilder::new),
					x -> append(x, ' '));
			//
			append(append(sb, StringUtils.leftPad(Objects.toString(instance.day), 2)), " Day");
			//
		} // if
			//
		return Objects.toString(sb);
		//

	}

	private static Duration toDuration(final Date a, final Date b) {
		//
		final Calendar ca = Calendar.getInstance();
		//
		setTime(ca, a);
		//
		final Calendar cb = Calendar.getInstance();
		//
		setTime(cb, b);
		//
		final Duration duration = new Duration();
		//
		duration.year = Integer.valueOf(ca.get(Calendar.YEAR) - cb.get(Calendar.YEAR));
		//
		if ((duration.month = Integer.valueOf(ca.get(Calendar.MONTH) - cb.get(Calendar.MONTH))) != null
				&& duration.month < 0) {
			//
			final Calendar c = Calendar.getInstance();
			//
			setTime(c, a);
			//
			roll(c, Calendar.YEAR, -1);
			//
			if (duration.year != null) {
				//
				duration.year = Integer.valueOf(duration.year.intValue() - 1);
				//
			} // if
				//
			duration.month = Integer.valueOf(duration.month.intValue() + c.getMaximum(Calendar.MONTH) + 1);
			//
		} // if
			//
		if ((duration.day = Integer.valueOf(ca.get(Calendar.DATE) - cb.get(Calendar.DATE))) != null
				&& duration.day < 0) {
			//
			final Calendar c = Calendar.getInstance();
			//
			setTime(c, a);
			//
			roll(c, Calendar.MONTH, -1);
			//
			if (duration.month != null) {
				//
				duration.month = Integer.valueOf(duration.month.intValue() - 1);
				//
			} // if
				//
			duration.day = Integer.valueOf(duration.day.intValue() + c.getMaximum(Calendar.DATE));
			//
		} // if
			//
		return duration;
		//
	}

	private static void roll(final Calendar instance, final int field, final int amount) {
		//
		if (instance != null) {
			//
			instance.roll(field, amount);
			//
		} // if
			//
	}

	private static void setTime(final Calendar instance, final Date date) {
		//
		if (instance == null || date == null) {
			//
			return;
			//
		} // if
			//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(
						stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "zone")), Collectors.toList()),
				x -> get(x, 0), null);
		//
		if (field == null || Narcissus.getField(instance, field) != null) {
			//
			instance.setTime(date);
			//
		} // if
			//
	}

	private static StringBuilder append(final StringBuilder instance, final char c) {
		//
		if (instance == null) {
			//
			return instance;
			//
		} // if
			//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(
						stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "value")), Collectors.toList()),
				x -> get(x, 0), null);
		//
		return field == null || Narcissus.getField(instance, field) != null ? instance.append(c) : instance;
		//
	}

	private static StringBuilder append(final StringBuilder instance, final Object obj) {
		//
		if (instance == null) {
			//
			return instance;
			//
		} // if
			//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(
						stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "value")), Collectors.toList()),
				x -> get(x, 0), null);
		//
		return field == null || Narcissus.getField(instance, field) != null ? instance.append(obj) : instance;
		//
	}

	private static <T, R, A> R collect(final Stream<T> instance, final Collector<? super T, A, R> collector) {
		//
		return instance != null && (collector != null || Proxy.isProxyClass(getClass(instance)))
				? instance.collect(collector)
				: null;
		//
	}

	private static String getName(final Member instance) {
		return instance != null ? instance.getName() : null;
	}

	private static <T> Stream<T> filter(final Stream<T> instance, final Predicate<? super T> predicate) {
		return instance != null ? instance.filter(predicate) : instance;
	}

	private static <T> Stream<T> stream(final Collection<T> instance) {
		return instance != null ? instance.stream() : null;
	}

	private static Class<?> getClass(final Object instance) {
		return instance != null ? instance.getClass() : null;
	}

	private static <K, V> void put(final Map<K, V> instance, final K key, final V value) {
		if (instance != null) {
			instance.put(key, value);
		}
	}

	private static Entry<String, Date> getEntry(final String url) throws IOException {
		//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(
						stream(testAndApply(Objects::nonNull, getClass(url), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "value")), Collectors.toList()),
				x -> get(x, 0), null);
		//
		final HttpsURLConnection httpsURLConnection = cast(HttpsURLConnection.class,
				openConnection(testAndApply(x -> x != null && (field == null || Narcissus.getField(x, field) != null),
						url, URL::new, null)));
		//
		connect(httpsURLConnection);
		//
		final Certificate[] certificates = getServerCertificates(httpsURLConnection);
		//
		final DomainValidator domainValidator = DomainValidator.getInstance();
		//
		final List<Certificate> list = collect(
				filter(testAndApply(Objects::nonNull, certificates, Arrays::stream, null), x -> {
					//
					if (domainValidator != null) {
						//
						return domainValidator.isValid(longestCommonSubstring(url,
								getName(getSubjectX500Principal(cast(X509Certificate.class, x)))));
						//
					} // if
						//
					return true;
					//
				}), Collectors.toList());
		//
		X509Certificate x509Certificate = null;
		//
		String name = null;
		//
		Date date = null;
		//
		for (int i = 0; i < size(list); i++) {
			//
			if (date != null) {
				//
				throw new IllegalStateException();
				//
			} // if
				//
			name = StringUtils.substringAfter(
					getName(getSubjectX500Principal(x509Certificate = cast(X509Certificate.class, get(list, i)))), '=');
			//
			date = getNotAfter(x509Certificate);
			//
		} // for
			//
		disconnect(httpsURLConnection);
		//
		return Pair.of(name, date);
		//
	}

	private static Date getNotAfter(final X509Certificate instance) {
		return instance != null ? instance.getNotAfter() : null;
	}

	private static X500Principal getSubjectX500Principal(final X509Certificate instance) {
		return instance != null ? instance.getSubjectX500Principal() : null;
	}

	private static void disconnect(final HttpsURLConnection instance) throws IOException {
		//
		if (instance == null) {
			//
			return;
			//
		} // if
			//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(
						stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "delegate")), Collectors.toList()),
				x -> get(x, 0), null);

		//
		if (field == null || Narcissus.getField(instance, field) != null) {
			//
			instance.disconnect();
			//
		} // if
			//
	}

	private static void connect(final HttpsURLConnection instance) throws IOException {
		//
		if (instance == null) {
			//
			return;
			//
		} // if
			//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(
						stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "delegate")), Collectors.toList()),
				x -> get(x, 0), null);

		//
		if (field == null || Narcissus.getField(instance, field) != null) {
			//
			instance.connect();
			//
		} // if
			//
	}

	private static Certificate[] getServerCertificates(final HttpsURLConnection instance)
			throws SSLPeerUnverifiedException {
		//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(
						stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "delegate")), Collectors.toList()),
				x -> get(x, 0), null);

		//
		return instance != null && (field == null || Narcissus.getField(instance, field) != null)
				? instance.getServerCertificates()
				: null;
		//
	}

	private static URLConnection openConnection(final URL instance) throws IOException {
		//
		if (instance == null) {
			//
			return null;
			//
		} // if
			//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(
						stream(testAndApply(Objects::nonNull, getClass(instance), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "handler")), Collectors.toList()),
				x -> get(x, 0), null);
		//
		return field == null || Narcissus.getField(instance, field) != null ? instance.openConnection() : null;
		//
	}

	private static <T, R, E extends Throwable> R testAndApply(final Predicate<T> predicate, final T value,
			final FailableFunction<T, R, E> functionTrue, final FailableFunction<T, R, E> functionFalse) throws E {
		return test(predicate, value) ? apply(functionTrue, value) : apply(functionFalse, value);
	}

	private static <T> boolean test(final Predicate<T> instance, final T value) {
		return instance != null && instance.test(value);
	}

	private static <T, R, E extends Throwable> R apply(final FailableFunction<T, R, E> instance, final T value)
			throws E {
		return instance != null ? instance.apply(value) : null;
	}

	private static String getName(final Principal instance) {
		return instance != null ? instance.getName() : null;
	}

	private static String longestCommonSubstring(final String a, final String b) {
		//
		int start = 0, max = 0;
		//
		final Field field = testAndApply(x -> size(x) == 1,
				collect(filter(stream(testAndApply(Objects::nonNull, getClass(a), FieldUtils::getAllFieldsList, null)),
						f -> Objects.equals(getName(f), "value")), Collectors.toList()),
				x -> get(x, 0), null);
		//
		final boolean conditionA = field == null || Narcissus.getField(a, field) != null;
		//
		final boolean conditionB = field == null || Narcissus.getField(b, field) != null;
		//
		for (int i = 0; conditionA && i < StringUtils.length(a); i++) {
			//
			for (int j = 0; conditionB && j < StringUtils.length(b); j++) {
				//
				int x = 0;
				//
				while (a.charAt(i + x) == b.charAt(j + x)) {
					//
					x++;
					//
					if (((i + x) >= a.length()) || ((j + x) >= b.length())) {
						//
						break;
						//
					} // if
						//
				} // while
					//
				if (x > max) {
					//
					max = x;
					//
					start = i;
					//
				} // if
					//
			} // for
				//
		} // for
			//
		return conditionA ? StringUtils.substring(a, start, start + max) : null;
		//
	}

	private static <T> T cast(final Class<T> clz, final Object instance) {
		return clz != null && clz.isInstance(instance) ? clz.cast(instance) : null;
	}

}