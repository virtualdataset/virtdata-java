/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.rng.core.source32;

import org.apache.commons.rng.core.util.NumberFactory;

/**
 * This abstract class is a base for algorithms from the Xor-Shift-Rotate family of 32-bit
 * generators with 128-bits of state.
 *
 * @see <a href="http://xoshiro.di.unimi.it/">xorshiro / xoroshiro generators</a>
 *
 * @since 1.3
 */
abstract class AbstractXoShiRo128 extends IntProvider {
    /** Size of the state vector. */
    private static final int SEED_SIZE = 4;

    // State is maintained using variables rather than an array for performance

    /** State 0 of the generator. */
    protected int state0;
    /** State 1 of the generator. */
    protected int state1;
    /** State 2 of the generator. */
    protected int state2;
    /** State 3 of the generator. */
    protected int state3;

    /**
     * Creates a new instance.
     *
     * @param seed Initial seed.
     * If the length is larger than 4, only the first 4 elements will
     * be used; if smaller, the remaining elements will be automatically
     * set. A seed containing all zeros will create a non-functional generator.
     */
    AbstractXoShiRo128(int[] seed) {
        if (seed.length < SEED_SIZE) {
            final int[] state = new int[SEED_SIZE];
            fillState(state, seed);
            setState(state);
        } else {
            setState(seed);
        }
    }

    /**
     * Creates a new instance using a 4 element seed.
     * A seed containing all zeros will create a non-functional generator.
     *
     * @param seed0 Initial seed element 0.
     * @param seed1 Initial seed element 1.
     * @param seed2 Initial seed element 2.
     * @param seed3 Initial seed element 3.
     */
    AbstractXoShiRo128(int seed0, int seed1, int seed2, int seed3) {
        state0 = seed0;
        state1 = seed1;
        state2 = seed2;
        state3 = seed3;
    }

    /**
     * Copies the state from the array into the generator state.
     *
     * @param state the new state
     */
    private void setState(int[] state) {
        state0 = state[0];
        state1 = state[1];
        state2 = state[2];
        state3 = state[3];
    }

    /** {@inheritDoc} */
    @Override
    protected byte[] getStateInternal() {
        return composeStateInternal(NumberFactory.makeByteArray(
                                        new int[] {state0, state1, state2, state3}),
                                    super.getStateInternal());
    }

    /** {@inheritDoc} */
    @Override
    protected void setStateInternal(byte[] s) {
        final byte[][] c = splitStateInternal(s, SEED_SIZE * 4);

        setState(NumberFactory.makeIntArray(c[0]));

        super.setStateInternal(c[1]);
    }
}
