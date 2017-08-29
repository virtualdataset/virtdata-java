/*
*   Copyright 2016 jshook
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package io.virtdata.from_long.to_long;

import io.virtdata.api.Desc;
import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@Desc("divides the operand by an long and returns the whole part")
@ThreadSafeMapper
public class Div implements LongUnaryOperator {
    private int divisor;

    public Div(int divisor) {
        this.divisor = divisor;
    }

    @Override
    public long applyAsLong(long operand) {
        return operand / divisor;
    }
}
