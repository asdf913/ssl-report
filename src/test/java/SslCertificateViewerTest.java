import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.reflect.Reflection;

import io.github.toolfactory.narcissus.Narcissus;

public class SslCertificateViewerTest {

	private static Method METHOD_CAST, METHOD_FORMAT, METHOD_TO_DURATION, METHOD_TO_STRING, METHOD_GET_NAME,
			METHOD_LONGEST_COMMON_SUB_STRING, METHOD_ENDS_WITH = null;

	@BeforeClass
	static void beforeClass() throws NoSuchMethodException, ClassNotFoundException {
		//
		final Class<?> clz = SslCertificateViewer.class;
		//
		(METHOD_CAST = clz.getDeclaredMethod("cast", Class.class, Object.class)).setAccessible(true);
		//
		(METHOD_FORMAT = clz.getDeclaredMethod("format", DateFormat.class, Date.class)).setAccessible(true);
		//
		(METHOD_TO_DURATION = clz.getDeclaredMethod("toDuration", Date.class, Date.class)).setAccessible(true);
		//
		(METHOD_TO_STRING = clz.getDeclaredMethod("toString", Class.forName("SslCertificateViewer$Duration")))
				.setAccessible(true);
		//
		(METHOD_GET_NAME = clz.getDeclaredMethod("getName", Member.class)).setAccessible(true);
		//
		(METHOD_LONGEST_COMMON_SUB_STRING = clz.getDeclaredMethod("longestCommonSubstring", String.class, String.class))
				.setAccessible(true);
		//
		(METHOD_ENDS_WITH = clz.getDeclaredMethod("endsWith", String.class, String.class)).setAccessible(true);
		//
	}

	private static class IH implements InvocationHandler {

		private Boolean test, anyMatch;

