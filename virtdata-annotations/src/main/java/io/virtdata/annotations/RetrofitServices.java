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

package io.virtdata.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If a module declaration has this annotation, then it will be used
 * to create pre-JPMS compatible entries in META-INF/services/....
 *
 * <P><EM>WARNING</EM></P>
 *
 * <P>Use of this annotation is risky, as namespace conflicts are possible
 * prior to JPMS within Jars. Different build tools have found ways around
 * this by merging same-named service files. If you use the pre-JPMS method
 * of service declaration, then you must ensure that this is done properly.</P>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.MODULE)
public @interface RetrofitServices {
    Class<?> value();
}
