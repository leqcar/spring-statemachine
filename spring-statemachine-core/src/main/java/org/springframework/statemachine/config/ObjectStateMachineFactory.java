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
package org.springframework.statemachine.config;

import java.util.Collection;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.ObjectStateMachine;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.builders.StateMachineStates;
import org.springframework.statemachine.config.builders.StateMachineTransitions;
import org.springframework.statemachine.state.ObjectState;
import org.springframework.statemachine.state.PseudoState;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

/**
 * Implementation of a {@link StateMachineFactory} which know the actual types of
 * {@link State} and {@link StateMachine}.
 *
 * @author Janne Valkealahti
 *
 * @param <S> the type of state
 * @param <E> the type of event
 */
public class ObjectStateMachineFactory<S, E> extends AbstractStateMachineFactory<S, E> {

	public ObjectStateMachineFactory(StateMachineTransitions<S, E> stateMachineTransitions,
			StateMachineStates<S, E> stateMachineStates) {
		super(stateMachineTransitions, stateMachineStates);
	}

	@Override
	protected StateMachine<S, E> buildStateMachineInternal(Collection<State<S, E>> states,
			Collection<Transition<S, E>> transitions, State<S, E> initialState, Transition<S, E> initialTransition,
			Message<E> initialEvent, ExtendedState extendedState, PseudoState<S, E> historyState, Boolean contextEventsEnabled, BeanFactory beanFactory) {
		ObjectStateMachine<S, E> machine = new ObjectStateMachine<S, E>(states, transitions, initialState, initialTransition, initialEvent,
				extendedState);
		machine.setHistoryState(historyState);
		if (contextEventsEnabled != null) {
			machine.setContextEventsEnabled(contextEventsEnabled);
		}
		if (beanFactory != null) {
			machine.setBeanFactory(beanFactory);
		}
		machine.afterPropertiesSet();
		return machine;
	}

	@Override
	protected State<S, E> buildStateInternal(S id, Collection<E> deferred, Collection<? extends Action<S, E>> entryActions,
			Collection<? extends Action<S, E>> exitActions, PseudoState<S, E> pseudoState) {
		return new ObjectState<S, E>(id, deferred, entryActions, exitActions, pseudoState);
	}

}