		private Integer size;

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			//
			final String name = getName(method);
			//
			if (Objects.equals(method != null ? method.getReturnType() : null, Void.TYPE)) {
				//
				return null;
				//
			} // if
				//
			if (proxy instanceof Collection) {
				//
				if (Objects.equals(name, "stream")) {
					//
					return null;
					//
				} else if (Objects.equals(name, "size")) {
					//
					return size;
					//
				} // if
					//
			} // if
				//
			if (proxy instanceof Principal && Objects.equals(name, "getName")) {
				//
				return null;
				//
			} else if (proxy instanceof Map && contains(Arrays.asList("get", "put", "keySet", "values"), name)) {
				//
				return null;
				//
			} else if (proxy instanceof Predicate && Objects.equals(name, "test")) {
				//
				return test;
				//
			} else if (proxy instanceof FailableFunction && Objects.equals(name, "apply")) {
				//
				return null;
				//
			} else if (proxy instanceof Stream) {
				//
				if (contains(Arrays.asList("collect", "filter", "mapToInt"), name)) {
					//
					return null;
					//
				} else if (Objects.equals(name, "anyMatch")) {
					//
					return anyMatch;
					//
				} // if
					//
			} else if (proxy instanceof List && Objects.equals(name, "get")) {
				//
				return null;
				//
			} else if (proxy instanceof Member && Objects.equals(name, "getName")) {
				//
				return null;
				//
			} else if (proxy instanceof IntStream && Objects.equals(name, "max")) {
				//
				return null;
				//
			} // if
				//
			throw new Throwable(name);
			//
		}

	}

	private static boolean contains(final Collection<?> instance, final Object item) {
		return instance != null && instance.contains(item);
	}

	private static String getName(final Member instance) throws Throwable {
		try {
			final Object obj = invoke(METHOD_GET_NAME, null, instance);
			if (obj == null) {
				return null;
			} else if (obj instanceof String) {
				return (String) obj;
			}
			throw new Throwable(Objects.toString(getClass(instance)));
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private DateFormat df = null;

	@BeforeMethod
	void beforeMethod() {
		//
		df = new SimpleDateFormat("yyyy-MM-dd");
		//
	}

	@Test
	void testNull() throws Throwable {
		//
		final Method[] ms = SslCertificateViewer.class.getDeclaredMethods();
		//
		Method m = null;
		//
		Class<?>[] parameterTypes = null;
		//
		Class<?> parameterType = null;
		//
		Object result = null;
		//
		String toString, name = null;
		//
		Collection<Object> collection = null;
		//
		for (int i = 0; ms != null && i < ms.length; i++) {
			//
			if ((m = ArrayUtils.get(ms, i)) == null || m.isSynthetic()
					|| (parameterTypes = m.getParameterTypes()) == null) {
				//
				continue;
				//
			} // if
				//
			clear(collection = ObjectUtils.getIfNull(collection, ArrayList::new));
			//
			for (int j = 0; j < parameterTypes.length; j++) {
				//
				if (Objects.equals(parameterType = ArrayUtils.get(parameterTypes, j), Character.TYPE)) {
					//
					add(collection, Character.valueOf(' '));
					//
				} else if (Objects.equals(parameterType, Integer.TYPE)) {
					//
					add(collection, Integer.valueOf(0));
					//
				} else {
					//
					add(collection, null);
					//
				} // if
					//
			} // for
				//
			result = Narcissus.invokeStaticMethod(m, toArray(collection));
			//
			toString = Objects.toString(m);
			//
			if (contains(Arrays.asList(Boolean.TYPE, Integer.TYPE), m.getReturnType())
					|| Boolean.logicalOr(Objects.equals(name = m.getName(), "getEntry"),
							Arrays.equals(parameterTypes, new Class<?>[] { String.class }))
					|| Boolean.logicalOr(Objects.equals(name, "toDuration"),
							Arrays.equals(parameterTypes, new Class<?>[] { Date.class, Date.class }))) {
				//
				Assert.assertNotNull(result, toString);
				//
			} else {
				//
				Assert.assertNull(result, toString);
				//
			} // if
				//
		} // for
			//
	}

	private static <E> void add(final Collection<E> instance, final E item) {
		if (instance != null) {
			instance.add(item);
		}
	}

	private static void clear(final Collection<?> instance) {
		if (instance != null) {
			instance.clear();
		}
	}

	private static Object[] toArray(final Collection<?> instance) {
		return instance != null ? instance.toArray() : null;
	}

	@Test
	void testNotNull() throws Throwable {
		//
		final Method[] ms = SslCertificateViewer.class.getDeclaredMethods();
		//
		Method m = null;
		//
		Class<?>[] parameterTypes = null;
		//
		Class<?> parameterType = null;
		//
		Object result = null;
		//
		String toString, name = null;
		//
		Collection<Object> collection = null;
		//
		IH ih = null;
		//
		for (int i = 0; ms != null && i < ms.length; i++) {
			//
			if ((m = ArrayUtils.get(ms, i)) == null || m.isSynthetic()
					|| (parameterTypes = m.getParameterTypes()) == null) {
				//
				continue;
				//
			} // if
				//
			clear(collection = ObjectUtils.getIfNull(collection, ArrayList::new));
			//
			for (int j = 0; j < parameterTypes.length; j++) {
				//
				if (Objects.equals(parameterType = ArrayUtils.get(parameterTypes, j), Character.TYPE)) {
					//
					add(collection, Character.valueOf(' '));
					//
				} else if (parameterType != null && parameterType.isInterface()) {
					//
					if ((ih = ObjectUtils.getIfNull(ih, IH::new)) != null) {
						//
						final List<Field> fs = FieldUtils.getAllFieldsList(getClass(ih));
						//
						Field f = null;
						//
						for (int k = 0; fs != null && k < fs.size(); k++) {
							//
							if ((f = fs.get(k)) == null) {
								//
								continue;
								//
							} // if
								//
							final Class<?> type = f.getType();
							//
							if (Objects.equals(type, Boolean.class)) {
								//
								Narcissus.setField(ih, f, Boolean.TRUE);
								//
							} else if (Objects.equals(type, Integer.class)) {
								//
								Narcissus.setField(ih, f, Integer.valueOf(0));
								//
							} // if
								//
						} // for
							//
					} // if
						//
					add(collection, Reflection.newProxy(parameterType, ih = ObjectUtils.getIfNull(ih, IH::new)));
					//
				} else if (parameterType != null && parameterType.isArray()) {
					//
					add(collection, Array.newInstance(parameterType.getComponentType(), 0));
					//
				} else if (Objects.equals(parameterType, Class.class)) {
					//
					add(collection, Class.class);
					//
				} else if (Objects.equals(parameterType, DateFormat.class)) {
					//
					add(collection, Narcissus.allocateInstance(SimpleDateFormat.class));
					//
				} else if (Objects.equals(parameterType, Calendar.class)) {
					//
					add(collection, Narcissus.allocateInstance(getClass(Calendar.getInstance())));
					//
				} else if (Objects.equals(parameterType, Integer.TYPE)) {
					//
					add(collection, Integer.valueOf(0));
					//
				} else if (Objects.equals(parameterType, HttpsURLConnection.class)) {
					//
					add(collection, Narcissus
							.allocateInstance(Class.forName("sun.net.www.protocol.https.HttpsURLConnectionImpl")));
					//
				} else if (Objects.equals(parameterType, X509Certificate.class)) {
					//
					add(collection, Narcissus.allocateInstance(Class.forName("sun.security.x509.X509CertImpl")));
					//
				} else {
					//
					add(collection, Narcissus.allocateInstance(parameterType));
					//
				} // if
					//
			} // for
				//
			result = Narcissus.invokeStaticMethod(m, toArray(collection));
			//
			toString = Objects.toString(m);
			//
			if (contains(Arrays.asList(Boolean.TYPE, Integer.TYPE), m.getReturnType())
					|| Boolean.logicalOr(Objects.equals(name = m.getName(), "getEntry"),
							Arrays.equals(parameterTypes, new Class<?>[] { String.class }))
					|| Boolean.logicalOr(Objects.equals(name, "toDuration"),
							Arrays.equals(parameterTypes, new Class<?>[] { Date.class, Date.class }))
					|| Boolean.logicalOr(Objects.equals(name, "toString"),
							Arrays.equals(parameterTypes,
									new Class<?>[] { Class.forName("SslCertificateViewer$Duration") }))
					|| Boolean.logicalOr(Objects.equals(name, "append"),
							Arrays.equals(parameterTypes, new Class<?>[] { StringBuilder.class, Object.class }))
					|| Boolean.logicalOr(Objects.equals(name, "getClass"),
							Arrays.equals(parameterTypes, new Class<?>[] { Object.class }))
					|| Boolean.logicalOr(Objects.equals(name, "delete"), Arrays.equals(parameterTypes,
							new Class<?>[] { StringBuilder.class, Integer.TYPE, Integer.TYPE }))) {
				//
				Assert.assertNotNull(result, toString);
				//
			} else {
				//
				Assert.assertNull(result, toString);
				//
			} // if
				//
		} // for
			//
	}

	private static Class<?> getClass(final Object instance) {
		return instance != null ? instance.getClass() : null;
	}

	@Test
	public void testMain() throws Throwable {
		//
		SslCertificateViewer.main(new String[] { cast(String.class, Narcissus.allocateInstance(String.class)) });
		//
	}

	@Test
	public void testCast() throws Throwable {
		//
		final Object object = new Object();
		//
		Assert.assertSame(cast(Object.class, object), object);
		//
	}

	private static <T> T cast(final Class<T> clz, final Object instance) throws Throwable {
		try {
			return (T) invoke(METHOD_CAST, null, clz, instance);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static Object invoke(final Method method, final Object instance, final Object... args)
			throws IllegalAccessException, InvocationTargetException {
		return method != null && method.getDeclaringClass() != null ? method.invoke(instance, args) : null;
	}

	@Test
	public void testFormat() throws IllegalAccessException, InvocationTargetException, ParseException {
		//
		final String string = "2001-02-03";
		//
		Assert.assertEquals(invoke(METHOD_FORMAT, null, df, parse(df, string)), string);
		//
	}

	private static Date parse(final DateFormat instance, final String source) throws ParseException {
		return instance != null ? instance.parse(source) : null;
	}

	@Test
	public void testToString() throws IllegalAccessException, InvocationTargetException, ParseException {
		//
		final String string = "2001-02-03";
		//
		final Date date = parse(df, string);
		//
		Assert.assertEquals(
				invoke(METHOD_TO_STRING, null, (invoke(METHOD_TO_DURATION, null, date, DateUtils.addDays(date, -3)))),
				" 3 Day");
		//
		Assert.assertEquals(
				invoke(METHOD_TO_STRING, null, (invoke(METHOD_TO_DURATION, null, date, DateUtils.addMonths(date, -2)))),
				" 2 Month");
		//
		Assert.assertEquals(
				invoke(METHOD_TO_STRING, null, (invoke(METHOD_TO_DURATION, null, date, DateUtils.addYears(date, -1)))),
				"1 Year");
		//
	}

	@Test
	public void testLongestCommonSubstring() throws IllegalAccessException, InvocationTargetException, ParseException {
		//
		Assert.assertEquals(invoke(METHOD_LONGEST_COMMON_SUB_STRING, null, "abc", "bcd"), "bc");
		//
		Assert.assertEquals(
				invoke(METHOD_LONGEST_COMMON_SUB_STRING, null, "abc", Narcissus.allocateInstance(String.class)), "");
		//
	}

	@Test
	public void testEndsWith() throws IllegalAccessException, InvocationTargetException, ParseException {
		//
		final String s = "s";
		//
		Assert.assertEquals(invoke(METHOD_ENDS_WITH, null, s, s), Boolean.TRUE);
		//
		Assert.assertEquals(invoke(METHOD_ENDS_WITH, null, s, Narcissus.allocateInstance(String.class)), Boolean.FALSE);
		//
	}

}