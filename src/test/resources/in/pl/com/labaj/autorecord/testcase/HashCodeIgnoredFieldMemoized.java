package pl.com.labaj.autorecord.testcase;

/*-
 * Copyright © 2023 Auto Record
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import pl.com.labaj.autorecord.AutoRecord;
import pl.com.labaj.autorecord.Ignored;
import pl.com.labaj.autorecord.test.Counters;

@AutoRecord.Options(memoizedHashCode = true)
@AutoRecord()
public interface HashCodeIgnoredFieldMemoized {
    String used();

    int anotherUsed();

    @Ignored
    String ignored();

    @Ignored
    Counters anotherIgnored();
}