package com.zevinar.crypto.utils;

import static java.lang.Thread.sleep;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import fj.data.Either;

/**
 * Utility class for functional code
 * 
 * @author ms172g
 *
 */
public final class FunctionalCodeUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(FunctionalCodeUtils.class);

	private FunctionalCodeUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns enum value for a field <br>
	 * See usage example at
	 * {@link SessionAttributeEnum#findByName(String name) } <br>
	 * 
	 * @param field
	 * @param values
	 * @param enumValueGetter
	 * @return
	 */
	public static <T extends Enum<T>> T getEnumValueByField(String field, T[] values,
			Function<T, String> enumValueGetter) {
		T ret;
		Optional<T> optionalFound =
				// Stream of values of enum
				Arrays.asList(values).stream().
				// Filter in the one that match the field
						filter(e -> field.equals(enumValueGetter.apply(e))).
						// collect
						findAny();
		if (optionalFound.isPresent()) {
			ret = optionalFound.get();
		} else {
			throw new IllegalArgumentException("No Enum was found for:" + field.toString());
		}
		return ret;

	}

	/**
	 * This method runs the given runnable.<br>
	 * In case exception has occurred it uses the consumer to handle it.<br>
	 * 
	 * @param methodToRun
	 * @param handler
	 */
	public static void methodRunner(Runnable methodToRun, Consumer<Exception> handler) {
		try {
			methodToRun.run();
		} catch (Exception e) {
			handler.accept(e);
		}
	}

	/**
	 * This method runs the given runnable.<br>
	 * In case exception has occurred it uses the consumer to handle it.<br>
	 * 
	 * @param methodToRun
	 * @param handler
	 */
	public static <E extends Exception> void methodRunner(RunnableThrows<E> methodToRun, Consumer<Exception> handler) {
		try {
			methodToRun.run();
		} catch (Exception e) {
			handler.accept(e);
		}
	}

	/**
	 * This method runs the given supplier.<br>
	 * In case exception has occurred it uses the consumer to handle it. and
	 * returns null<br>
	 * 
	 * @param methodToRun
	 * @param handler
	 */
	public static <E extends Exception, R> R methodRunner(SupplierThrows<E, R> methodToRun,
			Consumer<Exception> handler) {
		R returnVal;
		try {
			returnVal = methodToRun.get();
		} catch (Exception e) {
			handler.accept(e);
			returnVal = null;
		}
		return returnVal;
	}

	/**
	 * This method runs the given runnable.<br>
	 * In case exception has occurred it logs it.<br>
	 * 
	 * @param methodToRun
	 */
	public static <E extends Exception> void methodRunner(RunnableThrows<E> methodToRun) {
		Consumer<Exception> handler = FunctionalCodeUtils::logException;
		methodRunner(methodToRun, handler);

	}

	/**
	 * This method runs the given supplier and returns its value.<br>
	 * In case exception has occurred it logs it and returns null.<br>
	 * 
	 * @param methodToRun
	 */
	public static <E extends Exception, R> R methodRunner(SupplierThrows<E, R> methodToRun) {
		Consumer<Exception> handler = FunctionalCodeUtils::logException;
		return methodRunner(methodToRun, handler);

	}

	/**
	 * This method runs the given runnable.<br>
	 * In case exception has occurred it logs it.<br>
	 * 
	 * @param methodToRun
	 */
	public static void methodRunner(Runnable methodToRun) {
		Consumer<Exception> handler = FunctionalCodeUtils::logException;
		methodRunner(methodToRun, handler);

	}

	/**
	 * This method runs the given runnable.<br>
	 * In case exception has occurred it throws it.<br>
	 * 
	 * @param methodToRun
	 */
	public static <E extends Exception> void throwsExceptionMethodRunner(RunnableThrows<E> methodToRun) {
		Consumer<Exception> handler = FunctionalCodeUtils::throwException;
		methodRunner(methodToRun, handler);

	}

	/**
	 * This method runs the given Supplier.<br>
	 * In case exception has occurred it throws it.<br>
	 * 
	 * @param methodToRun
	 */
	public static <E extends Exception, R> R throwsExceptionMethodRunner(SupplierThrows<E, R> methodToRun) {
		R result;
		try {
			result = methodToRun.get();
		} catch (Exception e) {
			throw new FunctionalCodeException(e);
		}
		return result;

	}

	/**
	 * This method runs the given method.<br>
	 * In case given method finished running within timeoutInMs limit it returns
	 * true.<br>
	 * In case given method did not finish running within timeoutInMs limit it
	 * returns false. <br>
	 * 
	 * @param runnable
	 * @param timeoutInMs
	 * @return
	 */
	public static boolean runMethodWithTimeOut(Runnable runnable, long timeoutInMs) {
		Supplier<Boolean> supplier = () -> {
			runnable.run();
			return true;
		};
		Either<Boolean, Boolean> eitherSuccessfulRun = runMethodWithTimeOut(supplier, timeoutInMs);
		return eitherSuccessfulRun.isLeft();
	}

	/**
	 * @see FunctionalCodeUtils#runMethodWithTimeOut(Runnable, long)
	 * @param runnable
	 * @param timeoutInMs
	 * @return
	 */
	public static <E extends Exception> boolean runMethodWithTimeOut(RunnableThrows<E> runnable, long timeoutInMs) {
		Supplier<Boolean> supplier = () -> {
			FunctionalCodeUtils.methodRunner(runnable);
			return true;
		};
		Either<Boolean, Boolean> eitherSuccessfulRun = runMethodWithTimeOut(supplier, timeoutInMs);
		return eitherSuccessfulRun.isLeft();
	}

	/**
	 * This method runs the given method.<br>
	 * In case given method finished running within timeoutInMs limit it returns
	 * Either which left value is the result of the method that ran.<br>
	 * In case given method did not finish running within timeoutInMs limit it
	 * returns Either which right value is false. <br>
	 * 
	 * @param supplier
	 * @param timeoutInMs
	 * @return
	 */
	public static <T> Either<T, Boolean> runMethodWithTimeOut(Supplier<T> supplier, long timeoutInMs) {
		Either<T, Boolean> result;
		if (timeoutInMs <= NumberUtils.LONG_ZERO) {
			result = Either.left(supplier.get());
		} else {
			ExecutorService pool = Executors.newSingleThreadExecutor();
			Future<T> future = pool.submit(supplier::get);
			try {
				T calcValue = future.get(timeoutInMs, TimeUnit.MILLISECONDS);
				result = Either.left(calcValue);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				LOGGER.debug("method run was canceled because it has passed its time limit of {} MS", timeoutInMs, e);
				result = Either.right(false);
			} finally {
				pool.shutdownNow();
			}
		}
		return result;
	}

	/**
	 * This method runs the given method.<br>
	 * In case given method finished running within timeoutInMs limit it returns
	 * Either which left value is the result of the method that ran.<br>
	 * In case given method did not finish running within timeoutInMs limit it
	 * returns Either which right value is false. <br>
	 * 
	 * @param supplier
	 * @param timeoutInMs
	 * @return
	 */

	public static <T> Either<T, Boolean> runMethodWithTimeOutFixedPool(Supplier<T> supplier, long timeoutInMs,
			ExecutorService fixedpool) {
		Either<T, Boolean> result;
		if (timeoutInMs <= NumberUtils.LONG_ZERO) {
			result = Either.left(supplier.get());
		} else {

			Future<T> future = fixedpool.submit(supplier::get);
			try {
				T calcValue = future.get(timeoutInMs, TimeUnit.MILLISECONDS);
				result = Either.left(calcValue);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				LOGGER.debug("failed due to ", e);
				LOGGER.debug("method run was canceled due to {} because it has passed its time limit of {} MS. ",
						e.toString(), timeoutInMs);
				result = Either.right(false);
			}

			LOGGER.debug("fixedpool data is {}", fixedpool);
		}
		return result;
	}

	/**
	 * Runs the given method.<br>
	 * Verify the method result against the resultVerifier.<br>
	 * If verification passed returns the result.<br>
	 * If Verification failed keeps retrying until it passes or until maxNumberOfTries is reached.<br>
	 * 
	 * @param methodToRun
	 *            given Method
	 * @param resultVerifier
	 *            verifier for the method result
	 * @param maxNumberOfTries
	 * @param retryIntervalMS
	 * @return
	 */
	public static <R> R retryMethodOnResult(Supplier<R> methodToRun, Function<R, Boolean> resultVerifier,
			int maxNumberOfTries, long retryIntervalMS) {
		if( maxNumberOfTries <= 0 ){
			throw new UnsupportedOperationException("Number of Tries Should be greater than 0");
		}
		int currNumberOfTries = NumberUtils.INTEGER_ONE;
		R ret = methodToRun.get();
		boolean stopSearch = resultVerifier.apply(ret) || currNumberOfTries >= maxNumberOfTries;
		while (!stopSearch) {
			throwsExceptionMethodRunner(() -> sleep(retryIntervalMS));
			ret = methodToRun.get();
			currNumberOfTries++;
			stopSearch = resultVerifier.apply(ret) || currNumberOfTries >= maxNumberOfTries;

		}

		return ret;

	}

	/**
	 * Check if the given element matches any of the Elements in the array
	 * 
	 * @param element
	 * @param elementArray
	 * @return
	 */
	@SafeVarargs
	public static <T> boolean matchElementInArray(T element, T... elementArray) {
		boolean ret;
		List<T> list = Arrays.asList(elementArray);
		if (element == null || list.isEmpty()) {
			ret = false;
		} else {
			ret = list.contains(element);
		}
		return ret;
	}

	private static void logException(Exception e) {
		LOGGER.error("Unexpected Exception was caught:", e.getMessage());
		LOGGER.debug("Unexpected Exception was caught:", e.getMessage(), e);
	}

	private static void throwException(Exception e) {
		throw new FunctionalCodeException(e);
	}

	private static class FunctionalCodeException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public FunctionalCodeException(Exception e) {
			super(e);
		}

	}
	
	/**
	 * This method evaluates a method that return an either result.<br>
	 * In case an exception occurred it throws it - otherwise it returns the result.<br>
	 * @param supplier
	 * @return
	 * @throws E
	 */
	public static <R, E extends Exception> R ignoreEitherResult(Supplier<Either<R,E>> supplier){
		Either<R, E> either = supplier.get();
		if( either.isLeft() ){
			return either.left().value();
		}
		else{
			throw new FunctionalCodeException(either.right().value());
		}
	}

	
	/**
	 * This method wraps a method that might throw an exception with an Either
	 * @param runMe
	 * @return
	 */
	public static <R, E extends Exception> Either<R,E> wrapWithEither(SupplierThrows<E, R> runMe){
		Either<R,E> eitherResult;
		try{
			
			R result = runMe.get();
			eitherResult = Either.left(result);
		}
		catch( Exception e){
			@SuppressWarnings("unchecked")
			Either<R, E> tempEither = (Either<R, E>)Either.right(e);
			eitherResult = tempEither;
		}
		return eitherResult;
	}
	
	/**
	 * This method wraps a method that might throw an exception with an Either
	 * @param runMe
	 * @return
	 */
	public static <E extends Exception> Either<Boolean,E> wrapWithEither(Runnable runMe){
		return wrapWithEither(() ->  {runMe.run(); return true;});
		
	}
	

	/**
	 * Functional Interface Similar to Runnable except that throws Exception
	 * 
	 * @author ms172g
	 *
	 * @param <E>
	 */
	@FunctionalInterface
	public interface RunnableThrows<E extends Exception> {
		/**
		 * Similar to run of Runnable except that throws Exception
		 * 
		 * @throws E
		 */
		void run() throws E;

	}
	
	@FunctionalInterface
	public interface FunctionThrows<T, R, E extends Exception> {
	    R apply(T arg) throws E;
	}

	/**
	 * Functional Interface Similar to Supplier except that throws Exception
	 * 
	 * @author ms172g
	 *
	 * @param <E>
	 * @param <R>
	 */
	@FunctionalInterface
	public interface SupplierThrows<E extends Exception, R> {
		/**
		 * Similar to run of Runnable except that throws Exception
		 * 
		 * @throws E
		 */
		R get() throws E;

	}

	@FunctionalInterface
	public interface BiConsumerEx<T, U> {

		/**
		 * Performs this operation on the given arguments.
		 *
		 * @param t
		 *            the first input argument
		 * @param u
		 *            the second input argument
		 */
		void accept(T t, U u);
	}

	@FunctionalInterface
	public interface ThreeParamsConsumer<F, S, T, E extends Exception> {

		/**
		 * Performs this operation on the given arguments.
		 *
		 * @param f
		 *            the first input argument
		 * @param s
		 *            the second input argument
		 * @param t
		 *            the third input argument
		 */
		void accept(F f, S s, T t) throws E;
	}

	
}
