/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.statemachine.processor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.EnumSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.AbstractStateMachineTests;
import org.springframework.statemachine.ObjectStateMachine;
import org.springframework.statemachine.StateMachineSystemConstants;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

public class AnnotatedMethodTests extends AbstractStateMachineTests {

	@Override
	protected AnnotationConfigApplicationContext buildContext() {
		return new AnnotationConfigApplicationContext();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSimpleMachine() throws Exception {
		context.register(Config1.class, BeanConfig1.class);
		context.refresh();
		ObjectStateMachine<TestStates,TestEvents> machine =
				context.getBean(StateMachineSystemConstants.DEFAULT_ID_STATEMACHINE, ObjectStateMachine.class);
		Bean1 bean1 = context.getBean(Bean1.class);
		machine.start();
		assertThat(bean1.onMethod0Latch.await(2, TimeUnit.SECONDS), is(true));
		machine.sendEvent(TestEvents.E1);
		assertThat(bean1.onMethod1Latch.await(2, TimeUnit.SECONDS), is(true));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testRegions() throws Exception {
		context.register(Config2.class, BeanConfig1.class);
		context.refresh();
		ObjectStateMachine<TestStates,TestEvents> machine =
				context.getBean(StateMachineSystemConstants.DEFAULT_ID_STATEMACHINE, ObjectStateMachine.class);
		Bean1 bean1 = context.getBean(Bean1.class);
		machine.start();
		machine.sendEvent(TestEvents.E1);
		machine.sendEvent(TestEvents.E2);
		assertThat(bean1.onMethod2Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod4Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod6Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod8Latch.await(2, TimeUnit.SECONDS), is(true));
		machine.sendEvent(TestEvents.E3);
		assertThat(bean1.onMethod3Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod5Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod7Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod9Latch.await(2, TimeUnit.SECONDS), is(true));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testViaJoin() throws Exception {
		context.register(Config3.class, BeanConfig1.class);
		context.refresh();
		ObjectStateMachine<TestStates,TestEvents> machine =
				context.getBean(StateMachineSystemConstants.DEFAULT_ID_STATEMACHINE, ObjectStateMachine.class);
		Bean1 bean1 = context.getBean(Bean1.class);
		machine.start();
		machine.sendEvent(TestEvents.E1);
		machine.sendEvent(TestEvents.E2);
		assertThat(bean1.onMethod2Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod4Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod6Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod8Latch.await(2, TimeUnit.SECONDS), is(true));
		machine.sendEvent(TestEvents.E3);
		assertThat(bean1.onMethod3Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod5Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod7Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod9Latch.await(2, TimeUnit.SECONDS), is(true));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testViaJoinSuper() throws Exception {
		context.register(Config4.class, BeanConfig1.class);
		context.refresh();
		ObjectStateMachine<TestStates,TestEvents> machine =
				context.getBean(StateMachineSystemConstants.DEFAULT_ID_STATEMACHINE, ObjectStateMachine.class);
		Bean1 bean1 = context.getBean(Bean1.class);
		machine.start();
		machine.sendEvent(TestEvents.E1);
		machine.sendEvent(TestEvents.E2);
		assertThat(bean1.onMethod2Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod4Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod6Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod8Latch.await(2, TimeUnit.SECONDS), is(true));
		machine.sendEvent(TestEvents.E3);
		assertThat(bean1.onMethod3Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod5Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod7Latch.await(2, TimeUnit.SECONDS), is(true));
		assertThat(bean1.onMethod9Latch.await(2, TimeUnit.SECONDS), is(true));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testViaForkSuper() throws Exception {
		context.register(Config5.class, BeanConfig1.class);
		context.refresh();
		ObjectStateMachine<TestStates,TestEvents> machine =
				context.getBean(StateMachineSystemConstants.DEFAULT_ID_STATEMACHINE, ObjectStateMachine.class);
		Bean1 bean1 = context.getBean(Bean1.class);
		machine.start();
		machine.sendEvent(TestEvents.E1);
		machine.sendEvent(TestEvents.E2);
		assertThat(bean1.onMethod8Latch.await(2, TimeUnit.SECONDS), is(true));
		machine.sendEvent(TestEvents.E3);
		assertThat(bean1.onMethod9Latch.await(2, TimeUnit.SECONDS), is(true));
	}

	@WithStateMachine
	static class Bean1 {

		CountDownLatch onMethod0Latch = new CountDownLatch(1);
		CountDownLatch onMethod1Latch = new CountDownLatch(1);
		CountDownLatch onMethod2Latch = new CountDownLatch(1);
		CountDownLatch onMethod3Latch = new CountDownLatch(1);
		CountDownLatch onMethod4Latch = new CountDownLatch(1);
		CountDownLatch onMethod5Latch = new CountDownLatch(1);
		CountDownLatch onMethod6Latch = new CountDownLatch(1);
		CountDownLatch onMethod7Latch = new CountDownLatch(1);
		CountDownLatch onMethod8Latch = new CountDownLatch(1);
		CountDownLatch onMethod9Latch = new CountDownLatch(1);
		CountDownLatch onOnTransitionFromS2ToS3Latch = new CountDownLatch(1);

		@OnTransition(target = "S1")
		public void method0() {
			onMethod0Latch.countDown();
		}

		@OnTransition(source = "S1", target = "S2")
		public void method1() {
			onMethod1Latch.countDown();
		}

		@OnTransition(source = "S20", target = "S21")
		public void method2() {
			onMethod2Latch.countDown();
		}

		@OnTransition(source = "S30", target = "S31")
		public void method3() {
			onMethod3Latch.countDown();
		}

		@StatesOnTransition(source = TestStates.S20, target = TestStates.S21)
		public void method4() {
			onMethod4Latch.countDown();
		}

		@StatesOnTransition(source = TestStates.S30, target = TestStates.S31)
		public void method5() {
			onMethod5Latch.countDown();
		}

		@StatesOnTransition(target = TestStates.S21)
		public void method6() {
			onMethod6Latch.countDown();
		}

		@StatesOnTransition(target = TestStates.S31)
		public void method7() {
			onMethod7Latch.countDown();
		}

		@StatesOnTransition(target = TestStates.S20)
		public void method8() {
			onMethod8Latch.countDown();
		}

		@StatesOnTransition(target = TestStates.S30)
		public void method9() {
			onMethod9Latch.countDown();
		}

		@OnTransition
		public void onTransitionFromS2ToS3() {
			onOnTransitionFromS2ToS3Latch.countDown();
		}

		@Bean
		public String dummy() {
			return "dummy";
		}

	}

	@Configuration
	static class BeanConfig1 {

		@Bean
		public Bean1 bean1() {
			return new Bean1();
		}

	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@OnTransition
	public static @interface StatesOnTransition {

		TestStates[] source() default {};

		TestStates[] target() default {};

	}

	@Configuration
	@EnableStateMachine
	static class Config1 extends EnumStateMachineConfigurerAdapter<TestStates, TestEvents> {

		@Override
		public void configure(StateMachineStateConfigurer<TestStates, TestEvents> states) throws Exception {
			states
				.withStates()
					.initial(TestStates.S1)
					.states(EnumSet.allOf(TestStates.class));
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<TestStates, TestEvents> transitions) throws Exception {
			transitions
				.withExternal()
					.source(TestStates.S1)
					.target(TestStates.S2)
					.event(TestEvents.E1);
		}

	}

	@Configuration
	@EnableStateMachine
	static class Config2 extends EnumStateMachineConfigurerAdapter<TestStates, TestEvents> {

		@Override
		public void configure(StateMachineStateConfigurer<TestStates, TestEvents> states) throws Exception {
			states
				.withStates()
					.initial(TestStates.SI)
					.state(TestStates.S2)
					.and()
					.withStates()
						.parent(TestStates.S2)
						.initial(TestStates.S20)
						.state(TestStates.S20)
						.state(TestStates.S21)
						.and()
					.withStates()
						.parent(TestStates.S2)
						.initial(TestStates.S30)
						.state(TestStates.S30)
						.state(TestStates.S31);
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<TestStates, TestEvents> transitions) throws Exception {
			transitions
				.withExternal()
					.source(TestStates.SI)
					.target(TestStates.S2)
					.event(TestEvents.E1)
					.and()
				.withExternal()
					.source(TestStates.S20)
					.target(TestStates.S21)
					.event(TestEvents.E2)
					.and()
				.withExternal()
					.source(TestStates.S30)
					.target(TestStates.S31)
					.event(TestEvents.E3);
		}

	}

	@Configuration
	@EnableStateMachine
	static class Config3 extends EnumStateMachineConfigurerAdapter<TestStates, TestEvents> {

		@Override
		public void configure(StateMachineStateConfigurer<TestStates, TestEvents> states) throws Exception {
			states
				.withStates()
					.initial(TestStates.SI)
					.state(TestStates.S2)
					.join(TestStates.S3)
					.state(TestStates.S4)
					.and()
					.withStates()
						.parent(TestStates.S2)
						.initial(TestStates.S20)
						.end(TestStates.S21)
						.and()
					.withStates()
						.parent(TestStates.S2)
						.initial(TestStates.S30)
						.end(TestStates.S31);
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<TestStates, TestEvents> transitions) throws Exception {
			transitions
				.withExternal()
					.source(TestStates.SI)
					.target(TestStates.S2)
					.event(TestEvents.E1)
					.and()
				.withExternal()
					.source(TestStates.S20)
					.target(TestStates.S21)
					.event(TestEvents.E2)
					.and()
				.withExternal()
					.source(TestStates.S30)
					.target(TestStates.S31)
					.event(TestEvents.E3)
					.and()
				.withJoin()
					.source(TestStates.S21)
					.source(TestStates.S31)
					.target(TestStates.S3)
					.and()
				.withExternal()
					.source(TestStates.S3)
					.target(TestStates.S4);
		}

	}


	@Configuration
	@EnableStateMachine
	static class Config4 extends EnumStateMachineConfigurerAdapter<TestStates, TestEvents> {

		@Override
		public void configure(StateMachineStateConfigurer<TestStates, TestEvents> states) throws Exception {
			states
				.withStates()
					.initial(TestStates.SI)
					.state(TestStates.S2)
					.join(TestStates.S3)
					.state(TestStates.S4)
					.and()
					.withStates()
						.parent(TestStates.S2)
						.initial(TestStates.S20)
						.end(TestStates.S21)
						.and()
					.withStates()
						.parent(TestStates.S2)
						.initial(TestStates.S30)
						.end(TestStates.S31);
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<TestStates, TestEvents> transitions) throws Exception {
			transitions
				.withExternal()
					.source(TestStates.SI)
					.target(TestStates.S2)
					.event(TestEvents.E1)
					.and()
				.withExternal()
					.source(TestStates.S20)
					.target(TestStates.S21)
					.event(TestEvents.E2)
					.and()
				.withExternal()
					.source(TestStates.S30)
					.target(TestStates.S31)
					.event(TestEvents.E3)
					.and()
				.withJoin()
					.source(TestStates.S2)
					.target(TestStates.S3)
					.and()
				.withExternal()
					.source(TestStates.S3)
					.target(TestStates.S4);
		}

	}

	@Configuration
	@EnableStateMachine
	static class Config5 extends EnumStateMachineConfigurerAdapter<TestStates, TestEvents> {

		@Override
		public void configure(StateMachineStateConfigurer<TestStates, TestEvents> states) throws Exception {
			states
				.withStates()
					.initial(TestStates.SI)
					.fork(TestStates.S1)
					.state(TestStates.S2)
					.and()
					.withStates()
						.parent(TestStates.S2)
						.initial(TestStates.S20)
						.state(TestStates.S21)
						.and()
					.withStates()
						.parent(TestStates.S2)
						.initial(TestStates.S30)
						.state(TestStates.S31);
		}

		@Override
		public void configure(StateMachineTransitionConfigurer<TestStates, TestEvents> transitions) throws Exception {
			transitions
				.withExternal()
					.source(TestStates.SI)
					.target(TestStates.S1)
					.event(TestEvents.E1)
					.and()
				.withFork()
					.source(TestStates.S1)
					.target(TestStates.S2);
		}

	}

}
